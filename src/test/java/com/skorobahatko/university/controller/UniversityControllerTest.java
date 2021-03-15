package com.skorobahatko.university.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class UniversityControllerTest {
	
	private UniversityController universityController;
	
	@BeforeEach
	public void init() {
		universityController = new UniversityController();
	}
	
	@Test
	void indexViewIsShownForEmptyPathRequested() {
		assertThat(universityController.index(), equalTo("index"));
	}

}
