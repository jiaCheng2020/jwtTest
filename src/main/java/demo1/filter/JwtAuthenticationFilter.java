package demo1.filter;

import demo1.handler.JwtAuthenticationEntryPoint;
import demo1.tools.JwtUtils;
import demo1.tools.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


/**
 * jwt拦截器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public  static final String TOKEN_HEADER = "Authorization";
    public  static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;//验证异常委托给该处理类进行处理

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

       //首次通过用户名密码访问直接放行
       if (SecurityContextHolder.getContext().getAuthentication() != null){
           filterChain.doFilter(httpServletRequest,httpServletResponse);
       }

       //提取jwt
        String head = httpServletRequest.getHeader(TOKEN_HEADER);
        if (head != null  &&  !"".equals(head)){
            if (head.startsWith(TOKEN_PREFIX)) {
                String token = head.replace(TOKEN_PREFIX,"");
                if (!"".equals(token)){
                    try {
                        jwtAuthenticationHandle(token,httpServletRequest);
                    }catch (AuthenticationException e){
                        jwtAuthenticationEntryPoint.commence(httpServletRequest,httpServletResponse,e);
                    }
                }
            }else{
                //有Authorization头,没有token数据
                jwtAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse, new AuthenticationCredentialsNotFoundException("没有找到token"));
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);//最终过滤结束后放行
    }

    //具体进行token验证的处理方法
    private void jwtAuthenticationHandle(String token,HttpServletRequest httpServletRequest) throws AuthenticationException {
        if (!JwtUtils.isExpiration(token)){   //判断是否过期
            String username = JwtUtils.getSub(token);
            String cacheToken =(String)redisTemplate.opsForValue().get(username);
            if(StringUtils.equals(token,cacheToken)){ //与缓存中的token进行对比
                List<String> roles = JwtUtils.getRoles(token);
                Collection<GrantedAuthority> authorities = new HashSet<>();
                roles.forEach(
                    rol -> authorities.add(new SimpleGrantedAuthority(rol))
                );
                //创建验证过的authentication,将其放入安全上下文中
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                throw new BadCredentialsException("token不匹配");
            }
        }else{
                throw new CredentialsExpiredException("Token失效了");
        }
    }

}
