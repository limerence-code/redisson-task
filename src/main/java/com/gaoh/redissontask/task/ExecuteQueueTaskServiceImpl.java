package com.gaoh.redissontask.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 15871
 */
@Component(ExecuteQueueTaskService.TASK_01)
@Slf4j
public class ExecuteQueueTaskServiceImpl implements ExecuteQueueTaskService {
    @Override
    public void execute(String params) {
        log.info("任务执行...{}", params);
    }
}
