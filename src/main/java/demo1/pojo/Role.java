package demo1.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//角色表
@Data
@NoArgsConstructor
public class Role implements Serializable {
    private int id;
    private String roleName;
    private String desc;

}
