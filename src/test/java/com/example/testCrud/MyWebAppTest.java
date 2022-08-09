package com.example.testCrud;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MyWebAppTest {
	@Autowired
	  private MockMvc mockMvc;

	  @Test
	  public void testSayHi() throws Exception {
	      this.mockMvc.perform(MockMvcRequestBuilders.get("/posts").param("","0","0"))
	                  .andExpect(MockMvcResultMatchers.status().isOk())
	                  .andExpect(MockMvcResultMatchers.model().attribute("msg", "Hi there, Joe."))
	                  .andExpect(MockMvcResultMatchers.view().name("posts/Index"))
	                  .andDo(MockMvcResultHandlers.print());
	  }
}
