package com.skmj.server.transcoding.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skmj.server.transcoding.entity.TranscodingFile;
import com.skmj.server.transcoding.mapper.TranscodingFileMapper;
import org.springframework.stereotype.Service;

/**
 * @author lc
 */
@Service
public class TranscodingFileServiceImpl extends ServiceImpl<TranscodingFileMapper, TranscodingFile> implements TranscodingFileService {
}
