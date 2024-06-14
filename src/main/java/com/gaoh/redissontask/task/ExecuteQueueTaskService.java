package com.gaoh.redissontask.task;

/**
 * @author gaoh
 */
@FunctionalInterface
public interface ExecuteQueueTaskService {
    String TASK_01 = "redisson-task01";

    /**
     * 存放业务
     *
     * @param t 参数
     */
    void execute(String t);

}
