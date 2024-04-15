package ru.hogwarts.school4.controller;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import ru.hogwarts.school4.model.Faculty;
import ru.hogwarts.school4.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerWithRestTemplatesTest {
    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoadsForFacultyController() {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testGetFaculty() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject(
                        "http://localhost:" + port + "/faculty", String.class))
                .isNotNull();
    }

    @Test
    public void testCreateFaculty() {
        Faculty faculty = new Faculty("faculty" + generateRandomString(), "color" + generateRandomString());

        ResponseEntity<Faculty> newFacultyRs =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty newFacultyRsBody = newFacultyRs.getBody();

        assertThat(newFacultyRsBody.getName()).isEqualTo(faculty.getName());
        assertThat(newFacultyRsBody.getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    public void testGetFacultyById() {
        Faculty faculty = new Faculty("faculty" + generateRandomString(), "color" + generateRandomString());

        ResponseEntity<Faculty> newFacultyRs =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty newFacultyFromRepForTest =
                restTemplate.getForObject("http://localhost:" + port + "/faculty/" + newFacultyRs.getBody().getId(), Faculty.class);

        assertEquals(newFacultyRs.getBody().getId(), newFacultyFromRepForTest.getId());
    }

    @Test
    public void testGetListStudentsByIdFaculty() {
        Faculty[] facultyList =
                restTemplate.getForObject("http://localhost:" + port + "/faculty", Faculty[].class);

        Collection<Student> studentList = null;

        for (Faculty faculty : facultyList) {
            Long id = faculty.getId();
            studentList =
                    restTemplate.getForObject("http://localhost:" + port + "/faculty/" + id + "/students",
                            Collection.class);

            if (studentList != null) break;
        }

        assertNotNull(studentList);
    }

    @Test
    public void testUpdateFaculty() {
        Faculty faculty = new Faculty("faculty" + generateRandomString(), "color" + generateRandomString());

        ResponseEntity<Faculty> newFacultyRs =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty newFacultyRsBody = newFacultyRs.getBody();

        String newName = "faculty test";
        String newColor = "color test";
        newFacultyRsBody.setColor(newColor);
        newFacultyRsBody.setName(newName);

        restTemplate.put("http://localhost:" + port + "/faculty", newFacultyRsBody, Faculty.class);

        Faculty facultyForTest = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + newFacultyRsBody.getId(), Faculty.class);
        assertEquals(newName, facultyForTest.getName());
        assertEquals(newColor, facultyForTest.getColor());
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty("faculty" + generateRandomString(), "color" + generateRandomString());

        ResponseEntity<Faculty> newFacultyRs =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty newFacultyRsBody = newFacultyRs.getBody();

        Long id = newFacultyRsBody.getId();

        restTemplate.delete("http://localhost:" + port + "/faculty/" + id);

        Faculty facultyForTest = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + id, Faculty.class);

        assertNull(facultyForTest.getId());
        assertNull(facultyForTest.getName());
        assertNull(facultyForTest.getColor());
    }

    private static String generateRandomString() {
        int minValue = 1;
        int maxValue = 50_00;
        Integer randomValue1 = minValue + (int) (Math.random() * (maxValue - minValue + 1));
        Integer randomValue2 = minValue + (int) (Math.random() * (maxValue - minValue + 1));

        return randomValue1.toString() + randomValue2;
    }

}
