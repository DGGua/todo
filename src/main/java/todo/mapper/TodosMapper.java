package todo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import todo.entity.Todos;

@Mapper
public interface TodosMapper extends BaseMapper<Todos> {
}
