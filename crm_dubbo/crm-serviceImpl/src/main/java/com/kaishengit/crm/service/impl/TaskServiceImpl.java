package com.kaishengit.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.example.TaskExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.jobs.SendMessageJob;
import com.kaishengit.crm.mapper.TaskMapper;
import com.kaishengit.crm.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by hoyt on 2017/11/14.
 */

@Service
public class TaskServiceImpl implements TaskService {

    Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 根据id查找对应的待办事项
     * @param id
     * @return
     */
    @Override
    public Task findById(Integer id) {
        return taskMapper.selectByPrimaryKey(id);
    }

    /**
     * 获得待办事项列表
     * @param pageNo
     * @return
     */
    @Override
    public PageInfo<Task> pageForTask(Integer pageNo, Integer accountId,boolean showAll) {
        PageHelper.startPage(pageNo,15);
        List<Task> list = taskMapper.findByAccountId(accountId,showAll);
       /* TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andAccountIdEqualTo(account.getId());
        List<Task> list = taskMapper.selectByExample(taskExample);*/
        return new PageInfo<>(list);
    }

    /**
     * 获得待办事项列表(不分页)
     * @param accountId
     * @param showAll
     * @return
     */
    @Override
    public List<Task> findTaskList(Integer accountId, boolean showAll) {
        return taskMapper.findByAccountId(accountId,showAll);
    }

    /**
     * 根据客户id获得事项列表
     * @param id
     * @return
     */
    @Override
    public List<Task> findTaskByCustId(Integer id) {
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andCustIdEqualTo(id);
        taskExample.setOrderByClause("id desc");
        return taskMapper.selectByExample(taskExample);
    }

    /**
     * 根据saleId获得事项列表
     * @param id
     * @return
     */
    @Override
    public List<Task> findTaskBySaleId(Integer id) {
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andSaleIdEqualTo(id);
        taskExample.setOrderByClause("finish_time desc");
        return taskMapper.selectByExample(taskExample);
    }

    /**
     * 新增待办事项
     * @param task
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNewTask(Task task) {
        task.setDone((byte)0);
        task.setCreateTime(new Date());
        taskMapper.insert(task);
        logger.info("创建新的待办事项 {}" + task.getTitle());

        if(StringUtils.isNotEmpty(task.getRemindTime())) {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.putAsString("accountId",task.getAccountId());
            jobDataMap.put("message",task.getTitle());

            JobDetail jobDetail = JobBuilder
                    .newJob(SendMessageJob.class)
                    .setJobData(jobDataMap)
                    .withIdentity(new JobKey("taskID:"+task.getId(),"sendMessageGroup"))
                    .build();

            //2017-09-08 12:35 -> cron
            //String -> DateTime
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            DateTime dateTime = formatter.parseDateTime(task.getRemindTime());

            StringBuilder cron = new StringBuilder("0")
                    .append(" ")
                    .append(dateTime.getMinuteOfHour())
                    .append(" ")
                    .append(dateTime.getHourOfDay())
                    .append(" ")
                    .append(dateTime.getDayOfMonth())
                    .append(" ")
                    .append(dateTime.getMonthOfYear())
                    .append(" ? ")
                    .append(dateTime.getYear());

            logger.info("CRON EX: {}" ,cron.toString());

            ScheduleBuilder scheduleBuilder =
                    CronScheduleBuilder.cronSchedule(cron.toString()); //!!!! Cron表达式
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(scheduleBuilder)
                    .build();

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            try {
                scheduler.scheduleJob(jobDetail, trigger);
                scheduler.start();
            } catch (Exception ex) {
                throw new ServiceException(ex,"添加定时任务异常");
            }

        }
    }

    /**
     * 根据id删除待办事项
     */
    @Override
    public void delTaskById(Integer id) {
        Task task = findById(id);
        taskMapper.deleteByPrimaryKey(id);
        //删除定时任务
        if(StringUtils.isNotEmpty(task.getRemindTime())) {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            try {
                scheduler.deleteJob(new JobKey("taskID:" + id, "sendMessageGroup"));
                logger.info("成功删除定时任务 ID:{} groupName:{}" ,id,"sendMessageGroup");
            } catch (Exception ex) {
                throw new ServiceException(ex,"删除定时任务异常");
            }
        }
    }

    /**
     * 更新状态为1完成
     * @param id
     */
    @Override
    public void updateStateDone(Integer id) {
        Task task = taskMapper.selectByPrimaryKey(id);
        task.setDone((byte)1);
        taskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 更新状态为0未完成
     * @param id
     */
    @Override
    public void updateStateUndone(Integer id) {
        Task task = taskMapper.selectByPrimaryKey(id);
        task.setDone((byte)0);
        taskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 更改待办事项状态状态
     *
     * @param task
     */
    @Override
    public void updateTask(Task task) {
        taskMapper.updateByPrimaryKey(task);
    }


}
