package org.djelroy.songtranslation.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "songs", indexes = {@Index(columnList = "song_title"), @Index(columnList = "song_artist")})
public class Song {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "song_id")
	private Integer id;

	@NotBlank
	@Column(name = "song_title")
	private String title;

	@NotBlank
	@Column(name = "song_artist")
	private String artist;

	@NotBlank
	@Column(name = "song_lyrics")
	private String lyrics;
	
	@Column(name = "song_language")
	private String language;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Translation> translations = new ArrayList<>();;

	public Song() {}

	public Song(String title, String artist, String lyrics) {
		this(null, title, artist, lyrics, "en");
	}

	public Song(Integer id, String title, String artist, String lyrics, String language) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.lyrics = lyrics;
		this.language = language;
	}

	public void addTranslation(Translation translation) {
		translations.add(translation);
		translation.setSong(this);
	}
	
	public void removeTranslation(Translation translation) {
		translations.remove(translation);
		translation.setSong(null);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if (obj instanceof Song) {
			Song song = (Song) obj;

			boolean songIdsEquality = (id == null && song.id == null) || (id != null) ? id.equals(song.id)
					: song.id.equals(id);

			return songIdsEquality && song.getLanguage().equals(language) && song.getTitle().equals(title) && song.getArtist().equals(artist)
					&& song.getLyrics().equals(lyrics);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "Song [id=" + id + ", title=" + title + ", artist=" + artist + ", lyrics=" + lyrics + ", language="
				+ language + "]";
	}
}
