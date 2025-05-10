package com.skmj.server.beetl;

import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring6.BeetlTemplateCustomize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeetlConfig {
    @Bean
    public BeetlTemplateCustomize beetlTemplateCustomize(){
        return new BeetlTemplateCustomize(){
            @Override
            public void customize(GroupTemplate groupTemplate){
                groupTemplate.setResourceLoader(new ClasspathResourceLoader("/templates", "UTF-8"));
            }
        };
    }
}
