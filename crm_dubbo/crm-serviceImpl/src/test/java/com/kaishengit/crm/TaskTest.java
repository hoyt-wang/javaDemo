package com.kaishengit.crm;

import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by hoyt on 2017/11/21.
 */

public class TaskTest {
    @Autowired
    private TaskService taskService;

    @Test
    public void pageFofTask() {
        List<Task> taskList = taskService.findTaskList(8,false);

        for (Task task : taskList) {
            System.out.println(task.getTitle());
        }
    }
}
