package org.djelroy.songtranslation.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.djelroy.songtranslation.domain.Song;
import org.springframework.stereotype.Repository;

@Repository
public class JpaSongDao implements SongDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void create(Song song) {
		entityManager.persist(song);
	}

	@Override
	public void delete(int id) {
		Song song = entityManager.find(Song.class, id);

		if (song != null) {
			entityManager.remove(song);
		}
	}

	@Override
	public Song get(int id) {
		return entityManager.find(Song.class, id);
	}

	@Override
	public Song update(Song song) {
		Song managedSong = entityManager.find(Song.class, song.getId());
		
		if(managedSong == song) {
			return null;
		}
		
		managedSong.setArtist(song.getArtist());
		managedSong.setTitle(song.getTitle());
		managedSong.setLyrics(song.getLyrics());

		return managedSong;
	}
}
