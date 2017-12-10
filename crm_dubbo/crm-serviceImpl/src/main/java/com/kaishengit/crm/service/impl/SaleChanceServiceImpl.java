package com.kaishengit.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.entity.SaleChanceRecord;
import com.kaishengit.crm.example.SaleChanceExample;
import com.kaishengit.crm.example.SaleChanceRecordExample;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.mapper.SaleChanceMapper;
import com.kaishengit.crm.mapper.SaleChanceRecordMapper;
import com.kaishengit.crm.service.SaleChanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hoyt on 2017/11/13.
 */

@Service
public class SaleChanceServiceImpl implements SaleChanceService{

    @Autowired
    private SaleChanceMapper saleChanceMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private SaleChanceRecordMapper saleChanceRecordMapper;

    @Value("#{'${saleChance.progress}'.split(',')}")
    private List<String> progressList;
    /**
     * 获得所有记录列表
     *
     * @param pageNo
     * @param account
     * @return
     */
    @Override
    public PageInfo<SaleChance> pageForSaleChance(Integer pageNo, Account account) {
        PageHelper.startPage(pageNo,15);
        List<SaleChance> list = saleChanceMapper.findByAccountId(account.getId());
        return new PageInfo<>(list);
    }

    /**
     * 新增记录
     * @param saleChance
     */
    @Override
    @Transactional
    public void saveNewRecord(SaleChance saleChance) {
        saleChance.setCreateTime(new Date());
        saleChance.setLastTime(new Date());
        //更新对应客户的最后跟进时间
        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContractTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
        saleChanceMapper.insert(saleChance);

        //判断销售机会的详细内容是否有值，如果存在，则保存一条跟进记录
        if(StringUtils.isNotEmpty(saleChance.getContent())) {
            SaleChanceRecord record = new SaleChanceRecord();
            record.setContent(saleChance.getContent());
            record.setCreateTime(new Date());
            record.setSaleId(saleChance.getId());
            saleChanceRecordMapper.insert(record);
        }

    }

    @Override
    public List<String> findAllProgress() {
        return progressList;
    }

    /**
     * 根据id获得记录
     *
     * @param id
     * @return
     */
    @Override
    public SaleChance findById(Integer id) {
        return saleChanceMapper.selectByPrimaryKey(id);
    }

    /**
     * 更改进度
     * @param progress
     */
    @Override
    public void changeProgress(Integer id, String progress) {
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        saleChance.setProgress(progress);
        saleChanceMapper.updateByPrimaryKeySelective(saleChance);
    }

    /**
     * 根据主键查询销售机会
     *
     * @param id
     * @return
     */
    @Override
    public SaleChance findSalesChanceWithCustomerById(Integer id) {
        return saleChanceMapper.findChanceWithCustomerById(id);
    }

    /**
     * 改变工作记录跟进状态
     * @param id
     * @param progress
     * @return
     */
    @Override
    public void updateSalesChanceState(Integer id, String progress) {
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        //改变进度
        saleChance.setProgress(progress);
        //销售机会的最后跟进时间
        saleChance.setLastTime(new Date());
        saleChanceMapper.updateByPrimaryKeySelective(saleChance);

        //改变关联客户的最后跟进时间
        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContractTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);

        //添加跟进记录
        SaleChanceRecord record = new SaleChanceRecord();
        record.setCreateTime(new Date());
        record.setSaleId(id);
        record.setContent("将当前进度修改为：["+ progress +"]");
        saleChanceRecordMapper.insert(record);
    }

    /**
     * 添加跟进记录
     * @param record
     */
    @Override
    public void saveNewSalesChanceRecode(SaleChanceRecord record) {
        record.setCreateTime(new Date());
        saleChanceRecordMapper.insert(record);

        //工作记录的最后跟进时间
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(record.getSaleId());
        saleChance.setLastTime(new Date());
        saleChanceMapper.updateByPrimaryKeySelective(saleChance);

        //改变关联客户的最后跟进时间
        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        customer.setLastContractTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 获得跟进记录列表
     * @param id
     * @return
     */
    @Override
    public List<SaleChanceRecord> findSalesChanceRecodeListBySalesId(Integer id) {
        SaleChanceRecordExample recordExample = new SaleChanceRecordExample();
        recordExample.createCriteria().andSaleIdEqualTo(id);
        return saleChanceRecordMapper.selectByExampleWithBLOBs(recordExample);
    }

    /**
     * 根据客户ID查询对应的销售记录
     * @param id
     * @return
     */
    @Override
    public List<SaleChance> findSalesChanceByCustId(Integer id) {
        SaleChanceExample example = new SaleChanceExample();
        example.createCriteria().andCustIdEqualTo(id);
        example.setOrderByClause("last_time desc");

        return saleChanceMapper.selectByExample(example);
    }

    /**
     * 删除工作记录
     * @param id
     */
    @Override
    public void deleteSalesChanceById(Integer id) {
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);

        //1.删除对应的跟进记录
        SaleChanceRecordExample recordExample = new SaleChanceRecordExample();
        recordExample.createCriteria().andSaleIdEqualTo(id);
        saleChanceRecordMapper.deleteByExample(recordExample);

        //2.删除工作记录
        saleChanceMapper.deleteByPrimaryKey(id);


        //3.将对应客户的最后跟进时间设置为null或空
        //判断该客户是否还有其他工作记录，如果没有最后跟进时间设置为null 或 空
        //如果有，则修改为最近的工作记录最后跟进时间
        SaleChanceExample saleChanceExample = new SaleChanceExample();
        saleChanceExample.createCriteria().andCustIdEqualTo(saleChance.getCustId());
        saleChanceExample.setOrderByClause("last_time desc");
        List<SaleChance> saleChanceList = saleChanceMapper.selectByExample(saleChanceExample);

        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustId());
        if(saleChanceList.isEmpty()) {
            customer.setLastContractTime(null);
        } else {
            customer.setLastContractTime(saleChanceList.get(0).getLastTime());
        }
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 销售进度列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findProgressCount() {
        return saleChanceMapper.findProgressCount();
    }


}
