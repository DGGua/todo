package todo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import todo.entity.Todolist;

@Mapper
public interface TodoListMapper extends BaseMapper<Todolist> {
}
