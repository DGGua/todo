package todo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.common.R;
import todo.entity.CommonCount;
import todo.entity.CountData;
import todo.entity.Todos;
import todo.service.TodoListService;
import todo.service.TodosService;

import javax.servlet.http.HttpSession;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:DataController
 * @Description:统计控制类
 * @Author:liumengying
 * @Date: 2023/1/2 17:01
 * Version v1.0
 */
@RequestMapping("/data")
@RestController
@Slf4j
public class DataController {

    @Autowired
    private TodosService todosService;

    @GetMapping("/analysis")
    public R<CountData> analysis(HttpSession session){

        String userID= (String) session.getAttribute("userID");   //获取当前登录用户ID
        log.info("进入统计函数，当前用户ID:"+userID);

        if(userID==null){
            return R.error("用户未登录");
        }
        CountData countData = new CountData();
        //1.获取当前用户的所有待办，统计实践

        //构造器
        LambdaQueryWrapper<Todos> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Todos::getUserID,userID);
        List<Todos> todos1 = todosService.list(queryWrapper1);

        //如果所有待办为空，即刻返回
        if(todos1.isEmpty()){
            return R.error("没有待办");
        }

        CommonCount total = new CommonCount();  //所有待办统计
        CommonCount complete = new CommonCount();  //完成待办统计
        CommonCount last = new CommonCount();   //余下待办

        int all_count = todos1.size();
        total.setCount(all_count);

        int countTime = 0;  //所有待办总时间
        for(Todos todo :todos1){
            int temp = (int) ChronoUnit.MINUTES.between(todo.getStarttime(), todo.getEndtime());
            countTime = countTime+temp;
        }
        total.setTime(countTime);

        //2.获取当前用户完成的待办，统计时间
        LambdaQueryWrapper<Todos> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Todos::getUserID,userID);
        queryWrapper2.eq(Todos::getStatus,1);
        List<Todos> todos2= todosService.list(queryWrapper2);
        complete.setCount(todos2.size());  //完成待办数
        int countTime2 = 0;
        for (Todos todo:todos2){
            int temp = (int) ChronoUnit.MINUTES.between(todo.getStarttime(),todo.getEndtime());
            countTime2 = countTime2+temp;
        }
        complete.setTime(countTime2);

        //3.获取当前用户的未完成待办，统计时间

        queryWrapper1.eq(Todos::getStatus,0);   //第一个比较得出所有待办，现在直接使用queryWrapper1的比较结果，得出未完成待办
        List<Todos> todos3 = todosService.list(queryWrapper1);
        last.setCount(todos3.size());   //未完成待办数
        int countTime3 = 0;
        for (Todos todo:todos3){
            int temp = (int) ChronoUnit.MINUTES.between(todo.getStarttime(),todo.getEndtime());
            countTime3 = countTime3+temp;
        }
        last.setTime(countTime3);
        //4.返回结果
        countData.setTotal(total);
        countData.setComplete(complete);
        countData.setLast(last);

        return R.success(countData);

    }
}
