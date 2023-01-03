package todo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import todo.entity.Todolist;
import todo.mapper.TodoListMapper;
import todo.service.TodoListService;
import todo.service.TodosService;

/**
 * @ClassName:TodoListImpl
 * @Description:待办集
 * @Author:liumengying
 * @Date: 2023/1/1 16:08
 * Version v1.0
 */
@Service
public class TodoListImpl extends ServiceImpl<TodoListMapper, Todolist> implements TodoListService {
}
