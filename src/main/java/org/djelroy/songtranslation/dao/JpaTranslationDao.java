package org.djelroy.songtranslation.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.djelroy.songtranslation.domain.Translation;
import org.springframework.stereotype.Repository;

@Repository
public class JpaTranslationDao implements TranslationDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void create(Translation translation) {
		entityManager.persist(translation);
	}

	@Override
	public void delete(int id) {
		Translation translation =  entityManager.find(Translation.class, id);
		
		if(translation != null) {
			entityManager.remove(translation);
		}
	}

	@Override
	public Translation get(int id) {
		return entityManager.find(Translation.class, id);
	}

	@Override
	public Translation update(Translation translation) {
		Translation managedTranslation = entityManager.find(Translation.class, translation.getId());
		
		if(managedTranslation == null) {
			return null;
		}
		
		managedTranslation.setLanguage(translation.getLanguage());
		managedTranslation.setTitle(translation.getTitle());
		managedTranslation.setLyrics(translation.getLyrics());
		
		return managedTranslation;
	}
}
