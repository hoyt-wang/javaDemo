package com.kaishengit.crm.service.impl;

import com.kaishengit.crm.entity.Disk;
import com.kaishengit.crm.example.DiskExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.files.FileStore;
import com.kaishengit.crm.mapper.DiskMapper;
import com.kaishengit.crm.service.DiskService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by hoyt on 2017/11/15.
 */

@Service
public class DiskServiceImpl implements DiskService {

    @Autowired
    private DiskMapper diskMapper;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Autowired
    @Qualifier("qiniuFileStore")
    private FileStore fileStore;

    /**
     * 根据Pid查找文件及文件夹
     * @param pId
     * @return
     */
    @Override
    public List<Disk> findDiskListByPId(Integer pId) {
        DiskExample example = new DiskExample();
        example.createCriteria().andPIdEqualTo(pId);
        return diskMapper.selectByExample(example);
    }

    /**
     * 根据主键查找
     * @param id
     * @return
     */
    @Override
    public Disk findDiskById(Integer id) {
        return diskMapper.selectByPrimaryKey(id);
    }

    /**
     * 新建文件夹
     * @param disk
     */
    @Override
    public void saveNewFolder(Disk disk) {
        disk.setUpdateTime(new Date());
        disk.setType(Disk.DISK_FOLDER_TYPE);
        diskMapper.insert(disk);
    }

    /**
     * 保存新的文件
     * @param disk
     * @param inputStream
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNewFile(Disk disk, InputStream inputStream) {
        String fileName = disk.getName();
        String saveName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));

        disk.setUpdateTime(new Date());
        disk.setDownloadCount(0);
        disk.setType(Disk.DISK_FILE_TYPE);

        String newFileName = null;
        try {
            newFileName = fileStore.saveFile(inputStream,saveName);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("文件上传异常");
        }
        disk.setSaveName(newFileName);
        diskMapper.insertSelective(disk);

    }

    /**
     * 根据id获得文件输入流
     * @param id
     * @return
     */
    @Override
    public InputStream donwnloadFile(Integer id) throws IOException,ServiceException {

        Disk disk = diskMapper.selectByPrimaryKey(id);
        if(disk == null || disk.getType().equals(Disk.DISK_FOLDER_TYPE)) {
            throw new ServiceException(id+"对应的文件不存在或已被删除");
        }

        //更新下载数量
        disk.setDownloadCount(disk.getDownloadCount() +1);
        diskMapper.updateByPrimaryKeySelective(disk);

        byte[] bytes = fileStore.getFile(disk.getSaveName());
        return new ByteArrayInputStream(bytes);
    }


}
