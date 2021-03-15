package com.skorobahatko.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.LectureService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.skorobahatko.university.util.TestUtils.getTestLectureWithCourseId;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LectureRestController.class)
class LectureRestControllerIT {

    @MockBean
    private LectureService lectureService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetAllLectures() throws Exception {
        Lecture lecture = getTestLectureWithCourseId(1);
        List<Lecture> lectures = List.of(lecture);

        when(lectureService.getAll()).thenReturn(lectures);

        mockMvc.perform(get("/api/lectures")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(lecture.getName())))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(lectureService, times(1)).getAll();
        verifyNoMoreInteractions(lectureService);
    }

    @Test
    void testAddLecture() throws Exception {
        Lecture lecture = getTestLectureWithCourseId(1);

        mockMvc.perform(post("/api/lectures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lecture)))
                .andExpect(status().isCreated());

        verify(lectureService, times(1)).add(lecture);
        verifyNoMoreInteractions(lectureService);
    }

    @Test
    void testGetLectureById() throws Exception {
        Lecture lecture = getTestLectureWithCourseId(1);

        when(lectureService.getById(0)).thenReturn(lecture);

        mockMvc.perform(get("/api/lectures/{id}", 0))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(lecture)))
                .andReturn();

        verify(lectureService, times(1)).getById(0);
        verifyNoMoreInteractions(lectureService);
    }

    @Test
    void testUpdateLecture() throws Exception {
        Lecture lecture = getTestLectureWithCourseId(1);

        mockMvc.perform(put("/api/lectures/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lecture)))
                .andExpect(status().isOk());

        verify(lectureService, times(1)).update(lecture);
        verifyNoMoreInteractions(lectureService);
    }

    @Test
    void testDeleteLectureById() throws Exception {
        mockMvc.perform(delete("/api/lectures/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(lectureService, times(1)).removeById(1);
        verifyNoMoreInteractions(lectureService);
    }

}