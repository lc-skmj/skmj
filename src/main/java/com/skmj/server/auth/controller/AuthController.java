package com.skmj.server.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.skmj.server.auth.service.SysUserService;
import com.skmj.server.auth.service.UserRegistrationService;
import com.skmj.server.auth.vo.LoginReq;
import com.skmj.server.auth.vo.RegisterReq;
import com.skmj.server.auth.util.CaptchaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String CAPTCHA_PREFIX = "captcha:";
    private static final int CAPTCHA_EXPIRE_MINUTES = 5;
    
    @Autowired
    SysUserService sysUserService;
    
    @Autowired
    private CaptchaUtil captchaUtil;
    
    @Autowired
    private UserRegistrationService userRegistrationService;
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 登录
     *
     * @return 登录成功返回 Token，失败返回错误信息
     */
    @RequestMapping("login")
    public SaResult login(@RequestBody LoginReq loginReq) {
        // 校验验证码
        if (!validateCaptcha(loginReq.getCaptchaId(), loginReq.getCaptcha())) {
            logger.warn("验证码校验失败，captchaId: {}, inputCaptcha: {}", loginReq.getCaptchaId(), loginReq.getCaptcha());
            return SaResult.error("验证码错误");
        }

        try {
            String token = sysUserService.login(loginReq);
            logger.info("用户登录成功，username: {}", loginReq.getUsername());
            return SaResult.ok("登录成功").setData(token);
        } catch (RuntimeException e) {
            logger.error("用户登录失败，username: {}, error: {}", loginReq.getUsername(), e.getMessage());
            return SaResult.error(e.getMessage());
        }
    }

    /**
     * 注册
     */
    @RequestMapping("register")
    public SaResult register(@RequestBody RegisterReq registerReq) {
        userRegistrationService.register(registerReq);
        return SaResult.ok("注册成功");
    }

    /**
     * 生成验证码
     */
    @RequestMapping("generateCaptcha")
    public SaResult generateCaptcha() {
        try {
            // 生成唯一的验证码ID
            String captchaId = UUID.randomUUID().toString();
            
            // 生成验证码文本
            String captchaText = captchaUtil.generateCaptchaText();
            
            // 将验证码存储到Redis，设置过期时间
            String redisKey = CAPTCHA_PREFIX + captchaId;
            redisTemplate.opsForValue().set(redisKey, captchaText, CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
            
            // 生成验证码图片
            BufferedImage image = captchaUtil.generateCaptchaImage(captchaText);
            
            // 将图片转换为Base64编码
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
            
            // 返回验证码ID和图片
            return SaResult.ok().setData(new CaptchaResponse(captchaId, "data:image/png;base64," + base64Image));
            
        } catch (Exception e) {
            logger.error("生成验证码失败", e);
            return SaResult.error("生成验证码失败");
        }
    }
    
    /**
     * 验证码响应对象
     */
    public static class CaptchaResponse {
        private String captchaId;
        private String image;
        
        public CaptchaResponse(String captchaId, String image) {
            this.captchaId = captchaId;
            this.image = image;
        }
        
        public String getCaptchaId() {
            return captchaId;
        }
        
        public void setCaptchaId(String captchaId) {
            this.captchaId = captchaId;
        }
        
        public String getImage() {
            return image;
        }
        
        public void setImage(String image) {
            this.image = image;
        }
    }

    /**
     * 验证码校验
     *
     * @param captchaId 验证码ID
     * @param userInputCaptcha 用户输入的验证码
     * @return 是否校验通过
     */
    private boolean validateCaptcha(String captchaId, String userInputCaptcha) {
        if (captchaId == null || userInputCaptcha == null) {
            return false;
        }
        
        try {
            String redisKey = CAPTCHA_PREFIX + captchaId;
            String storedCaptcha = redisTemplate.opsForValue().get(redisKey);
            
            if (storedCaptcha == null) {
                logger.warn("验证码已过期或不存在，captchaId: {}", captchaId);
                return false;
            }
            
            // 验证码使用后立即删除，防止重复使用
            redisTemplate.delete(redisKey);
            
            boolean isValid = userInputCaptcha.equalsIgnoreCase(storedCaptcha);
            if (!isValid) {
                logger.warn("验证码不匹配，expected: {}, actual: {}", storedCaptcha, userInputCaptcha);
            }
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("验证码校验异常，captchaId: {}", captchaId, e);
            return false;
        }
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

}
