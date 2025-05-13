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
}