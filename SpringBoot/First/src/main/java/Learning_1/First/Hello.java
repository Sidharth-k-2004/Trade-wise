package Learning_1.First;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Hello {

    @RequestMapping("/")
    public String greet() {
        return "Hello World";
    }
}
