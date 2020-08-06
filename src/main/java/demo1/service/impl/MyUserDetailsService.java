package demo1.service.impl;


import demo1.dao.UserMapper;
import demo1.pojo.Role;
import demo1.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //获取用户对象
        SysUser sysUser = userMapper.findUserByName(username);

        if (sysUser != null){
            String password = sysUser.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            password = passwordEncoder.encode(password);//从数据库查的密码进行加密
            sysUser.setPwd(password);
            return  sysUser;
        }else{
            throw new UsernameNotFoundException("用户"+username+"不存在");
        }

    }

}
