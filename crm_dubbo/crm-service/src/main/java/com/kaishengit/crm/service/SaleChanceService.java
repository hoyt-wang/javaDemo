package com.kaishengit.crm.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.entity.SaleChanceRecord;

import java.util.List;
import java.util.Map;

/**
 * Created by hoyt on 2017/11/13.
 */
public interface SaleChanceService {




    /**
     * 获得所有记录列表
     * @param pageNo
     * @param account
     * @return
     */
    PageInfo<SaleChance> pageForSaleChance(Integer pageNo, Account account);


    /**
     * 新增记录
     * @param saleChance
     */
    void saveNewRecord(SaleChance saleChance);

    List<String> findAllProgress();

    /**
     * 根据id获得记录
     * @param id
     * @return
     */
    SaleChance findById(Integer id);

    /**
     * 更改进度
     * @param progress
     */
    void changeProgress(Integer id, String progress);

    /**
     * 根据主键查询销售机会
     * @param id
     * @return
     */
    SaleChance findSalesChanceWithCustomerById(Integer id);

    /**
     * 改变工作记录跟进状态
     * @return
     */
    void updateSalesChanceState(Integer id, String progress);

    /**
     * 添加跟进记录
     * @param record
     */
    void saveNewSalesChanceRecode(SaleChanceRecord record);

    /**
     * 获得跟进记录列表
     * @param id
     * @return
     */
    List<SaleChanceRecord> findSalesChanceRecodeListBySalesId(Integer id);

    /**
     * 根据客户ID查询对应的销售记录
     * @param id
     * @return
     */
    List<SaleChance> findSalesChanceByCustId(Integer id);

    /**
     * 删除工作记录
     * @param id
     */
    void deleteSalesChanceById(Integer id);

    /**
     * 销售进度列表
     * @return
     */
    List<Map<String,Object>> findProgressCount();
}
