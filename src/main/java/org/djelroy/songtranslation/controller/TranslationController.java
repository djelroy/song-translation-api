package org.djelroy.songtranslation.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.djelroy.songtranslation.domain.Song;
import org.djelroy.songtranslation.domain.Translation;
import org.djelroy.songtranslation.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslationController {

	private static final Logger logger = LogManager.getLogger(TranslationController.class);

	@Autowired
	private TranslationService translationService;
	
	@PostMapping(value = "/songs/{songId}/translations")
	public ResponseEntity<Translation> createTranslation(@RequestBody Translation translation, Errors errors, @PathVariable int songId) {

		logger.info("Translation creation REQUEST for song #" + songId + " and translation: " + translation.toString());
		
		if(errors.hasErrors()) {
				logger.info("Translation language, title and lyrics must be provided but received: " + translation.toString());
				return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		Song song = new Song();
		song.setId(songId);
		translation.setSong(song);

		boolean success = translationService.create(translation);
		if (success) {
			logger.info("Translation creation SUCCESSFUL for: " + translation.toString());
			return new ResponseEntity<>(translation, HttpStatus.CREATED);
			
		} else {
			logger.info("Translation creation FAILURE for: " + translation.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/translations/{translationId}")
	public ResponseEntity<Translation> getTranslation(@PathVariable int translationId) {
		
		logger.info("Translation #" + translationId + " REQUEST");

		Translation translation = translationService.get(translationId);
		
		return (translation != null)? new ResponseEntity<>(translation, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}

	
	@DeleteMapping(value = "/translations/{translationId}")
	public ResponseEntity<Void> deleteteTranslation(@PathVariable int translationId) {

		logger.info("Translation #" + translationId + " deletion REQUEST");

		Translation translation = new Translation();
		translation.setId(translationId);

		boolean success = translationService.delete(translationId);

		if(success) {
			logger.info("Translation #" + translationId + " deletion SUCCESSFUL");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		else {
			logger.info("Translation #" + translationId + " deletion FAILURE");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/translations")
	public ResponseEntity<Void> updateTranslation(@RequestBody Translation translation, Errors errors) {
		logger.info("Translation update REQUEST for: " + translation.toString());
		
		if(errors.hasErrors() || translation.getId() == null) {
			logger.info("Translation id, language, title and lyrics must be provided but received: " + translation.toString());
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Translation updatedTranslation = translationService.update(translation);
		
		if(updatedTranslation != null) {
			logger.info("Translation #" + translation.getId() + " update SUCCESSFUL");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		else {
			logger.info("Translation #" + translation.getId() + " update FAILURE");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
