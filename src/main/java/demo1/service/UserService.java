package demo1.service;

import demo1.pojo.SysUser;

public interface UserService {

    /**
     * 根据用户名找user
     * @param username
     * @return
     */
    SysUser findUserByName(String username);

    /**
     * 根据用户名找token
     * @param username
     * @return
     */
    String findRefreshToken(String username);

    /**
     * 持久化refreshToken
     * @param username
     * @param refreshToken
     * @return
     */
    int updateRefreshToken(String username,String refreshToken);
}
