package com.DummyProject.BirdEyeListing.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CheckHealth {
	@GetMapping("/health")
	public ResponseEntity<?> health(){
		return ResponseEntity.ok("Hello tilak and pranav! welcome");
	}
}
