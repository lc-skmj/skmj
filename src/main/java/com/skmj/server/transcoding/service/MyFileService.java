package com.skmj.server.transcoding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skmj.server.transcoding.entity.MyFile;

import java.util.List;

/**
 * @author lc
 */
public interface MyFileService extends IService<MyFile> {
    List<MyFile> getTranscodingData();
}
