package todo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import lombok.extern.slf4j.Slf4j;
import todo.common.R;
import todo.dto.TodoDto;
import todo.entity.Todolist;
import todo.entity.Todos;
import todo.service.TodoListService;
import todo.service.TodosService;
import todo.utils.DateDiff;

/**
 * @ClassName:TodoController
 * @Description:待办操作
 * @Author:liumengying
 * @Date: 2023/1/1 16:11
 *        Version v1.0
 */
@RestController
@Slf4j
@CrossOrigin(origins = { "http://todo.dggua.top" }, allowCredentials = "true")
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodosService todosService;

    @Autowired
    private TodoListService todoListService;

    /**
     * 新建待办
     * 
     * @param todoDto
     * @param request
     * @return
     */
    @PutMapping("/create")
    public R<String> create(@RequestBody TodoDto todoDto, HttpSession request) {

        String userID = (String) request.getAttribute("userID");
        log.info("进入待办create方法:" + todoDto.toString() + "用户id:" + userID);
        // userID="111"; //测试

        if (userID == null) {
            return R.error("未登录");
        }
        LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todos::getId, todoDto.getId()); // 查询该待办id
        queryWrapper.eq(Todos::getUserID, userID); // 查询用户ID
        Todos todos = todosService.getOne(queryWrapper);
        if (todos != null) {
            return R.error("待办已存在");
        }
        todosService.saveWithTodoList(todoDto, userID);
        return R.success("添加待办成功");
    }

    /**
     * 获取某天待办信息
     * 
     * @param date
     * @param request
     * @return
     */
    @GetMapping("/list")
    public R<List<TodoDto>> list(@RequestParam String date, HttpSession request) throws ParseException {

        String userID = (String) request.getAttribute("userID");
        log.info("获取某天待办函数,当前用户ID:" + userID);
        if (userID == null) {
            return R.error("未登录");
        }

        // 转换日期
        // StringBuilder temp = new StringBuilder(date);
        // temp.insert(8, " 00:00:00");
        // temp.insert(6, "-");
        // temp.insert(4, "-");
        Date queryDate = new SimpleDateFormat("yyyyMMdd").parse(date);
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
        // HH:mm:ss");//SimpleDateFormat格式和解析日期的类
        // Date parse = sdf.parse(date1);//得到java.util.Date对象
        // long time = parse.getTime();//返回当前日期对应的long类型的毫秒数
        // java.sql.Date date2 = new
        // java.sql.Date(time);//得到java.sql.Date类型的对象，就可以插入到数据表对应的Date字段
        // log.info("转换的日期:"+date2);

        // 1.构造查询器
        final LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Todos> queryWrapper1 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Todos> queryWrapper2 = new LambdaQueryWrapper<>();
        // 2.查询,构造查询器（句段有点重复）
        queryWrapper.eq(Todos::getUserID, userID); // 当前用户的待办
        queryWrapper.eq(Todos::getStatus, 0); // 所有未完成待办
        queryWrapper1.eq(Todos::getUserID, userID); // 当前用户的待办
        queryWrapper1.eq(Todos::getStatus, 0); // 所有未完成待办
        queryWrapper2.eq(Todos::getUserID, userID); // 当前用户的待办
        queryWrapper2.eq(Todos::getStatus, 0); // 所有未完成待办

        // noclock没有时间，backclock只有endtime, normalclock有start和end
        // (1)全部noclock的未完成待办
        log.info("开始查询noclock");
        queryWrapper.eq(Todos::getTimecategory, "noclock");
        List<Todos> todos_noclock = todosService.list(queryWrapper);
        List<Todos> todos = new ArrayList<>(todos_noclock);

        log.info("开始查询backlock");
        // (2)截止日期在这一天之后的截止日期未完成待办
        queryWrapper1.eq(Todos::getTimecategory, "backclock");
        // queryWrapper1.ge(Todos::getEndtime, date1);
        List<Todos> todos_backclock = todosService.list(queryWrapper1);
        for (Todos todo : todos_backclock) {
            if (DateDiff.dayDiff(queryDate, todo.getEndtime()) >= 0) {
                todos.add(todo);
            }
        }

        // (3)当前查看日期在开始日期和截止日期之间的所有未完成待办
        log.info("开始查询normalclock");
        queryWrapper2.eq(Todos::getTimecategory, "normalclock"); // 正常待办
        // queryWrapper2.lt(Todos::getStarttime, date1); // 小于当前日期
        // queryWrapper2.gt(Todos::getEndtime, date1); // 大于当前日期

        List<Todos> todos_normalclock = todosService.list(queryWrapper2);
        for (Todos todo : todos_normalclock) {
            if (DateDiff.dayDiff(queryDate, todo.getEndtime()) >= 0
                    && DateDiff.dayDiff(todo.getStarttime(), queryDate) >= 0) {
                todos.add(todo);
            }
        }

        // if (todos.isEmpty()) {
        // return R.error("没有找到记录");
        // }

        // 3.查到多条记录，遍历id,找todoList
        final List<TodoDto> todoDtos = todos.stream().map(item -> {
            TodoDto todoDto = new TodoDto();
            BeanUtils.copyProperties(item, todoDto);

            final Integer id = item.getId(); // 获取每个对象的主键id
            // 根据id查询待办集
            // 构造查询器
            final LambdaQueryWrapper<Todolist> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(Todolist::getTodoID, id);
            // 获取待办集
            final List<Todolist> todolist = todoListService.list(queryWrapper3);
            // 赋值
            List<String> subs = new ArrayList<>();
            for (Todolist value : todolist) {
                String project = value.getProject(); // 待办名称
                subs.add(project);
            }
            todoDto.setSubs(subs);

            return todoDto;
        }).collect(Collectors.toList());

        return R.success(todoDtos);

    }

    /**
     * 获取某个待办信息
     * 
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public R<TodoDto> get(@RequestParam int id, HttpSession request) {

        String userID = (String) request.getAttribute("userID");

        log.info("获取某个待办信息，待办id:" + id + "用户ID:" + userID);
        if (userID == null) {
            return R.error("未登录");
        }
        LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todos::getUserID, userID); // 当前用户待办
        queryWrapper.eq(Todos::getId, id); // 待办id

        Todos todos = todosService.getOne(queryWrapper);

        if (todos == null) {
            return R.error("没有该待办");
        }
        TodoDto todoDto = new TodoDto();
        // 拷贝
        BeanUtils.copyProperties(todos, todoDto);
        // 条件构造器
        LambdaQueryWrapper<Todolist> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Todolist::getTodoID, id);
        List<Todolist> todolists = todoListService.list(queryWrapper1);

        // 设置待办集
        List<String> subs = new ArrayList<>();
        for (Todolist value : todolists) {
            String project = value.getProject(); // 待办名称
            subs.add(project);
        }
        todoDto.setSubs(subs);

        return R.success(todoDto);
    }

    /**
     * 删除某个待办
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public R<String> delete(@RequestParam int id, HttpSession request) {

        log.info("进入待办delete方法,待办id:" + id);
        String userID = (String) request.getAttribute("userID");
        // userID="111"; //测试
        if (userID == null) {
            return R.error("未登录");
        }
        LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todos::getId, id);
        queryWrapper.eq(Todos::getUserID, userID); // 当前用户的待办
        Todos todos = todosService.getOne(queryWrapper);

        if (todos == null) {
            return R.error("待办不存在"); // 一般来说，删除的待办是存在的
        }

        todosService.deleteWithTodoList(id, userID);
        return R.success("删除成功");
    }

    /**
     * 完成某个待办
     * 
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/complete")
    public R<String> complete(@RequestParam int id, HttpSession request) {

        String userID = (String) request.getAttribute("userID");
        log.info("完成某个待办，待办id:" + id + "用户id:" + userID);
        if (userID == null) {
            return R.error("未登录");
        }

        LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todos::getUserID, userID);
        queryWrapper.eq(Todos::getId, id);

        Todos todos = todosService.getOne(queryWrapper);
        if (todos == null) {
            return R.error("待办不存在");
        }
        todos.setStatus(1);
        todosService.updateById(todos);
        return R.success("待办已完成");
    }

    /**
     * 更新某个待办信息
     * 
     * @param todoDto
     * @param request
     * @return
     */
    @PostMapping("/update")
    public R<String> update(@RequestBody TodoDto todoDto, HttpSession request) {

        String userID = (String) request.getAttribute("userID");
        log.info("更新某个待办信息，待办信息:" + todoDto.toString());
        if (userID == null) {
            return R.error("未登录");
        }

        LambdaQueryWrapper<Todos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todos::getId, todoDto.getId());
        queryWrapper.eq(Todos::getUserID, userID);

        Todos todos = todosService.getOne(queryWrapper);
        if (todos == null) {
            return R.error("待办不存在"); // 一般来说更改代办时待办都存在
        }
        todosService.updateWithTodoList(todoDto, userID);
        return R.success("更改信息成功！");
    }

}
