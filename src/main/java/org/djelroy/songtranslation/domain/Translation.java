package org.djelroy.songtranslation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "translations")
public class Translation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "translation_id")
	private Integer id;
	
	@NotBlank
	@Column(name = "translation_language")
	private String language;
	
	@NotBlank
	@Column(name = "translation_title")
	private String title;
	
	@NotBlank
	@Column(name = "translation_lyrics")
	private String lyrics;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Song.class)
	@JoinColumn(name = "song_id", nullable = false)
	private Song song;
	
	
	public Translation() {
		super();
	}

	public Translation(String language, String title, String lyrics) {
		this(language, title, lyrics, null);
	}

	public Translation(String language, String title, String lyrics, Song song) {
		this(null, language, title, lyrics, song);
	}

	protected Translation(Integer id, String language, String title, String lyrics, Song song) {
		this.id = id;
		this.language = language;
		this.title = title;
		this.lyrics = lyrics;
		this.song = song;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if (obj instanceof Translation) {
			Translation t = (Translation) obj;
		
			return id.equals(t.id) && title.equals(t.title);
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "Translation [id=" + id + ", language=" + language + ", title=" + title + ", lyrics=" + lyrics + "]";
	}
}
