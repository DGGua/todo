package todo.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:R
 * @Description:返回类型
 * @Author:liumengying
 * @Date: 2023/1/1 12:54
 * Version v1.0
 */

@Data
public class R<T> implements Serializable {

    private Integer code;  //返回码：1为成功
    private String msg;  //错误信息
    private T data; //返回的数据
    private Map map =new HashMap(); //动态数据，用于存储未知类型或者经常变化的数据，一键值对形式

    public static <T> R<T> success(T object){
        R<T> r=new R<T>();
        r.data=object;
        r.code=1;
        r.msg="成功";
        return r;

    }

    public static <T> R<T> success(String msg){
        R<T> r=new R<T>();
        r.msg=msg;
        r.code=1;
        return r;
    }
    //创建错误返回结果
    public static <T> R<T> error(String msg){
        R<T> r=new R<T>();
        r.msg=msg;
        r.code=0;
        return r;
    }

    //添加动态数据
    public R<T> add(String key,Object value ){
        this.map.put(key,value);
        return this;
    }
}

