package com.kaishengit.crm.controller;

import com.google.gson.Gson;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Disk;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.DiskMapper;
import com.kaishengit.crm.service.DiskService;
import com.kaishengit.web.result.AjaxResult;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by hoyt on 2017/11/15.
 */

@Controller
@RequestMapping("/disk")
public class DiskController {

    @Autowired
    private DiskService diskService;

    @Value("${qiniu.ak}")
    private String qiniuAccessKey;

    @Value("${qiniu.sk}")
    private String qiniuSecretKey;

    @Value("${qiniu.bucketName}")
    private String bucketName;

    @Autowired
    private DiskMapper diskMapper;

    @GetMapping("/list")
    public String list(@RequestParam(required = false, defaultValue = "0",name = "_")Integer pId,
                       Model model) {
        List<Disk> diskList= diskService.findDiskListByPId(pId);
        if(0 != pId) {
            Disk disk = diskService.findDiskById(pId);
            model.addAttribute("disk",disk);
        }

        model.addAttribute("diskList",diskList);
        return "disk/list";
    }

    @PostMapping("/new/folder")
    @ResponseBody
    public AjaxResult saveNewFolder(Disk disk) {
        diskService.saveNewFolder(disk);
        List<Disk> diskList = diskService.findDiskListByPId(disk.getpId());
        return AjaxResult.successWithData(diskList);
    }

    /**
     * 文件上传
     * @param file
     * @param pId
     * @param accountId
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult updateFile(@RequestParam MultipartFile file, Integer pId, Integer accountId) throws IOException {

        if(file.isEmpty()) {
            return AjaxResult.error("文件不可为空");
        }
        //获取文件的原始名称
        String fileName = file.getOriginalFilename();
        //文件的大小
        long size = file.getSize();
        //文件的输入流
        InputStream inputStream = file.getInputStream();

        Disk disk = new Disk();
        disk.setpId(pId);
        disk.setAccountId(accountId);
        disk.setFileSize(FileUtils.byteCountToDisplaySize(size)); //10000000 -> 1KB
        disk.setName(fileName);

        String saveName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));

        disk.setUpdateTime(new Date());
        disk.setDownloadCount(0);
        disk.setType(Disk.DISK_FILE_TYPE);

        String newFileName = null;
        try {
           // newFileName = fileStore.saveFile(inputStream,saveName);
            //构造一个带指定Zone对象的配置类
            Configuration configuration = new Configuration(Zone.zone1());
            UploadManager uploadManager = new UploadManager(configuration);

            String qiniuAccessKey = "ihGMvJPxa1pueRhgomF3yNLni79nhI4YHxeGttmr";
            String qiniuSecretKey = "lLeD0G7dGQ6gg8bMxsuSZpWoRKctZkju4pt2DBjD";
            String bucketName = "qiniu-cloud";

            //...生成上传凭证，然后准备上传
            Auth auth = Auth.create(qiniuAccessKey,qiniuSecretKey);
            String uploadToken = auth.uploadToken(bucketName);

            byte[] bytes = IOUtils.toByteArray(inputStream);
            Response response = uploadManager.put(bytes,null,uploadToken);
            //解析上传成功的结果
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
            newFileName = defaultPutRet.key;

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("文件上传异常");
        }

        disk.setSaveName(newFileName);
        diskMapper.insertSelective(disk);

        //获得当前最新的disk集合
        List<Disk> diskList = diskService.findDiskListByPId(disk.getpId());
        return AjaxResult.successWithData(diskList);
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam(name = "_") Integer id,
                             @RequestParam(required = false,defaultValue = "") String fileName,
                             HttpServletResponse response) {
        try {
            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = diskService.donwnloadFile(id);
            //判断是下载还是预览
            if(StringUtils.isNotEmpty(fileName)) {
                response.setContentType("application/octet-stream");
                fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
                response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
            }
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (ServiceException|IOException e) {
            e.printStackTrace();
            throw new NotFoundException();
        }
    }

}
