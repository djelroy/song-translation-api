package org.djelroy.songtranslation.service;

import org.djelroy.songtranslation.domain.Song;

public interface SongService {
	
	boolean create(Song song);
	boolean delete(int id);
	Song get(int id);
	Song update(Song song);
}
