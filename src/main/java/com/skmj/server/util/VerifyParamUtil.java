package com.skmj.server.util;

import cn.hutool.core.util.StrUtil;

/**
 * 参数校验工具类
 * @author lc
 */
public class VerifyParamUtil {
    /**
     * 校验参数不为空
     * @param param 校验的参数
     * @param msg 提示的错误信息
     * @param stringBuilder 返回错误信息
     */
    public static void verifyParamNotNull(String param,String msg,StringBuilder stringBuilder){
        if (StrUtil.isBlank(param)){
            stringBuilder.append(msg);
        }
    }
    /**
     * 校验参数长度
     * @param param 校验的参数
     * @param msg 提示的错误信息
     * @param length 参数的最大长度
     * @param stringBuilder 返回错误信息
     */
    public static void verifyParamLength(String param,String msg,int length,StringBuilder stringBuilder){
        if (StrUtil.isNotBlank(param)&&param.length()>length ){
            stringBuilder.append(msg);
        }
    }

    /**
     * 根据参数是否正确返回信息
     * @param param 校验的参数
     * @param msg 提示的错误信息
     * @param stringBuilder 返回错误信息
     */
    public static void verifyParamTrueOrFalse(boolean param,String msg,StringBuilder stringBuilder){
        if (param){
            stringBuilder.append(msg);
        }
    }
}
