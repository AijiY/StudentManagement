package raisetech.student.management;

import java.util.HashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
	private final HashMap<String, String> info = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/info")
	public HashMap<String, String> getInfo() {
		return info;
	}

	@PostMapping("/info")
	public void addInfo(int id, String name) {
		info.put(String.valueOf(id), name);
	}
}


