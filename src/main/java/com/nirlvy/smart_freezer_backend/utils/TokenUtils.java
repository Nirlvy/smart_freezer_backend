package com.nirlvy.smart_freezer_backend.utils;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nirlvy.smart_freezer_backend.entity.User;
import com.nirlvy.smart_freezer_backend.service.IUserService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenUtils {

    private static IUserService staticUserService;

    @Resource
    private IUserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    /**
     * 生成token
     * 
     * @param userId
     * @param sign
     * @return token
     */
    public static String genToken(String userId, String sign) {
        return JWT.create()
                .withAudience(userId)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 获取当前登录用户信息（暂时没用）
     * 
     * @return user对象
     */
    public static User getCurrentUser() {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                String token = request.getHeader("token");
                if (StrUtil.isNotBlank(token)) {
                    String userId = JWT.decode(token).getAudience().get(0);
                    return staticUserService.getById(Integer.valueOf(userId));
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
