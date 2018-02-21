package org.djelroy.songtranslation.service;

import javax.persistence.TransactionRequiredException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.djelroy.songtranslation.dao.SongDao;
import org.djelroy.songtranslation.domain.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenericSongService implements SongService {

	private final static Logger logger = LogManager.getLogger(GenericSongService.class);
	
	@Autowired
	private SongDao songDao;

	public GenericSongService(SongDao dao) {
		this.songDao = dao;
	}

	@Override
	public boolean create(Song song) {
		if(Strings.isBlank(song.getLanguage())) {
			song.setLanguage("en");
		}
		
		songDao.create(song);
		return song.getId() != null;
	}

	@Override
	public boolean delete(int id) {
		try {
			songDao.delete(id);
			return true;
		}
		catch(IllegalArgumentException | TransactionRequiredException e) {
			logger.warn(e.getStackTrace());
			return false;
		}
	}

	@Override
	public Song get(int id) {
		return songDao.get(id);
	}

	@Override
	public Song update(Song song) {
		return songDao.update(song);
	}

}
