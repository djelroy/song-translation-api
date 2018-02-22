import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		Song song = new Song("The fragrance of the Self", "Ramana Maharshi", "You are the effulgent Self");
		String songJson = mapper.writeValueAsString(song);
		
		mockMvc.perform(post("/songs").content(songJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
	}
	
	@Test
	public void shouldReturnSameSongWithNewIdWhenPostingASong() throws Exception {
		
		Song songInput = new Song("Existence", "Dj Elroy", "Existence is the flower of miracles");
		String songToJson = mapper.writeValueAsString(songInput);
		
		MvcResult result = mockMvc.perform(post("/songs").content(songToJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		
		Song songOutput = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Song>() {});
		
		assertTrue("Returned song's title should be the same as the created one's", songOutput.getTitle().equals(songInput.getTitle()));
		assertTrue("Returned song's artist should be the same as the created one's", songOutput.getArtist().equals(songInput.getArtist()));
		assertTrue("Returned song's lyrics should be the same as the created one's", songOutput.getLyrics().equals(songInput.getLyrics()));
		assertTrue("The returned song's id shouldn't be null", songOutput.getId() != null);
	}
}
