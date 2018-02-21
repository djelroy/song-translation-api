package org.djelroy.songtranslation.dao;

import org.djelroy.songtranslation.domain.Song;

public interface SongDao {

	void create(Song song);
	void delete(int id);
	Song get(int id);
	Song update(Song song);
}
