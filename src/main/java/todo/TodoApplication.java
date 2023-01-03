package todo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName:TodoApplication
 * @Description:程序启动类
 * @Author:liumengying
 * @Date: 2023/1/1 13:08
 * Version v1.0
 */
@Slf4j
@SpringBootApplication
@EnableTransactionManagement
public class TodoApplication {
    public static void main(String[] args){
        SpringApplication.run(TodoApplication.class,args);
        log.info("todo项目启动成功...");
    }
}
