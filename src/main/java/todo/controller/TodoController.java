package todo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import todo.common.R;
import todo.dto.TodoDto;
import todo.entity.Todolist;
import todo.entity.Todos;
import todo.service.TodoListService;
import todo.service.TodosService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName:TodoController
 * @Description:待办操作
 * @Author:liumengying
 * @Date: 2023/1/1 16:11
 * Version v1.0
 */
@RestController
@Slf4j
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodosService todosService;

    @Autowired
    private TodoListService todoListService;

    /**
     * 新建待办
     * @param todoDto
     * @param request
     * @return
     */
    @PutMapping("/create")
    public R<String> create(@RequestBody TodoDto todoDto , HttpSession request){

       String userID =(String) request.getAttribute("userID");
        log.info("进入待办create方法:"+todoDto.toString()+"用户id:"+userID);
//       userID="111";  //测试

       if(userID==null){
           return R.error("未登录");
       }
       LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.eq(Todos::getId,todoDto.getId());   //查询该待办id
       queryWrapper.eq(Todos::getUserID,userID);   //查询用户ID
       Todos todos = todosService.getOne(queryWrapper);
       if(todos!=null){
           return R.error("待办已存在");
       }
       todosService.saveWithTodoList(todoDto,userID);
       return R.success("添加待办成功");
    }

    /**
     * 获取某天待办信息
     * @param date
     * @param request
     * @return
     */
    @GetMapping("/list")
    public R<List<TodoDto>> list(@RequestParam String date, HttpSession request){

        String userID =(String) request.getAttribute("userID");
        log.info("获取某天待办函数,当前用户ID:"+userID);
        if(userID==null){
            return R.error("未登录");
        }

     //1.构造查询器
      final LambdaQueryWrapper<Todos> queryWrapper =new LambdaQueryWrapper<>();
      //2.查询该日期待办 startTime
        queryWrapper.eq(Todos::getUserID,userID);   //当前用户的待办
        queryWrapper.like(Todos::getStarttime,date);   //模糊查询日期

        final List<Todos> todos=todosService.list(queryWrapper);

        if(todos.isEmpty()){
            return R.error("没有找到记录");
        }
        // 3.查到多条记录，遍历id,找todoList
        final List<TodoDto> todoDtos=todos.stream().map(item->{
            TodoDto todoDto=new TodoDto();
            BeanUtils.copyProperties(item,todoDto);

            final Integer id = item.getId();  //获取每个对象的主键id
            //根据id查询待办集
            //构造查询器
            final LambdaQueryWrapper<Todolist> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Todolist::getTodoID,id);
            //获取待办集
            final List<Todolist> todolist = todoListService.list(queryWrapper1);
            //赋值
            List<String> subs = new ArrayList<>();
            for (Todolist value : todolist) {
                String project = value.getProject();  //待办名称
                subs.add(project);
            }
            todoDto.setSubs(subs);

            return todoDto;
        }).collect(Collectors.toList());

        return R.success(todoDtos);

    }


    /**
     * 获取某个待办信息
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public R<TodoDto> get(@RequestParam int id,HttpSession request){

        String userID =(String) request.getAttribute("userID");

        log.info("获取某个待办信息，待办id:"+id+"用户ID:"+userID);
        if(userID==null){
            return R.error("未登录");
        }
        LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todos::getUserID,userID);   //当前用户待办
        queryWrapper.eq(Todos::getId,id);   //待办id

        Todos todos = todosService.getOne(queryWrapper);

        if(todos==null){
            return R.error("没有该待办");
        }
        TodoDto todoDto = new TodoDto();
        //拷贝
        BeanUtils.copyProperties(todos,todoDto);
        //条件构造器
        LambdaQueryWrapper<Todolist> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Todolist::getTodoID,id);
        List<Todolist> todolists = todoListService.list(queryWrapper1);

        //设置待办集
        List<String> subs = new ArrayList<>();
        for (Todolist value : todolists) {
            String project = value.getProject();  //待办名称
            subs.add(project);
        }
        todoDto.setSubs(subs);

        return R.success(todoDto);
    }

    /**
     * 删除某个待办
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public R<String> delete(@RequestParam int id,HttpSession request){

        log.info("进入待办delete方法,待办id:"+ id);
        String userID =(String) request.getAttribute("userID");
//        userID="111";  //测试
        if(userID==null){
            return R.error("未登录");
        }
        LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todos::getId,id);
        queryWrapper.eq(Todos::getUserID,userID);  //当前用户的待办
        Todos todos = todosService.getOne(queryWrapper);

        if(todos==null){
            return R.error("待办不存在");    //一般来说，删除的待办是存在的
        }

        todosService.deleteWithTodoList(id,userID);
        return R.success("删除成功");
    }

    /**
     * 完成某个待办
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/complete")
    public R<String> complete(@RequestParam int id,HttpSession request){

        String userID =(String) request.getAttribute("userID");
        log.info("完成某个待办，待办id:"+id+"用户id:"+userID);
        if(userID==null){
            return R.error("未登录");
        }

        LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todos::getUserID,userID);
        queryWrapper.eq(Todos::getId,id);

        Todos todos = todosService.getOne(queryWrapper);
        if(todos==null){
            return R.error("待办不存在");
        }
        todos.setStatus(1);
        todosService.updateById(todos);
        return R.success("待办已完成");
    }

    /**
     * 更新某个待办信息
     * @param todoDto
     * @param request
     * @return
     */
    @PostMapping("/update")
    public R<String> update(@RequestBody TodoDto todoDto,HttpSession request){

        String userID =(String) request.getAttribute("userID");
        log.info("更新某个待办信息，待办信息:"+todoDto.toString());
        if(userID==null){
            return R.error("未登录");
        }

        LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todos::getId,todoDto.getId());
        queryWrapper.eq(Todos::getUserID,userID);

        Todos todos = todosService.getOne(queryWrapper);
        if(todos==null){
            return R.error("待办不存在");   //一般来说更改代办时待办都存在
        }
        todosService.updateWithTodoList(todoDto,userID);
        return R.success("更改信息成功！");
    }

}
