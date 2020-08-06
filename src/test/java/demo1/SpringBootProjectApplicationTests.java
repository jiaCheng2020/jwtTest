package demo1;

import demo1.pojo.SysUser;
import demo1.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootProjectApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void test() {
        SysUser user = userService.findUserByName("zhangsan");
        System.out.println(user);
    }

}
