package todo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import todo.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
