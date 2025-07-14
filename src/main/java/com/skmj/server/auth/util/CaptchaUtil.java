package com.skmj.server.auth.util;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.Properties;

@Component
public class CaptchaUtil {
    
    private Producer kaptchaProducer;

    public CaptchaUtil() {
        Properties properties = new Properties();
        properties.put("kaptcha.image.width", "150");
        properties.put("kaptcha.image.height", "50");
        properties.put("kaptcha.textproducer.char.string", "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        properties.put("kaptcha.textproducer.char.length", "4");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        this.kaptchaProducer = defaultKaptcha;
    }

    public String generateCaptchaText() {
        return kaptchaProducer.createText();
    }

    public BufferedImage generateCaptchaImage(String captchaText) {
        return kaptchaProducer.createImage(captchaText);
    }
}