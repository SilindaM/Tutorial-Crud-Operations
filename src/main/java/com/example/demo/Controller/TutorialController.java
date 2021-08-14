package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.TutorialRepository;

@RestController
@RequestMapping("/api/tutorials")
@CrossOrigin(origins="http://localhost:8081")
public class TutorialController {

	@Autowired
	TutorialRepository tutorialRepository;
	
	
	//Get All tutorials
	@GetMapping("/all")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title){
		try {
			List<Tutorial> tutorials=new ArrayList<Tutorial>();
			//if the user does not search by title
			 if(title==null) {
				 tutorialRepository.findAll().forEach(tutorials::add);
			 }
			 //if user searches by title
			 else {
				 tutorialRepository.findByTitleContaining(title);
			 }
			 //if tutorials are empty
			 if(tutorials.isEmpty()) {
				 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			 }
			 return new ResponseEntity<>(tutorials,HttpStatus.OK);
			
		}
		catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//get tutorial by id
	@GetMapping("/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id){
		Optional<Tutorial> tutorialData=tutorialRepository.findById(id);
		if(tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(),HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	//Post tutorial
	@PostMapping("")
	public ResponseEntity<Tutorial> PostTutorial(@RequestBody Tutorial tutorial){
		try {
			Tutorial tuto=tutorialRepository.save(new Tutorial(tutorial.getTitle(),tutorial.getDescription(), false));
			return new ResponseEntity<>(tuto,HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//update a tutorial
	@PutMapping("/{id}")
	public ResponseEntity<Tutorial> UpdateTutorial(@PathVariable("id") long id,@RequestBody Tutorial tutorial ){
		//we find the data from old data
		Optional<Tutorial> tutorialData=tutorialRepository.findById(id);
		//if the data is present
		if(tutorialData.isPresent()) {
			//get the data from saved tutorials
			Tutorial tuto=tutorialData.get();
			
			//tuto is the old data, tutorial is the new that from the request body
			tuto.setTitle(tutorial.getTitle());
			tuto.setDescription(tutorial.getDescription());
			tuto.setPublished(tutorial.getPublished());
			return new ResponseEntity<>(tutorialRepository.save(tuto),HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	//DELETE BY ID
	@DeleteMapping("/id")
	public ResponseEntity<HttpStatus> DeleteById(@PathVariable("id") long id){
		try {
			tutorialRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//Delete All
	@DeleteMapping("/all")
	public ResponseEntity<HttpStatus> DeleteAll(){
		try {
			tutorialRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//Get pushished
	@GetMapping("/published")
	public ResponseEntity<List<Tutorial>> findAllPublished(){
		try {
			List<Tutorial> tutorials=tutorialRepository.findByPublished(true);
			if(tutorials.isEmpty()) {
				return new ResponseEntity<>(tutorials,HttpStatus.OK);
			}
			return new ResponseEntity<>(tutorials,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
