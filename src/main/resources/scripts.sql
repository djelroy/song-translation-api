CREATE TABLE songs (
	song_id INT NOT NULL AUTO_INCREMENT, 
	song_title VARCHAR(75) NOT NULL, 
	song_artist VARCHAR(50) NOT NULL, 
	song_lyrics VARCHAR(10000) NOT NULL,
	song_language CHAR(2) DEFAULT 'en' NOT NULL,
	PRIMARY KEY (song_id),
	INDEX song_title_idx (song_title),
	INDEX song_artist_idx (song_artist)	
) ENGINE=InnoDB; 

CREATE TABLE translations (
	translation_id INT NOT NULL AUTO_INCREMENT,
	song_id INT NOT NULL, 
	translation_language CHAR(2) NOT NULL, 
	translation_title VARCHAR(75) NOT NULL, 
	translation_lyrics VARCHAR(10000) NOT NULL,
	PRIMARY KEY (translation_id),
	FOREIGN KEY (song_id) REFERENCES songs(song_id)
) ENGINE=InnoDB; 

ALTER TABLE translations
ADD CONSTRAINT FK_songtranslation
FOREIGN KEY (song_id) REFERENCES songs(song_id);