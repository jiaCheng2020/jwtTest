package demo1.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import demo1.pojo.AjaxResult;

import demo1.pojo.Type;
import demo1.service.UserService;
import demo1.tools.HttpUtils;
import demo1.tools.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功返回用于验证的以及刷新用的token
 */
@Component
@Slf4j
public class JwtSuccessHandler implements AuthenticationSuccessHandler  {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        AjaxResult ajaxResult = null;

        if (user != null){
            String username = user.getUsername();
            log.debug(username+"成功登陆");

            String accessToken = JwtUtils.createJwt(user,JwtUtils.EXPIRATION);
            Date expiredIn = new Date(System.currentTimeMillis()+JwtUtils.EXPIRATION);
            long expirationTime = JwtUtils.EXPIRATION;
            redisTemplate.opsForValue().set(username,accessToken);//验证用的token放入redis中
            String refreshToken = JwtUtils.createJwt(user,JwtUtils.VALIDATE_TIME);
            userService.updateRefreshToken(username,refreshToken);//刷新用的token进行持久化操作
            Map<String,Object> data = new HashMap<>();
            data.put("access-token",accessToken);
            data.put("refresh-token",refreshToken);
            data.put("expired_in",expiredIn);
            data.put("expiration",expirationTime);
            ajaxResult = AjaxResult.success(data);//将过期时间、凭证有效期等信息返回前端进行逻辑处理
        }else{
            ajaxResult = AjaxResult.failure(Type.UNAUTHORISED);
        }

        HttpUtils.write(response,objectMapper.writeValueAsString(ajaxResult));

    }
}
