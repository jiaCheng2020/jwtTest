package demo1.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo1.pojo.AjaxResult;
import demo1.pojo.Type;
import demo1.tools.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证失败的处理器
 */
@Component
public class JwtFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

       HttpUtils.write(response,objectMapper.writeValueAsString(AjaxResult.failure(Type.UNAUTHORISED)));
    }
}
