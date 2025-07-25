package com.skmj.server;

import org.beetl.ext.spring6.EnableBeetl;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lc
 */
@EnableFileStorage
@EnableBeetl
@SpringBootApplication
public class SkmjApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkmjApplication.class, args);
    }

}
