package raisetech.student.management;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	@Autowired
	private StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/student")
	public String getStudent(@RequestParam("name") String name) {
		Student student = studentRepository.searchByName(name);
		return student.getName() + " " + student.getAge() + "歳";
	}

	@PostMapping("/student")
	public void registerStudent(String name, int age) {
		studentRepository.registerStudent(name, age);
	}

	@PatchMapping("/student")
	public void updateStudentAge(String name, int age) {
		studentRepository.updateStudent(name, age);
	}

	@DeleteMapping("/student")
	public void deleteStudent(String name) {
		studentRepository.deleteStudent(name);
	}

//	課題用：全生徒の情報取得
	@GetMapping("/allStudents")
	public String getStudents() {
		List<Student> students = studentRepository.getStudents();
		String result = "";
		for (Student student : students) {
			result += student.getName() + " " + student.getAge() + "歳\n";
		}
		return result;
	}

}


