package com.skmj.server.area;

import cn.dev33.satoken.annotation.SaIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lc
 */
@SaIgnore
@RestController
@RequestMapping("/system/area")
public class AreaController {
    @Autowired
    private AreaService areaService;
    @GetMapping("/tree")
    public List<AreaTree> getAreaTree() {
        return areaService.getAreaTree();
    }
}
