package demo1.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo1.pojo.AjaxResult;
import demo1.pojo.Type;
import demo1.tools.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证异常返回提示信息
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper;

    //处理验证异常信息
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Map<String,String> data = new HashMap<>();
        String uri = request.getRequestURI();
        data.put("uri",uri);
        HttpUtils.write(response,objectMapper.writeValueAsString(AjaxResult.failure(Type.UNAUTHORISED,authException.getMessage(),data)));
    }
}
