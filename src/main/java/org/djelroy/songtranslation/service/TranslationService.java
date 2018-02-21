package org.djelroy.songtranslation.service;

import org.djelroy.songtranslation.domain.Translation;

public interface TranslationService {

	boolean create(Translation translation);
	boolean delete(int id);
	Translation get(int id);
	Translation update(Translation translation);
}
