package com.DummyProject.BirdEyeListing.Service;

import com.DummyProject.BirdEyeListing.Entities.Student;
import com.DummyProject.BirdEyeListing.Repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {
	@Autowired
	private StudentRepository studentRepository;

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public Optional<Student> getStudentById(String id) {
		return studentRepository.findById(Integer.valueOf(id));
	}

	public Student createOrUpdateStudent(Student student) {
		return studentRepository.save(student);
	}

	public void deleteStudentById(String id) {
		studentRepository.deleteById(Integer.valueOf(id));
	}

	public Set<String> getAllCities() {
		return studentRepository.findAll().stream()
				.map(Student::getCity)
				.collect(Collectors.toSet());
	}

}
