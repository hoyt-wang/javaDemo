package com.kaishengit.crm.files;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by hoyt on 2017/11/18.
 */

@Component
public class LocalFileStore implements FileStore{

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 保存文件
     * @param inputStream 文件输入流
     * @param saveName    文件名
     * @return 文件存放路径或名称
     * @throws IOException
     */
    @Override
    public String saveFile(InputStream inputStream, String saveName) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(new File(uploadPath,saveName));
        IOUtils.copy(inputStream,outputStream);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return saveName;
    }

    /**
     * 获取文件
     * @param fileName 文件名称或者路径
     * @return 文件字节数组
     * @throws IOException
     */
    @Override
    public byte[] getFile(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(new File(uploadPath,fileName));
        return IOUtils.toByteArray(inputStream);
    }
}
