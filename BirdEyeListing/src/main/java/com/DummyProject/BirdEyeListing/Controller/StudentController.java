package com.DummyProject.BirdEyeListing.Controller;

import com.DummyProject.BirdEyeListing.Entities.Mentor;
import com.DummyProject.BirdEyeListing.Entities.Student;
import com.DummyProject.BirdEyeListing.Repo.StudentRepository;
import com.DummyProject.BirdEyeListing.Service.MentorService;
import com.DummyProject.BirdEyeListing.Service.StudentService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	StudentService studentService;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	MentorService mentorService;

	@PostMapping("/")
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		// Check if the provided mentor ID exists
		Optional<Mentor> optionalMentor = mentorService.getMentorById(String.valueOf(student.getMentorId()));
		if (optionalMentor.isEmpty()) {
			return ResponseEntity.badRequest().build(); // Mentor not found
		}

		Mentor mentor = optionalMentor.get();

		// Associate the student with the mentor
		student.setMentorId(mentor.getId());

		// Check if the mentor already has this student associated
		if (mentor.getStudentIds().contains(student.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Mentor already has this student
		}

		mentor.getStudentIds().add(student.getId());
		mentorService.createOrUpdateMentor(mentor);

		Student createdStudent = studentService.createOrUpdateStudent(student);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
	}

	// Fetch All Student Records
	@GetMapping("/")
	public ResponseEntity<?> getStudents() {
		return ResponseEntity.ok(studentRepository.findAll());
	}

	// Get a student Record
	@GetMapping("/{id}")
	public ResponseEntity<?> getStudent(@PathVariable("id") Integer id) {
		Optional<Student> studentOptional = studentRepository.findById(id);
		if (studentOptional.isPresent()) {
			return new ResponseEntity<Student>(studentOptional.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>("No student record present with id: " + id, HttpStatus.OK);

	}

	// Delete Any Student Record
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteStudent(@PathVariable("id") Integer id) {
		Optional<Student> studentOptional = studentRepository.findById(id);
		if (studentOptional.isPresent()) {

			Optional<Mentor> oldMentor = mentorService.getMentorById(studentOptional.get().getMentorId());
			if (oldMentor.isPresent() && oldMentor.get().getStudentIds().contains(String.valueOf(id))) {
				// need to remove student
				List<String> studentIds = oldMentor.get().getStudentIds();
				studentIds.remove(String.valueOf(id));
				oldMentor.get().setStudentIds(studentIds);
				mentorService.createOrUpdateMentor(oldMentor.get());

				studentRepository.deleteById(id);
			}
			return new ResponseEntity<>("student record deleted with id:" + id, HttpStatus.OK);
		}
		return new ResponseEntity<>("No student record present with id: " + id, HttpStatus.OK);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateStudent(@RequestBody Student student, @PathVariable("id") Integer id) {
		Optional<Student> studentOptional = studentRepository.findById(id);
		if (studentOptional.isPresent()) {
			Student dbStudent = studentOptional.get();
			dbStudent.setName(student.getName());
			dbStudent.setCity(student.getCity());
			dbStudent.setCollege(student.getCollege());

			// based on student if mentor is updated
			if (StringUtils.isNotBlank(student.getMentorId())) {
				Optional<Mentor> mentor = mentorService.getMentorById(student.getMentorId());

				if (mentor.isPresent() && !mentor.get().getStudentIds().contains(student.getId())) {
					List<String> studentsIds = mentor.get().getStudentIds();
					studentsIds.add(String.valueOf(id));
					mentor.get().setStudentIds(studentsIds);
					mentorService.createOrUpdateMentor(mentor.get());
				}
				// removing current student from previously associated mentor
				// Note: if future mentor is not present then no need to remove old mentor
				Optional<Mentor> oldMentor = null;
				if (dbStudent != null && StringUtils.isNotBlank(dbStudent.getMentorId()))
					oldMentor = mentorService.getMentorById(dbStudent.getMentorId());

				if (mentor.isPresent() && oldMentor != null && oldMentor.isPresent() && oldMentor.get().getStudentIds().contains(String.valueOf(id))) {
					// need to remove student
					List<String> studentIds = oldMentor.get().getStudentIds();
					studentIds.remove(dbStudent.getId());
					oldMentor.get().setStudentIds(studentIds);
					mentorService.createOrUpdateMentor(oldMentor.get());
				}
				if(mentor.isPresent()) {
					dbStudent.setMentorId(student.getMentorId());
				}
			}
			studentRepository.save(dbStudent);
			return new ResponseEntity<>(dbStudent, HttpStatus.OK);
		}
		return new ResponseEntity<>("No student record present with id: " + id, HttpStatus.OK);
	}

	@GetMapping("/uniqueCities")
	public ResponseEntity<?> getUniqueCities() {
		return ResponseEntity.ok(studentService.getAllCities());
	}

}
