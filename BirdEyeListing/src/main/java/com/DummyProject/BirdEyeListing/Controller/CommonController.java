package com.DummyProject.BirdEyeListing.Controller;

import com.DummyProject.BirdEyeListing.Entities.Mentor;
import com.DummyProject.BirdEyeListing.Entities.Student;
import com.DummyProject.BirdEyeListing.Repo.MentorRepository;
import com.DummyProject.BirdEyeListing.Repo.StudentRepository;
import com.DummyProject.BirdEyeListing.Service.CommonService;
import com.DummyProject.BirdEyeListing.Service.StudentService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/find")
public class CommonController {

	@Autowired
	MentorRepository mentorRepository;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	StudentService studentService;

	@Autowired
	CommonService commonService;

	@GetMapping("/mentors/{studentName}")
	public String findMentorsBasedOnStudent(@PathVariable("studentName") String studentName) {
		Student student = studentRepository.findByName(studentName);
		if (student != null && StringUtils.isNotBlank(String.valueOf(student.getMentorId()))) {
			Optional<Mentor> mentor = mentorRepository.findById(Integer.valueOf(student.getMentorId()));

			if (mentor.isPresent()) {
				return mentor.get().getMentorName();
			}
		}
		return "No mentor found!!!";
	}

	@GetMapping("/students/{mentorName}")
	public Set<String> findStudentsBasedOnMentor(@PathVariable("mentorName") String mentorName) {
		return commonService.findAllStudents(mentorName);
	}

}
