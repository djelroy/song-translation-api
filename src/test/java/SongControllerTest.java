import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
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

    // @Test
	public void shouldReturnHttpCreatedStatusWhenPostingASong() throws Exception {
		final Song song = new Song("The fragrance of the Self", "Ramana Maharshi", "You are the effulgent Self");
		final String songJson = mapper.writeValueAsString(song);
		
		mockMvc.perform(post("/songs").content(songJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
	}
	
//    @Test
	public void shouldReturnSameSongWithNewIdWhenPostingASong() throws Exception {
		
		final Song songInput = new Song("Existence", "Dj Elroy", "Existence is the flower of miracles");
		final String songToJson = mapper.writeValueAsString(songInput);
		
		final MvcResult result = mockMvc.perform(post("/songs").content(songToJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		
		final Song songOutput = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Song>() {});
		
		assertTrue("Returned song's title should be the same as the created one's", songOutput.getTitle().equals(songInput.getTitle()));
		assertTrue("Returned song's artist should be the same as the created one's", songOutput.getArtist().equals(songInput.getArtist()));
		assertTrue("Returned song's lyrics should be the same as the created one's", songOutput.getLyrics().equals(songInput.getLyrics()));
		assertTrue("The returned song's id shouldn't be null", songOutput.getId() != null);
	}
    
//	@Test
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

//	@Test
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
			assertTrue("All the retrieved songs should have title = " + songInput.getTitle(),
					songs.stream().allMatch(s -> StringUtils.equalsIgnoreCase(s.getTitle(), songInput.getTitle())));
		}
	}

//	@Test
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
			assertTrue("All the retrieved songs should have artist = " + songInput.getArtist(),
					songs.stream().allMatch(s -> StringUtils.equalsIgnoreCase(s.getArtist(), songInput.getArtist())));
		}
	}

//	@Test
	public void shouldReturn100SongsMax() throws Exception {

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
