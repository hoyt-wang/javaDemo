package com.kaishengit.crm.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by hoyt on 2017/11/15.
 */

public class MyQuartzJob {

    public void doSomething() {
        System.out.println("Spring job running....");
    }

}
