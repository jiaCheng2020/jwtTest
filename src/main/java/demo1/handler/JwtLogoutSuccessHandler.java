package demo1.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo1.pojo.AjaxResult;
import demo1.tools.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //走token验证的principal为username
        String username =(String)authentication.getPrincipal();
        redisTemplate.delete(username);
        HttpUtils.write(response,objectMapper.writeValueAsString(AjaxResult.success("登出成功",null)));
    }
}
