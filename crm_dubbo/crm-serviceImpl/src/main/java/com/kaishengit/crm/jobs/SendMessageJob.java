package com.kaishengit.crm.jobs;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * 定时发送提醒任务
 * Created by hoyt on 2017/11/14.
 */

public class SendMessageJob implements Job{

    private Logger logger = LoggerFactory.getLogger(SendMessageJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String message = (String) dataMap.get("message");
        Integer accountId = dataMap.getInt("accountId");

        logger.info("To:{} Message:{}",accountId,message);

        try {
            ApplicationContext applicationContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("springApplicationContext");
            JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");
            jmsTemplate.send("weixinMessage-queue", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    String json = "{\"id\":\"1\",\"message\":\"hello,jms123321\"}";
                    TextMessage textMessage = session.createTextMessage(json);
                    return textMessage;
                }
            });
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
