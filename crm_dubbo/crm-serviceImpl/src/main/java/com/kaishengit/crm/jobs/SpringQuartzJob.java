package com.kaishengit.crm.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by hoyt on 2017/11/14.
 */

public class SpringQuartzJob implements Job{

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("+++++++++++++++++++++");
        //取值
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Integer accountId = dataMap.getInt("accountId");//dataMap.getIntegerFromString("accountId");
        System.out.println("Quartz Running....." + accountId);
    }


}
