package todo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName:todos
 * @Description:待办事项类
 * @Author:liumengying
 * @Date: 2023/1/1 13:12
 * Version v1.0
 */
@Data
public class Todos {
    private String name;
    private String category;  //待办事项类别:待办，待办集
    private String timecategory;  //计时类型:倒计时，正向计时，不计时

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endtime;  //截止日期

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime starttime;  //开始日期

    private String userID;           //所属用户ID
    private Integer id;           //待办id
    private Integer status;   //待办状态 0:未完成

}
