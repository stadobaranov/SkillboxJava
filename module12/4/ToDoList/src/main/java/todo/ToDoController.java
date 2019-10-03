package todo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToDoController {
    @RequestMapping("/")
    public String index() {
        return String.format("%d", (int)(Math.random() * 100) + 1);
    }
}
