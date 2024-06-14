package com.gaoh.redissontask.controler;

import com.gaoh.redissontask.task.ExecuteQueueTaskService;
import com.gaoh.redissontask.task.QueueTask;
import com.gaoh.redissontask.task.QueueTaskService;
import com.gaoh.redissontask.vo.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author gaoh
 */
@RestController
public class TaskController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private QueueTaskService queueTaskService;


    @GetMapping("task")
    public RestResult<String> getTask() {
        stringRedisTemplate.opsForHash().put("redisson-task", "1", System.currentTimeMillis() + "");
        return RestResult.ok("redisson-task");
    }


    @GetMapping("addTask")
    public RestResult<String> addTask() {
        queueTaskService.addTask(new QueueTask(ExecuteQueueTaskService.TASK_01, "666", LocalDateTime.now().plusMinutes(2)));
        return RestResult.ok();
    }

    @GetMapping("removeTask")
    public RestResult<String> removeTask() {
        queueTaskService.removeTask(new QueueTask(ExecuteQueueTaskService.TASK_01, "666", LocalDateTime.now().plusMinutes(2)));
        return RestResult.ok();
    }

}
