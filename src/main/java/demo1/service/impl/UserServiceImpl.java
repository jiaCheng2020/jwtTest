package demo1.service.impl;

import demo1.dao.UserMapper;
import demo1.pojo.SysUser;
import demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public SysUser findUserByName(String username) {
        return userMapper.findUserByName(username);
    }

    @Override
    public String findRefreshToken(String username) {
        return userMapper.findRefreshTokenByName(username);
    }

    @Override
    public int updateRefreshToken(String username,String refreshToken) {
        return userMapper.updateRefreshToken(username,refreshToken);
    }
}
