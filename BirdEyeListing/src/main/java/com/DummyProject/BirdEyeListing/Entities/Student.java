package com.DummyProject.BirdEyeListing.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "students")
public class Student {
	@MongoId
	private String id;
	private String name;
	private String city;
	private String college;
	private String mentorId;
}
