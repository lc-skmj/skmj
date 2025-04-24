package com.skmj.server.transcoding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lc
 */
@TableName(value = "ele_train_subject")
@Data
public class MyFile {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 类型 VIDEO WORD PDF
     */
    private String type;


    /**
     * 文件类型
     */
    private String extension;


    /**
     * 相对地址
     */
    private String videos;

    /**
     * 转码状态 0等待转码 1已转码 2转码中 3 转码失败
     */
    private Integer transcodingStatus;



}
