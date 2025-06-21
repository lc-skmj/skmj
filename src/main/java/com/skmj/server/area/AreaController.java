package com.skmj.server.area;

import cn.dev33.satoken.annotation.SaIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public boolean addArea(@RequestBody Area area) {
        return areaService.addArea(area);
    }

    @PutMapping("/update")
    public boolean updateArea(@RequestBody Area area) {
        return areaService.updateArea(area);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteArea(@PathVariable Long id) {
        return areaService.deleteArea(id);
    }

    @PostMapping("/delete/batch")
    public boolean deleteAreas(@RequestBody List<Long> ids) {
        return areaService.deleteAreas(ids);
    }

    @GetMapping("/get/{id}")
    public Area getAreaById(@PathVariable Long id) {
        return areaService.getAreaById(id);
    }

    @GetMapping("/search")
    public List<Area> searchAreaByName(@RequestParam String name) {
        return areaService.searchAreaByName(name);
    }

    @GetMapping("/list")
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<Area> listAreaByPage(@RequestParam int pageNum, @RequestParam int pageSize) {
        return areaService.listAreaByPage(pageNum, pageSize);
    }
}
