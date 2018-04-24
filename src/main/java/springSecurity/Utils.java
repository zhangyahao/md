package springSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @program: md
 * @description: 常用的操作
 * @author: Zhang
 * @create: 2018-04-24 10:33
 **/
public class Utils {
    public static void main(String[] args) {
        SecurityContextHolder.getContext().getAuthentication().getName();

// 获取用户ROLE：
        SecurityContextHolder.getContext().getAuthentication().getAuthorities();

    }
}
