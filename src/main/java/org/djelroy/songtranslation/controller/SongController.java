package org.djelroy.songtranslation.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.djelroy.songtranslation.domain.Song;
import org.djelroy.songtranslation.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SongController {

	private static final Logger logger = LogManager.getLogger(SongController.class);

	@Autowired
	private SongService songService;

	@PostMapping(value = "/songs")
	public ResponseEntity<Song> createSong(@Valid @RequestBody Song song, Errors errors) {
		logger.info("Song creation REQUEST for: " + song.toString());
		
		if(errors.hasErrors()) {
			logger.info("Song title, artist and lyrics must be provided but received: " + song.toString());
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		boolean success = songService.create(song);
		if (success) {
			logger.info("Song creation SUCCESSFUL for: " + song.toString());
			return new ResponseEntity<Song>(song, HttpStatus.CREATED);
		} else {
			logger.error("Song creation FAILURE for: " + song.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/songs/{songId}")
	public ResponseEntity<Song> getSong(@PathVariable int songId) {

		logger.info("Song #" + songId + " REQUEST");

		Song song = songService.get(songId);

		return (song != null)? new ResponseEntity<>(song, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping(value = "/songs/{songId}")
	public ResponseEntity<Void> deleteSong(@PathVariable int songId) {
		logger.info("Song #" + songId + " deletion REQUEST");

		boolean success = songService.delete(songId);

		if(success) {
			logger.info("Song #" + songId + " deletion SUCCESSFUL");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		else {
			logger.info("Song #" + songId + " deletion FAILURE");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value = "/songs")
	public ResponseEntity<Void> updateSong(@Valid @RequestBody Song song, Errors errors) {
		
		logger.info("Song update REQUEST for: " + song.toString());
		
		if(errors.hasErrors() || song.getId() == null) {
			logger.info("Song id, title, artist and lyrics must be provided but received: " + song.toString());
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		

		Song updatedSong = songService.update(song);

		if(updatedSong != null) {
			logger.info("Song #" + song.getId() + " update SUCCESSFUL");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		else {
			logger.info("Song #" + song.getId() + " update FAILURE");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/songs")
	public ResponseEntity<List<Song>> getSong(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "artist", required = false) String artist) {
		
		logger.info("Song REQUEST [title, artist] = [" + title + ", " + artist + "]");

		boolean isTitleNotBlank = StringUtils.isNotBlank(title);
		boolean isArtistNotBlank = StringUtils.isNotBlank(artist);

		List<Song> songs;

		try {
			if (isTitleNotBlank && isArtistNotBlank) {
				songs = songService.getSong(URLDecoder.decode(title, StandardCharsets.UTF_8.name()),
						URLDecoder.decode(artist, StandardCharsets.UTF_8.name()));
			} else if (isArtistNotBlank) {
				songs = songService.getSongByArtist(URLDecoder.decode(artist, StandardCharsets.UTF_8.name()));
			} else if (isTitleNotBlank) {
				songs = songService.getSongsByTitle(URLDecoder.decode(title, StandardCharsets.UTF_8.name()));
			} else {
				songs = songService.getSongs();
			}
		} catch (UnsupportedEncodingException e) {
			logger.warn("Songs request FAILURE while calling URLDecoder.decode()", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return (CollectionUtils.isEmpty(songs)) ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(songs, HttpStatus.OK);
	}
	
}
