package org.djelroy.songtranslation.service;

import java.util.List;

import org.djelroy.songtranslation.domain.Song;

public interface SongService {
	
	boolean create(Song song);
	boolean delete(int id);
	Song get(int id);
	Song update(Song song);
	List<Song> getSongByArtist(String artist);
	List<Song> getSongsByTitle(String title);
	List<Song> getSong(String title, String artist);
	List<Song> getSongs();
}
