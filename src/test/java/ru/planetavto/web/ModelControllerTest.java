package ru.planetavto.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import ru.planetavto.advertsment.Model;
import ru.planetavto.presistent.ModelRepository;

public class ModelControllerTest {
	
	 @Test
	  public void shouldShowValidViewWhenModel_1() throws Exception {
	    Model expectedModel = new Model();
	    ModelRepository mockRepository = mock(ModelRepository.class);
	    when(mockRepository.findById(1l)).thenReturn(expectedModel);
	    
	    ModelController controller = new ModelController(mockRepository);
	    MockMvc mockMvc = standaloneSetup(controller).build();

	    mockMvc.perform(get("/model/1"))
	      .andExpect(view().name("model/modelUnitForm"))
	      .andExpect(model().attributeExists("model"))
	      .andExpect(model().attribute("model", expectedModel));
	  }
	 
	 @Test
	  public void shouldShowNewModel() throws Exception {
	    ModelRepository mockRepository = mock(ModelRepository.class);
	    ModelController controller = new ModelController(mockRepository);
	    MockMvc mockMvc = standaloneSetup(controller).build();
	    mockMvc.perform(get("/model/new"))
	           .andExpect(view().name("model/modelUnitForm"));
	  }
}