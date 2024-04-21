package ru.hogwarts.school4.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ru.hogwarts.school4.model.Faculty;
import ru.hogwarts.school4.repository.FacultyRepository;
import ru.hogwarts.school4.service.FacultyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWithWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private FacultyService facultyService;
    @InjectMocks
    private FacultyController facultyController;

    private Long id = 1L;
    private String name = "math";
    private String color = "Black";

    private JSONObject facultyObject = new JSONObject();

    private Faculty faculty = new Faculty(name, color);


    private void initializingVariablesForTests() throws JSONException {
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        faculty.setId(id);
    }

    @Test
    public void createFacultyTest() throws Exception {
        initializingVariablesForTests();

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color))
                .andDo(print());
    }

    @Test
    public void getByIdFacultyTest() throws Exception {
        initializingVariablesForTests();

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color))
                .andDo(print());
    }

    @Test
    public void getAllFacultyTest() throws Exception {

        List<Faculty> listFaculty = new ArrayList<>(
                Arrays.asList(
                        new Faculty("Spring Boot @WebMvcTest 1", "Description 1"),
                        new Faculty("Spring Boot @WebMvcTest 2", "Description 2"),
                        new Faculty("Spring Boot @WebMvcTest 3", "Description 3")));

        when(facultyRepository.findAll()).thenReturn(listFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(listFaculty.size()))
                .andDo(print());
    }

    @Test
    public void updateFacultyTest() throws Exception {
        initializingVariablesForTests();

        Faculty updateFaculty = new Faculty("new" + name, "new" + color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updateFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //    .content(objectMapper.writeValueAsString(updateFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateFaculty.getId()))
                .andExpect(jsonPath("$.name").value(updateFaculty.getName()))
                .andExpect(jsonPath("$.color").value(updateFaculty.getColor()))
                .andDo(print());
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        initializingVariablesForTests();

        doNothing().when(facultyRepository).deleteById(id);

        mockMvc.perform(delete("/faculty/{id}", id))
                .andDo(print());

    }
}
