
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletContext;

import org.djelroy.songtranslation.configuration.AppConfig;
import org.djelroy.songtranslation.controller.SongController;
import org.djelroy.songtranslation.domain.Song;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class SongControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void ifServletContextPresentThenItProvidesBeanForSongController() {
		ServletContext servletContext = wac.getServletContext();

		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean(SongController.class));
	}

    @Test
	public void shouldReturnHttpCreatedStatusWhenPostingASong() throws Exception {
		final Song song = new Song("The fragrance of the Self", "Ramana Maharshi", "You are the effulgent Self");
		final String songJson = mapper.writeValueAsString(song);
		
		mockMvc.perform(post("/songs").content(songJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
	}
	
    @Test
	public void shouldReturnSameSongWithNewIdWhenPostingASong() throws Exception {
		
		final Song songInput = new Song("Existence", "Dj Elroy", "Existence is the flower of miracles");
		final String songToJson = mapper.writeValueAsString(songInput);
		
		final MvcResult result = mockMvc.perform(post("/songs").content(songToJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		
		final Song songOutput = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Song>() {});
		
		assertThat(songOutput.getTitle(), equalTo(songInput.getTitle()));
		assertThat(songOutput.getArtist(), equalTo(songInput.getArtist()));
		assertThat(songOutput.getLyrics(), equalTo(songInput.getLyrics()));
		assertThat(songOutput.getId(), notNullValue());
	}
    
	@Test
	public void shouldReturnNotFound() throws Exception {
		String title = URLEncoder.encode("TITLE_DONT_EXIST_GGZEZ000", StandardCharsets.UTF_8.name());
		String artist = URLEncoder.encode("ARTIST_DONT_EXIST_GGZEZ000", StandardCharsets.UTF_8.name());

		mockMvc.perform(get("/songs?title=" + title).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		mockMvc.perform(get("/songs?artist=" + artist).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		mockMvc.perform(get("/songs?title=" + title + "&artist=" + artist).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

	}

	@Test
	public void shouldReturnSongsOnlyWithSameTitle() throws Exception {

		final Song songInput = new Song("The Best Title", "DJ Elroy", "My lyrics are on fire");
		final String songToJson = mapper.writeValueAsString(songInput);

		mockMvc.perform(post("/songs").content(songToJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

		final MvcResult result = mockMvc
				.perform(get("/songs?title=" + URLEncoder.encode(songInput.getTitle(), StandardCharsets.UTF_8.name()))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		final List<Song> songs = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Song>>() {
				});

		if (songs != null) {
			songs.stream()
					.forEach(s -> assertThat(s.getTitle().toLowerCase(), equalTo(songInput.getTitle().toLowerCase())));
		}
	}

	@Test
	public void shouldReturnSongsOnlyWithSameArtist() throws Exception {

		final Song songInput = new Song("You And I", "DJ Elroy", "You and I are one");
		final String songToJson = mapper.writeValueAsString(songInput);

		mockMvc.perform(post("/songs").content(songToJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

		final MvcResult result = mockMvc
				.perform(get("/songs?artist=" + URLEncoder.encode(songInput.getArtist(), StandardCharsets.UTF_8.name()))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		final List<Song> songs = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Song>>() {
				});

		if (songs != null) {
			songs.stream().forEach(
					s -> assertThat(s.getArtist().toLowerCase(), equalTo(songInput.getArtist().toLowerCase())));
		}
	}

	@Test
	public void shouldReturn100SongsMax() throws Exception {

		// Creating 105 songs
		for(int i = 0; i < 105; i++) {
			final Song song = new Song("The fragrance of the Self" + i, "Ramana Maharshi" + i, "You are the effulgent Self" + i);
			final String songJson = mapper.writeValueAsString(song);
			mockMvc.perform(post("/songs").content(songJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		}
		
		final MvcResult result = mockMvc.perform(get("/songs").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		final List<Song> songs = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Song>>() {
				});

		if (songs != null) {
			assertTrue("Number of retrieved songs shouldn't be more than the max = 100", songs.size() <= 100);
		}
	}
}
