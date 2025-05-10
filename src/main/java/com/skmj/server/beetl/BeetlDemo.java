package com.skmj.server.beetl;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BeetlDemo {
    @Autowired
    private GroupTemplate groupTemplate;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        Template template = groupTemplate.getTemplate("hello.btl");
        template.binding("name", "Beetl");
        return template.render();
    }
}
