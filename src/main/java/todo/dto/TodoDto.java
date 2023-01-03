package todo.dto;

import lombok.Data;
import todo.entity.Todolist;
import todo.entity.Todos;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:todoDto
 * @Description:todo扩展类
 * @Author:liumengying
 * @Date: 2023/1/1 13:24
 * Version v1.0
 */
@Data
public class TodoDto extends Todos {

    private List<String> subs=new ArrayList<>();   //待办集名称

}
