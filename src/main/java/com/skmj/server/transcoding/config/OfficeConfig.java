package com.skmj.server.transcoding.config;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.InstalledOfficeManagerHolder;
import org.jodconverter.core.office.OfficeUtils;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * @author lc
 */
@Slf4j
@Component
public class OfficeConfig {
    @Value("${officeUrl}")
    private String officeUrl;

    private LocalOfficeManager localOfficeManager;

    @PostConstruct
    public LocalOfficeManager init() {
        localOfficeManager = LocalOfficeManager.builder().officeHome(officeUrl).portNumbers(3000)
                .processTimeout(3000L).maxTasksPerProcess(1).install().build();
        try {
            localOfficeManager.start();
            InstalledOfficeManagerHolder.setInstance(localOfficeManager);
            log.info("office进程启动成功");
        } catch (Exception e) {
            log.error("office进程启动失败", e);
        }
        return localOfficeManager;
    }

    @Bean
    public DocumentConverter documentConverter() {
        log.info("创建DocumentConverter实例");
        return LocalConverter.builder().officeManager(localOfficeManager).build();
    }

    @PreDestroy
    public void destroyOfficeManager() {
        if (null != localOfficeManager && localOfficeManager.isRunning()) {
            log.info("终止localOfficeManager进程");
            OfficeUtils.stopQuietly(localOfficeManager);
        }
    }
}
