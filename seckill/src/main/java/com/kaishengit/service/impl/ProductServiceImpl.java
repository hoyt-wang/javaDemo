package com.kaishengit.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kaishengit.entity.Product;
import com.kaishengit.entity.ProductExample;
import com.kaishengit.files.FileStore;
import com.kaishengit.job.ProductInventoryJob;
import com.kaishengit.mapper.ProductMapper;
import com.kaishengit.service.ProductService;
import com.kaishengit.service.exception.ServiceException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * Created by hoyt on 2017/12/5.
 */

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    //@Qualifier("QiniuFileStore")
    private FileStore fileStore;

    @Autowired
    private ProductMapper productMapper;

    @Value("${qiniu.ak}")
    private String qiniuAccessKey;

    @Value("${qiniu.sk}")
    private String qiniuSecretKey;

    @Value("${qiniu.bucketName}")
    private String bucketName;

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 添加商品
     * @param product
     * @param inputStream
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveProduct(Product product,  InputStream inputStream) {

      /*  String fileName = product.getProductImage();

        String saveName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        String newFileName = null ;*/
       /* try {
            //newFileName = fileStore.saveFile(inputStream,saveName);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //product.setProductImage(newFileName);
        String key = uploadToQiNiu(inputStream);
        product.setProductImage(key);
        productMapper.insertSelective(product);

        try(Jedis jedis = jedisPool.getResource()) {
            for (int i = 0; i < product.getProductInventory(); i++) {
                jedis.lpush("product:" + product.getId() + ":inventory", String.valueOf(i));
            }
        }
        //添加秒杀结束的定时任务，用于秒杀结束时更新库存
        System.out.println(product.getEndTime());

        addSchedulerJob(product.getEndTime().getTime(),product.getId());
       ;

    }

    /**
     * 添加一个定时任务
     * @param endTime
     * @param productId
     */
    private void addSchedulerJob(long endTime, Integer productId) {

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAsString("productId",productId);

        JobDetail jobDetail = JobBuilder
                .newJob(ProductInventoryJob.class)
                .setJobData(jobDataMap)
                .withIdentity(new JobKey("taskID:"+productId,"productInventoryGroup"))
                .build();

        DateTime dateTime = new DateTime(endTime);

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
        System.out.println(cron.toString());

        ScheduleBuilder scheduleBuilder =
                CronScheduleBuilder.cronSchedule(cron.toString());
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

    /**
     * 查找所有商品列表
     *
     * @return
     */
    @Override
    public List<Product> findAll() {
        ProductExample productExample = new ProductExample();
        productExample.setOrderByClause("start_time asc");
        return productMapper.selectByExample(productExample);
    }

    /**
     * 根据id查找商品
     *@param id
     * @return
     */
    @Override
    public Product findById(Integer id) {
        Product product;
        try(Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get("product:"+id);
            if(json == null) {
                product = productMapper.selectByPrimaryKey(id);
                jedis.set("product:"+id, JSON.toJSONString(product));
            } else {
                product = JSON.parseObject(json,Product.class);
            }
        }
        return product;
    }

    /**
     * 秒杀商品
     * @param id
     */
    @Override
    public void seckill(Integer id) throws ServiceException{

        try(Jedis jedis = jedisPool.getResource()) {
            Product product = JSON.parseObject(jedis.get("product:"+id),Product.class);
            if(!product.isStart()) {
                throw new RuntimeException("你来早了，还没开始");
            }
            String value = jedis.lpop("product:"+id+":inventory");

            if(value == null) {
                logger.error("库存不足，秒杀失败");
                throw new ServiceException("抢光了");
            } else {
                logger.info("秒杀商品成功");

                //修改redis的缓存

                product.setProductInventory(product.getProductInventory() - 1);
                jedis.set("product:"+id,JSON.toJSONString(product));

                /*jmsTemplate.send("product_inventory_queue", new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage textMessage = session.createTextMessage();
                        textMessage.setText(id.toString());
                        return textMessage;
                    }
                });*/
            }
        }


        /*Jedis jedis = jedisPool.getResource();
        Product product = JSON.parseObject(jedis.get("product:" + id),Product.class);
        if(!product.isStart()) {
            throw new RuntimeException("您来早了！");
        }
        String value = jedis.lpop("product:" + id + ":inventory");
        if (value == null) {
            logger.info("商品抢光了！");
            throw new ServiceException("商品抢光了！");
        } else {
            logger.info("商品秒杀成功");
            //修改redis的缓存
            product.setProductInventory(product.getProductInventory() - 1);
            jedis.set("product:" + id ,JSON.toJSONString(product));
        }*/
    }



    /**
     * @param inputStream
     * @return 文件存放路径或者名称
     * @throws RuntimeException
     */
    private String uploadToQiNiu(InputStream inputStream) throws RuntimeException {
        Configuration configuration = new Configuration(Zone.zone1());
        UploadManager uploadManager = new UploadManager(configuration);

        Auth auth = Auth.create(qiniuAccessKey,qiniuSecretKey);
        String uploadToken = auth.uploadToken(bucketName);

        try {
            Response response = uploadManager.put(IOUtils.toByteArray(inputStream), null, uploadToken);
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
            return defaultPutRet.key;
        }catch (IOException ex) {
            throw new RuntimeException("上传文件到七牛异常",ex);
        }
    }
}
