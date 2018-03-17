package org.djelroy.songtranslation.dao;

import java.util.List;

import org.djelroy.songtranslation.domain.Song;

public interface SongDao {

	void create(Song song);
	void delete(int id);
	Song get(int id);
	Song update(Song song);
	List<Song> getSongByArtist(String artist);
	List<Song> getSongsByTitle(String title);
	List<Song> getSongs(String title, String artist);
	List<Song> getSongs(int size);
}
