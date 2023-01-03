package todo.entity;

import lombok.Data;

/**
 * @ClassName:CommonCount
 * @Description:统计基础类
 * @Author:liumengying
 * @Date: 2023/1/2 20:07
 * Version v1.0
 */
@Data
public class CommonCount {
    private int count;   //累计待办
    private int time;    //类及分钟数
}
