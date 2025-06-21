package com.skmj.server.area;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

;

/**
 * @author lc
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    @Override
    public List<AreaTree> getAreaTree() {
        List<Area> list = list();
        //数据转成树结构
        return buildAreaTree(list);
    }

    public List<AreaTree> buildAreaTree(List<Area> areas) {
        // 1. 转换为 AreaTree 并建立 ID 映射
        Map<Long, AreaTree> areaMap = areas.stream()
                .collect(Collectors.toMap(
                        Area::getId,
                        this::convertToAreaTree
                ));

        // 2. 构建父子关系
        List<AreaTree> rootNodes = new ArrayList<>();

        for (Area area : areas) {
            AreaTree node = areaMap.get(area.getId());
            Long parentId = area.getParentId();

            if (parentId == null || parentId == 0) {
                rootNodes.add(node);
            } else {
                AreaTree parentNode = areaMap.get(parentId);
                if (parentNode != null) {
                    if (parentNode.getChildren() == null) {
                        parentNode.setChildren(new ArrayList<>());
                    }
                    parentNode.getChildren().add(node);
                }
            }
        }

        return rootNodes;
    }

    private AreaTree convertToAreaTree(Area area) {
        AreaTree tree = new AreaTree();
        tree.setId(area.getId());
        tree.setLabel(area.getName());
        tree.setValue(area.getCode());
        return tree;
    }

    @Override
    public boolean addArea(Area area) {
        return save(area);
    }

    @Override
    public boolean updateArea(Area area) {
        return updateById(area);
    }

    @Override
    public boolean deleteArea(Long id) {
        return removeById(id);
    }

    @Override
    public boolean deleteAreas(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Area getAreaById(Long id) {
        return getById(id);
    }

    @Override
    public List<Area> searchAreaByName(String name) {
        return lambdaQuery().like(Area::getName, name).list();
    }

    @Override
    public Page<Area> listAreaByPage(int pageNum, int pageSize) {
        Page<Area> page = new Page<>(pageNum, pageSize);
        return page(page);
    }
}