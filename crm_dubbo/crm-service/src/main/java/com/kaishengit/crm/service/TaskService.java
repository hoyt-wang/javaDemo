package com.kaishengit.crm.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Task;
import sun.security.timestamp.TSRequest;

import java.util.List;

/**
 * Created by hoyt on 2017/11/14.
 */
public interface TaskService {

    /**
     * 根据id查找对应的待办事项
     * @param id
     * @return
     */
    Task findById(Integer id);

    /**
     * 获得待办事项列表
     * @param pageNo
     * @return
     */
    PageInfo<Task> pageForTask(Integer pageNo, Integer accountId,boolean showAll);

    /**
     * 获得待办事项列表(不分页)
     * @param accountId
     * @param showAll
     * @return
     */
    List<Task>  findTaskList(Integer accountId,boolean showAll);

    /**
     * 根据客户id获得事项列表
     * @param id
     * @return
     */
    List<Task> findTaskByCustId(Integer id);

    /**
     * 根据saleId获得事项列表
     * @param id
     * @return
     */
    List<Task> findTaskBySaleId(Integer id);

    /**
     * 新增待办事项
     * @param task
     */
    void saveNewTask(Task task);


    /**
     * 根据id删除待办事项
     */
    void delTaskById(Integer id);

    /**
     * 更新状态为1完成
     * @param id
     */
    void updateStateDone(Integer id);

    /**
     * 更新状态为0未完成
     * @param id
     */
    void updateStateUndone(Integer id);

    /**
     * 更改待办事项状态状态
     * @param task
     */
    void updateTask(Task task);
}
