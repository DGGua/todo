package todo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.dto.TodoDto;
import todo.entity.Todolist;
import todo.entity.Todos;
import todo.mapper.TodosMapper;
import todo.service.TodoListService;
import todo.service.TodosService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName:TodosImpl
 * @Description:待办实现
 * @Author:liumengying
 * @Date: 2023/1/1 16:03
 * Version v1.0
 */
@Service
public class TodosImpl extends ServiceImpl<TodosMapper, Todos> implements TodosService{

    @Autowired
    private TodoListService todoListService;

    @Override
    @Transactional
    public void saveWithTodoList(TodoDto todoDto,String userID){

        //保存待办信息
        todoDto.setUserID(userID);
        this.save(todoDto);

        //获取待办集信息
       List<String> subs = todoDto.getSubs();
       List<Todolist> todoLists = new ArrayList<>();
       for(int i=0;i<subs.size();++i){
           Todolist todolist =new Todolist();
           todolist.setProject(subs.get(i));    //设置待办名称
           todolist.setTodoID(todoDto.getId()); //设置所属待办id
           todolist.setUserID(userID);
           todoLists.add(todolist);   //加入数组

       }
        //批量保存待办关联的待办集信息
        todoListService.saveBatch(todoLists);
    }

    @Override
    @Transactional
    public void deleteWithTodoList(int id,String userID){
        //删除待办信息
       this.removeById(id);
       //删除待办关联的待办集
        LambdaQueryWrapper<Todolist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todolist::getTodoID,id);
        queryWrapper.eq(Todolist::getUserID,userID);
        todoListService.remove(queryWrapper);
    }

    @Override
    @Transactional
    public void updateWithTodoList(TodoDto todoDto,String userID){
        //更新待办信息
        this.updateById(todoDto);
        int id = todoDto.getId();

        LambdaQueryWrapper<Todolist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todolist::getTodoID,id);
        queryWrapper.eq(Todolist::getUserID,userID);  //该用户->待办->待办集
        //先删除再更新
        todoListService.remove(queryWrapper);

        final List<String> subs = todoDto.getSubs();
        final List<Todolist> todoLists = new ArrayList<>();

        for(int i=0;i<subs.size();++i){
            Todolist todolist =new Todolist();
            todolist.setProject(subs.get(i));    //设置待办名称
            todolist.setTodoID(todoDto.getId()); //设置所属待办id
            todolist.setUserID(userID);
            todoLists.add(todolist);   //加入数组

        }
        //更新
        todoListService.saveBatch(todoLists);
    }
}
