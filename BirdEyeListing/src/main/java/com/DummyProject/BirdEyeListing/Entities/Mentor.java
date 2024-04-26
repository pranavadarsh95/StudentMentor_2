package com.DummyProject.BirdEyeListing.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "mentors")
public class Mentor {
	@MongoId
	private String id;
	private String mentorName;
	private String mentorCity;
	private String specialization;
	private String salary;
	private List<String> studentIds;
}
