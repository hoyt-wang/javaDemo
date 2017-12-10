package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Disk;
import com.kaishengit.crm.exception.ServiceException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by hoyt on 2017/11/15.
 */
public interface DiskService {

    /**
     * 根据Pid查找文件及文件夹
     * @param pId
     * @return
     */
    List<Disk> findDiskListByPId(Integer pId);

    /**
     * 根据主键查找
     * @param id
     * @return
     */
    Disk findDiskById(Integer id);

    /**
     * 新建文件夹
     * @param disk
     */
    void saveNewFolder(Disk disk);

    /**
     * 保存新的文件
     * @param disk
     * @param inputStream
     */
    void saveNewFile(Disk disk, InputStream inputStream);

    /**
     * 根据id获得文件输入流
     * @param id
     * @return
     */
    InputStream donwnloadFile(Integer id) throws IOException,ServiceException;
}
