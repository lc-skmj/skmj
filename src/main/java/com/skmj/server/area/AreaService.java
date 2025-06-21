package com.skmj.server.area;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lc
 */
public interface AreaService extends IService<Area> {
    /**
     * 获取地区树
     * @return
     */
    List<AreaTree> getAreaTree();

    boolean addArea(Area area);

    boolean updateArea(Area area);

    boolean deleteArea(Long id);

    boolean deleteAreas(List<Long> ids);

    Area getAreaById(Long id);

    List<Area> searchAreaByName(String name);

    com.baomidou.mybatisplus.extension.plugins.pagination.Page<Area> listAreaByPage(int pageNum, int pageSize);
}