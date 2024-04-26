package com.DummyProject.BirdEyeListing.Repo;

import com.DummyProject.BirdEyeListing.Entities.Mentor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MentorRepository extends MongoRepository<Mentor, Integer> {

	Mentor findByMentorName(String mentorName);
}
