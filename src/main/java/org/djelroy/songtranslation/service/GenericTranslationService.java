package org.djelroy.songtranslation.service;

import javax.persistence.TransactionRequiredException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.djelroy.songtranslation.dao.TranslationDao;
import org.djelroy.songtranslation.domain.Translation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenericTranslationService implements TranslationService {

	private final static Logger logger = LogManager.getLogger(GenericTranslationService.class);
	
	@Autowired
	private TranslationDao translationDao;
	
	public GenericTranslationService(TranslationDao translationDao) {
		this.translationDao = translationDao;
	}

	@Override
	public boolean create(Translation translation) {
		translationDao.create(translation);
		return translation.getId() != null;
	}

	@Override
	public boolean delete(int id) {
		try {
			translationDao.delete(id);
			return true;
		}
		catch(IllegalArgumentException | TransactionRequiredException e) {
			logger.warn(e.getStackTrace());
			return false;
		}
		
	}

	@Override
	public Translation get(int id) {
		return translationDao.get(id);
	}

	@Override
	public Translation update(Translation translation) {
		return translationDao.update(translation);
	}

}
