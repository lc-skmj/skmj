package com.skmj.server.xxljob.handler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 定时清理任务处理器
 */
@Component
public class CleanUpJobHandler {

    /**
     * 每天凌晨 1 点执行一次
     */
    @XxlJob("cleanUpJobHandler")
    public void cleanUpJobHandler() {
        XxlJobHelper.log("XXL-JOB: 开始执行定时清理任务...");

        // 清理过期的临时文件（假设临时文件存放在 ./temp 目录）
        File tempDir = new File("./temp");
        if (tempDir.exists() && tempDir.isDirectory()) {
            for (File file : tempDir.listFiles()) {
                if (file.lastModified() < System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
                    file.delete();
                    XxlJobHelper.log("已删除临时文件: {}", file.getName());
                }
            }
        }

        // 清理过期的验证码（假设验证码存储在内存中，这里可以替换为 Redis 或数据库清理）
        // 示例中简单打印日志，实际应调用清理逻辑
        XxlJobHelper.log("已清理过期验证码");

        XxlJobHelper.log("XXL-JOB: 定时清理任务执行完毕。");
    }
}