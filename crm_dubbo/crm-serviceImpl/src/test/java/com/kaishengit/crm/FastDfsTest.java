package com.kaishengit.crm;

import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;

import javax.naming.spi.NamingManager;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by hoyt on 2017/11/16.
 */

public class FastDfsTest {

    @Test
    public void uploadFile() throws IOException, MyException {

        Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,"192.168.106.28:22122,192.168.106.26:22122");
        ClientGlobal.initByProperties(properties);

        TrackerClient client = new TrackerClient();
        TrackerServer trackerServer = client.getConnection();

        StorageClient storageClient = new StorageClient(trackerServer,null);

        NameValuePair[] nameValuePair = new NameValuePair[2];
        nameValuePair[0] = new NameValuePair("width","200");
        nameValuePair[1] = new NameValuePair("height","200");

        InputStream inputStream = new FileInputStream("d:/upload/1.jpg");

        byte[] bytes = IOUtils.toByteArray(inputStream);

        String[] results = storageClient.upload_file(bytes,"jpg",nameValuePair);
        for(String result : results) {
            System.out.println(result);
        }

        inputStream.close();

    /*    Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,"192.168.106.28:22122");
        ClientGlobal.initByProperties(properties);

        TrackerClient client = new TrackerClient();
        TrackerServer trackerServer = client.getConnection();

        StorageClient storageClient = new StorageClient(trackerServer,null);
        InputStream inputStream = new FileInputStream("d:/temp/1.txt");

        byte[] bytes = IOUtils.toByteArray(inputStream);

        String[] results = storageClient.upload_file(bytes,"txt",null);
        for (String str : results) {
            System.out.println(str);
        }

        inputStream.close();;*/
    }

    @Test
    public void downloadFile() throws IOException, MyException {

        Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,"192.168.106.28:22122,192.168.106.26:22122");
        ClientGlobal.initByProperties(properties);

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageClient storageClient = new StorageClient(trackerServer,null);

        byte[] bytes = storageClient.download_file("group2","M00/00/00/wKhqLloO4heATfZ4AADTn19dfUo742.jpg");
        FileOutputStream outputStream = new FileOutputStream("d:/temp/new.jpg");

        //获得metadata数组
        NameValuePair[] nameValuePairs = storageClient.get_metadata("group2","M00/00/00/wKhqLloO4heATfZ4AADTn19dfUo742.jpg");
        for(NameValuePair nameValuePair : nameValuePairs) {
            System.out.println(nameValuePair.getName() +"--->"+ nameValuePair.getValue());
        }

        //FileInfo
        FileInfo fileInfo = storageClient.get_file_info("group2","M00/00/00/wKhqLloO4heATfZ4AADTn19dfUo742.jpg");
        System.out.println("fileSize: " + fileInfo.getFileSize());
        System.out.println("crc32: " + fileInfo.getCrc32());
        System.out.println("createTime: " + fileInfo.getCreateTimestamp());
        System.out.println("IP: " + fileInfo.getSourceIpAddr());

        outputStream.write(bytes,0,bytes.length);
        outputStream.flush();
        outputStream.close();




       /* Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,"192.168.106.28:22122");
        ClientGlobal.initByProperties(properties);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer,null);
        byte[] bytes =storageClient.download_file("group1","M00/00/00/wKhqHFoNkguAK7o4AAAAEC4QH5s045.txt");
        FileOutputStream outputStream = new FileOutputStream("d:/temp/6.txt");
        outputStream.write(bytes,0,bytes.length);
        outputStream.flush();
        outputStream.close();*/
    }
}
