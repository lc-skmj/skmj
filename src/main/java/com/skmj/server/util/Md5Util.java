package com.skmj.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @Description: 加密工具
 * @author: jeecg-boot
 */
public class Md5Util {
    
    private static final Logger logger = LoggerFactory.getLogger(Md5Util.class);

    private static final String[] HEXDIGITS = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++){
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HEXDIGITS[d1] + HEXDIGITS[d2];
	}

	public static String md5Encode(String origin, String charsetname) {
		if (origin == null) {
			logger.warn("MD5加密输入参数origin为null");
			return null;
		}
		
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname)) {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
			}
		} catch (Exception exception) {
			logger.error("MD5加密失败，origin: {}, charsetname: {}", origin, charsetname, exception);
			// 加密失败时返回null，调用方需要处理
			return null;
		}
		return resultString;
	}

}
