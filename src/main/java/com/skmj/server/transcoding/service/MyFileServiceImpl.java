package com.skmj.server.transcoding.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skmj.server.transcoding.entity.MyFile;
import com.skmj.server.transcoding.mapper.MyFileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lc
 */
@Service
public class MyFileServiceImpl extends ServiceImpl<MyFileMapper, MyFile> implements MyFileService {
    @Override
    public List<MyFile> getTranscodingData() {
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(MyFile::getType,"VIDEO","WORD","PDF","PPT");
        queryWrapper.lambda().in(MyFile::getTranscodingStatus,0,3);
        Page<MyFile> page = new Page<>(1, 3);
        return page(page,queryWrapper).getRecords();
    }
}
