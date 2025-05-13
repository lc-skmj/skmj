package com.skmj.server.area;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lc
 */
@Data
@TableName("area")
public class Area {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父id
     */
    private Long parentId;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 编码
     */
    private String code;

}