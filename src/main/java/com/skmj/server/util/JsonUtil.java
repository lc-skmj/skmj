package com.skmj.server.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lc
 */
public class JsonUtil {

    public static <T> List<T> jsonArrayToBeanArray(String json, Class<T> clazz) {
        if (StrUtil.isBlank(json)) {
            return new ArrayList<>();
        }
        try {
            // 使用Hutool的JSONUtil将JSON字符串解析成JSONArray
            JSONArray jsonArray = JSONUtil.parseArray(json);

            // 使用Stream API进行转换
            return jsonArray.stream()
                    .map(JSONObject.class::cast)
                    .map(jsonObject -> JSONUtil.toBean(jsonObject, clazz))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // 处理可能的异常，例如解析错误或类型转换错误
            throw new RuntimeException("JSON转换异常", e);
        }

    }
}
