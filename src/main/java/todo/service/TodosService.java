package todo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import todo.dto.TodoDto;
import todo.entity.Todos;

public interface TodosService extends IService<Todos> {
    void saveWithTodoList(TodoDto todoDto,String userID);

    void deleteWithTodoList(int id,String userID);

    void updateWithTodoList(TodoDto todoDto,String userID);
}
