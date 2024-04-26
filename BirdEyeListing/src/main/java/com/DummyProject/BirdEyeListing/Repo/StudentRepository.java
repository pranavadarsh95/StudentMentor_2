package com.DummyProject.BirdEyeListing.Repo;

import com.DummyProject.BirdEyeListing.Entities.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, Integer> {

	Student findByName(String studentName);
}
