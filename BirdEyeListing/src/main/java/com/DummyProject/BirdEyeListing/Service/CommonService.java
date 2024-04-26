package com.DummyProject.BirdEyeListing.Service;

import com.DummyProject.BirdEyeListing.Entities.Mentor;
import com.DummyProject.BirdEyeListing.Entities.Student;
import com.DummyProject.BirdEyeListing.Repo.MentorRepository;
import com.DummyProject.BirdEyeListing.Repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommonService {

	@Autowired
	MentorRepository mentorRepository;

	@Autowired
	StudentRepository studentRepository;

	public Set<String> findAllStudents(String mentorName) {
		Mentor mentor = mentorRepository.findByMentorName(mentorName);

		if (mentor == null || CollectionUtils.isEmpty(mentor.getStudentIds())) {
			return Collections.emptySet(); // Return an empty set if mentor or students are null or empty
		}
		return mentor.getStudentIds().stream().map(studentId -> studentRepository.findById(Integer.valueOf(studentId)))
				.filter(Optional::isPresent).map(Optional::get).map(Student::getName).collect(Collectors.toSet());
	}

}
