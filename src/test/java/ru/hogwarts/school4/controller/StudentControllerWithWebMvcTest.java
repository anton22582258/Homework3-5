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
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ru.hogwarts.school4.model.Student;
import ru.hogwarts.school4.repository.StudentRepository;
import ru.hogwarts.school4.service.StudentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(StudentController.class)
public class StudentControllerWithWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;
    @SpyBean
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;

    Long id = 1L;
    String name = "Robot Garry";
    int age = 10;

    JSONObject studentObject = new JSONObject();
    Student student = new Student();

    private void initializingVariablesForTests() throws JSONException {
        studentObject.put("id", id);
        studentObject.put("name", name);
        studentObject.put("age", age);

        student.setId(id);
        student.setName(name);
        student.setAge(age);
    }

    @Test
    public void createStudentTest() throws Exception {
        initializingVariablesForTests();

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void getByIdStudentTest() throws Exception {
        initializingVariablesForTests();

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andDo(print());
    }

    @Test
    public void getAllStudentTest() throws Exception {

        List<Student> listStudent = new ArrayList<>(
                Arrays.asList(
                        new Student("Spring Boot @WebMvcTest 1", 1),
                        new Student("Spring Boot @WebMvcTest 2", 2),
                        new Student("Spring Boot @WebMvcTest 3", 3)));

        when(studentRepository.findAll()).thenReturn(listStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(listStudent.size()))
                .andDo(print());
    }

    @Test
    public void updateStudentTest() throws Exception {
        initializingVariablesForTests();

        Student updateStudent = new Student("new" + name, 1 + age);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(updateStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //    .content(objectMapper.writeValueAsString(updateStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateStudent.getId()))
                .andExpect(jsonPath("$.name").value(updateStudent.getName()))
                .andExpect(jsonPath("$.age").value(updateStudent.getAge()))
                .andDo(print());
    }


    @Test
    public void deleteStudentTest() throws Exception {
        initializingVariablesForTests();

        doNothing().when(studentRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", id))
                .andDo(print());

    }
}

