package com.kaishengit.crm.files;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hoyt on 2017/11/18.
 */
public interface FileStore {

    /**
     * 保存文件
     * @param inputStream 文件输入流
     * @param fileName 文件名或者路径
     * @return 文件存放路径或名称
     * @throws IOException
     */
    String saveFile(InputStream inputStream,String fileName) throws IOException;

    /**
     * 获取文件
     * @return 文件字节数组
     * @param fileName 文件名称或者路径
     * @throws IOException
     */
    byte[] getFile(String fileName) throws IOException;
}
