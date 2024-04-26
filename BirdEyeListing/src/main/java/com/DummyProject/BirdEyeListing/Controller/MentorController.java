package com.DummyProject.BirdEyeListing.Controller;

import com.DummyProject.BirdEyeListing.Entities.Mentor;
import com.DummyProject.BirdEyeListing.Entities.Student;
import com.DummyProject.BirdEyeListing.Repo.MentorRepository;
import com.DummyProject.BirdEyeListing.Repo.StudentRepository;
import com.DummyProject.BirdEyeListing.Service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mentor")
public class MentorController {

	@Autowired
	MentorRepository mentorRepository;

	@Autowired
	MentorService mentorService;

	@Autowired
	StudentRepository studentRepository;

	@PostMapping("/")
	public ResponseEntity<Mentor> createMentor(@RequestBody Mentor mentor) {
		// Check if the provided list of student IDs is not null
		if (mentor.getStudentIds() == null) {
			mentor.setStudentIds(new ArrayList<>());
		}

		Mentor createdMentor = mentorService.createOrUpdateMentor(mentor);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdMentor);
	}


	// Fetch All Mentors Records
	@GetMapping("/")
	public ResponseEntity<?> getMentors(){
		return ResponseEntity.ok(mentorRepository.findAll());
	}

	// Get a mentor Record
	@GetMapping("/{id}")
	public ResponseEntity<?> getMentor(@PathVariable("id") Integer id){
		Optional<Mentor> mentorOptional = mentorRepository.findById(id);
		if(mentorOptional.isPresent()){
			return new ResponseEntity<Mentor>(mentorOptional.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>("No mentor record present with id: "+id, HttpStatus.OK);

	}

	// Delete Any Mentor Record
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMentor(@PathVariable("id") Integer id){
		Optional<Mentor> mentorOptional = mentorRepository.findById(id);
		if(mentorOptional.isPresent()){
			// Remove mapping of mentor from student

			Mentor mentor = mentorOptional.get();

			if(!CollectionUtils.isEmpty(mentor.getStudentIds())){
				List<String> list = mentor.getStudentIds();

				for(int i = 0; i<list.size(); i++){
					Optional<Student> student = studentRepository.findById(Integer.valueOf(list.get(i)));
					if(student.isPresent()){
						Student dbStudent = student.get();
						dbStudent.setMentorId(null);
						studentRepository.save(dbStudent);
					}
				}
			}
			mentorRepository.deleteById(id);
			return new ResponseEntity<>("mentor record deleted with id:"+ id, HttpStatus.OK);
		}
		return new ResponseEntity<>("No mentor record present with id: "+id, HttpStatus.OK);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateMentor(@RequestBody Mentor mentor , @PathVariable("id") Integer id){
		Optional<Mentor> mentorOptional = mentorRepository.findById(id);
		if(mentorOptional.isPresent()){
			Mentor dbMentor = mentorOptional.get();
			dbMentor.setMentorName(mentor.getMentorName());
			dbMentor.setMentorCity(mentor.getMentorCity());
			dbMentor.setSpecialization(mentor.getSpecialization());
			dbMentor.setSalary(mentor.getSalary());
			mentorRepository.save(dbMentor);
			return new ResponseEntity<>(dbMentor, HttpStatus.OK);
		}
		return new ResponseEntity<>("No mentor record present with id: "+id, HttpStatus.OK);
	}

}
