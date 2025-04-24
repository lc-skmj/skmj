package com.skmj.server.transcoding;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.skmj.server.transcoding.entity.MyFile;
import com.skmj.server.transcoding.service.MyFileService;
import com.skmj.server.transcoding.util.TranscodingFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lc
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private MyFileService myFileService;
    @Autowired
    private TranscodingFileUtil transcodingFileUtil;
    @PostMapping("/transcoding")
    public Map<String, String> transcoding(@RequestBody Map<String, Object> req) {
        Map<String, String> res = new HashMap<>();
        Object object = req.get("ids");
        JSONArray jsonArray = JSONUtil.parseArray(object.toString());
        List<String> ids = new ArrayList<>();
        for (Object o : jsonArray) {
            ids.add(o.toString());
        }
        List<MyFile> myFiles = myFileService.listByIds(ids);
        if (!myFiles.isEmpty()){
            ThreadUtil.execAsync(()->{
                transcodingFileUtil.transcodingFile(myFiles);
            });
        }
        ThreadUtil.sleep(2, TimeUnit.SECONDS);
        res.put("code", "200");
        return res;
    }

}
