package demo1.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import demo1.pojo.AjaxResult;
import demo1.pojo.Type;
import demo1.tools.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问被拒绝的处理器
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

      HttpUtils.write(response,objectMapper.writeValueAsString(AjaxResult.failure(Type.ACCESS_DENIED)));
    }
}
