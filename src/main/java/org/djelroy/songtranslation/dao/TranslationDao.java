package org.djelroy.songtranslation.dao;

import org.djelroy.songtranslation.domain.Translation;

public interface TranslationDao {

	void create(Translation translation);
	void delete(int id);
	Translation get(int id);
	Translation update(Translation translation);
}
