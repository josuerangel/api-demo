package user;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by joshua on 7/3/16.
 */
@RestController
public class UserController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/users")
    public  User users(@RequestParam(value="name", defaultValue="Holitas") String name){
        return  new User(counter.incrementAndGet(), String.format(template, name));
    }
}
