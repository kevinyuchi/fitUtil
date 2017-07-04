package controller;

/**
 * Created by kkaiwen on 7/2/17.
 */
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

@RequestMapping("/")
public String index() {
	return "Greetings from Spring Boot!\n";
}


}

