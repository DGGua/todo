package todo.entity;

import lombok.Data;

/**
 * @ClassName:user
 * @Description:用户实体类
 * @Author:liumengying
 * @Date: 2023/1/1 13:11
 * Version v1.0
 */
@Data
public class User {
    private String userID;
    private String name;
    private String password;
    private String phone;
    private String description;
}
