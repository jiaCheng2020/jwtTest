package demo1.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo1.pojo.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<SysUser> {

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    SysUser findUserByName(String username);

    /**
     * 持久化refreshToken
     * @param jwt
     * @return
     */
    int updateRefreshToken(String username,String jwt);

    /**
     * 通过用户名查询refreshToken
     * @param username
     * @return
     */
    String findRefreshTokenByName(String username);
}
