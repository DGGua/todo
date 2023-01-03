package todo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName:todolist
 * @Description:待办集类
 * @Author:liumengying
 * @Date: 2023/1/1 13:24
 * Version v1.0
 */
@Data
public class Todolist {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id; //待办集id

    private Integer todoID;  //所属待办集
    private String project;  //待办集名称
}
