package com.gaoh.redissontask.task;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gaoh
 */
@Slf4j
@Component
public class QueueTaskService {
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private Map<String, ExecuteQueueTaskService> executeQueueTaskMap;

    /**
     * 执行
     *
     * @param queueTask 队列任务
     */
    public void executeTask(QueueTask queueTask) {
        try {
            String type = queueTask.getType();
            if (StringUtils.isBlank(type)) {
                log.error("当前任务的ID不对");
                return;
            }

            String task = queueTask.getTask();
            if (StringUtils.isBlank(task)) {
                log.error("任务对象不存在");
                return;
            }

            ExecuteQueueTaskService executeQueueTask = executeQueueTaskMap.get(type);
            if (executeQueueTask == null) {
                log.error("任务:{}对应的执行器不存在", type);
                return;
            }

            //调用执行方法
            executeQueueTask.execute(task);
        } catch (Exception e) {
            log.error("执行队列任务发生异常", e);
        }
    }

    public void addTask(QueueTask queueTask) {
        log.info("添加任务到队列中...时间:{},参数:{}", queueTask.getStartTime(), queueTask.getTask());
        RBlockingQueue<QueueTask> blockingFairQueue = redissonClient.getBlockingQueue(queueTask.getType());
        RDelayedQueue<QueueTask> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);

        //计算延时时间
        long between = ChronoUnit.SECONDS.between(LocalDateTime.now(), queueTask.getStartTime());
        delayedQueue.offer(queueTask, between, TimeUnit.SECONDS);
    }

    public void removeTask(QueueTask queueTask) {
        RBlockingQueue<QueueTask> blockingFairQueue = redissonClient.getBlockingQueue(queueTask.getTask());
        RDelayedQueue<QueueTask> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.remove(queueTask);
    }
}
