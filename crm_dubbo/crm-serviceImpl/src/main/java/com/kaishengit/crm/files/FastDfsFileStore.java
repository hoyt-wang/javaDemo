package com.kaishengit.crm.files;

import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by hoyt on 2017/11/18.
 */

@Component
public class FastDfsFileStore implements FileStore{

    @Value("${trackerServer}")
    private String trackerServer;
    /**
     * 保存文件
     * @param inputStream 文件输入流
     * @param saveName 文件保存名
     * @return 文件存放路径或名称
     * @throws IOException
     */
    @Override
    public String saveFile(InputStream inputStream, String saveName) throws IOException {
        String extName = "";
        if(saveName.indexOf(".") != -1) {
            extName = saveName.substring(saveName.lastIndexOf(".") + 1);
        }
        StorageClient storageClient = getStorageClient();
        try {
            String[] results = storageClient.upload_file(IOUtils.toByteArray(inputStream),extName,null);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(results[0])
                    .append("#")
                    .append(results[1]);
            return stringBuilder.toString();
        } catch (MyException e) {
            throw  new RuntimeException("存储到fastdfs异常",e);
        }
    }

    /**
     * 获取文件
     * @param fileName 文件名称或者路径
     * @return 文件字节数组
     * @throws IOException
     */
    @Override
    public byte[] getFile(String fileName) throws IOException {
        String[] array = fileName.split("#");
        String groupName = array[0];
        String filePath = array[1];
        StorageClient storageClient = getStorageClient();
        try {
            return storageClient.download_file(groupName,filePath);
        } catch (MyException e) {
            throw new RuntimeException("从FastDFS获取文件异常",e);
        }
    }

    /**
     * 获取storageClient
     * @return
     */
    private StorageClient getStorageClient() {
        Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,trackerServer);
        try {
            ClientGlobal.initByProperties(properties);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer,null);
            return storageClient;
        } catch (IOException | MyException e) {
            throw new RuntimeException("获取storageClient异常");
        }
    }
}
