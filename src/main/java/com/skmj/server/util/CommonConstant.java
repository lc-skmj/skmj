// 新建文件并添加以下内容
package com.skmj.server.util;

/**
 * 常用常量统一管理接口
 */
public interface CommonConstant {
    // Internet Explorer 版本标识
    String IE11 = "rv:11.0";
    String IE10 = "MSIE 10.0";
    String IE9 = "MSIE 9.0";
    String IE8 = "MSIE 8.0";
    String IE7 = "MSIE 7.0";
    String IE6 = "MSIE 6.0";
    
    String MAXTHON = "Maxthon";
    String QQ_BROWSER = "QQBrowser";
    String GREEN_BROWSER = "GreenBrowser";
    String BROWSER_360SE = "360SE";
    
    String FIREFOX = "Firefox";
    String OPERA = "Opera";
    String CHROME = "Chrome";
    String SAFARI = "Safari";
    String CAMINO = "Camino";
    
    String UNKNOWN = "unknown";
    String OTHER = "其它";
    
    // 浏览器语言相关常量
    String ZH = "zh";
    String ZH_CN = "zh-cn";
    String EN = "en";
    String EN_US = "en-US";
    
    // 用户请求相关的Header字段名称
    String USER_AGENT_HEADER = "USER-AGENT";
    
    // 移动设备关键字正则表达式
    String MOBILE_DEVICE_REGEX = "(phone|pad|pod|iphone|ipod|ios|ipad|android|mobile|blackberry|iemobile|mqqbrowser|juc|fennec|wosbrowser|browserng|webos|symbian|windows phone)";
}