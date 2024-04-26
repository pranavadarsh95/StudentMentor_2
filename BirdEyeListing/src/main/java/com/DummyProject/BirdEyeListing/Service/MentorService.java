package com.DummyProject.BirdEyeListing.Service;

import com.DummyProject.BirdEyeListing.Entities.Mentor;
import com.DummyProject.BirdEyeListing.Repo.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MentorService {
	@Autowired
	private MentorRepository mentorRepository;

	public List<Mentor> getAllMentors() {
		return mentorRepository.findAll();
	}

	public Optional<Mentor> getMentorById(String id) {
		return mentorRepository.findById(Integer.parseInt(id));
	}

	public Mentor createOrUpdateMentor(Mentor mentor) {
		return mentorRepository.save(mentor);
	}

	public void deleteMentorById(Integer id) {
		mentorRepository.deleteById(id);
	}

	public boolean existsById(Integer id) {
		return mentorRepository.existsById(id);
	}
}