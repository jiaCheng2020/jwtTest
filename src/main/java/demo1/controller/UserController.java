package demo1.controller;

import demo1.pojo.AjaxResult;
import demo1.pojo.SysUser;
import demo1.pojo.Type;
import demo1.service.UserService;
import demo1.tools.JwtUtils;
import demo1.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/token")
    public AjaxResult refreshToken(String refreshToken){
        if (JwtUtils.isExpiration(refreshToken)){
            return AjaxResult.success("凭证失效,需要重新登录",null);
        }
        String username =(String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();//token验证的principal为username
        String token = userService.findRefreshToken(username);

        if (StringUtils.equals(token,refreshToken)){
            //刷新token
            SysUser user = userService.findUserByName(username);
            String accessToken = JwtUtils.createJwt(user, JwtUtils.EXPIRATION);
            redisTemplate.opsForValue().set(username,accessToken);
            Map<String,Object> data = new HashMap<>();
            data.put("access-token",accessToken);
            return AjaxResult.success(data);
        }

        return AjaxResult.failure(Type.UNAUTHORISED,"token有毛病");

    }
}
