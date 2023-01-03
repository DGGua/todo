package todo.entity;

import lombok.Data;

/**
 * @ClassName:CountData
 * @Description:获取个人统计时返回的类
 * @Author:liumengying
 * @Date: 2023/1/2 20:09
 * Version v1.0
 */
@Data
public class CountData {
    private CommonCount total;   //总计待办
    private CommonCount complete;  //累计完成待办
    private CommonCount last;   //剩余待办

}
