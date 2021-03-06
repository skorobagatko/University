package com.skorobahatko.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
class CourseControllerSystemTest {
	
	@Autowired
	private WebApplicationContext context;

    private MockMvc mockMvc;
    
    @BeforeEach
    public void init() {
    	mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
    @Test
	void testGetAllCourses() throws Exception {
		mockMvc.perform(get("/courses")
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")))
				.andExpect(view().name("courses/all"))
				.andExpect(model().attributeExists("courses"));
	}

}
