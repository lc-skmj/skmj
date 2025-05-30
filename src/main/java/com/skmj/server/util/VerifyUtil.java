package com.skmj.server.util;

import org.jeecg.common.exception.JeecgBootException;

/**
 * @author lc
 */
public class VerifyUtil {

    public static void verifyNotNull(Object obj,String msg){
        if(obj == null){
          throw new JeecgBootException(msg);
        }
    }
}
