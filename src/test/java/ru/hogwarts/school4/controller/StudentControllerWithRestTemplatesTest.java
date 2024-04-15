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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerWithRestTemplatesTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoadsForStudentController() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student("name" + generateRandomString(), 50);

        ResponseEntity<Student> newStudentRs =
                restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);

        Student newStudentRsBody = newStudentRs.getBody();

        assertThat(newStudentRsBody.getName()).isEqualTo(student.getName());
        assertThat(newStudentRsBody.getAge()).isEqualTo(student.getAge());
    }

    @Test
    public void testGetStudentById() {
        Student student = new Student("name" + generateRandomString(), 50);

        ResponseEntity<Student> newStudentRs =
                restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);

        Student newStudentFromRepForTest =
                restTemplate.getForObject("http://localhost:" + port + "/student/" + newStudentRs.getBody().getId(), Student.class);

        assertEquals(newStudentRs.getBody().getId(), newStudentFromRepForTest.getId());
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student("name" + generateRandomString(), 50);

        ResponseEntity<Student> newStudentRs =
                restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);

        Student newStudentRsBody = newStudentRs.getBody();

        String newName = "new Update Name";
        newStudentRsBody.setName(newName);

        restTemplate.put("http://localhost:" + port + "/student", newStudentRsBody, Student.class);

        Student studentForTest = restTemplate.getForObject(
                "http://localhost:" + port + "/student/" + newStudentRsBody.getId(), Student.class);

        assertEquals(newName, studentForTest.getName());
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student("name" + generateRandomString(), 50);

        ResponseEntity<Student> newStudentRs =
                restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);

        Student newStudentRsBody = newStudentRs.getBody();

        Long id = newStudentRsBody.getId();

        restTemplate.delete("http://localhost:" + port + "/student/" + id);

        Student studentForTest = restTemplate.getForObject(
                "http://localhost:" + port + "/student/" + id, Student.class);

        assertNull(studentForTest.getId());
        assertNull(studentForTest.getName());
    }

    private static String generateRandomString() {
        int minValue = 1;
        int maxValue = 50_000;
        Integer randomValue1 = minValue + (int) (Math.random() * (maxValue - minValue + 1));
        Integer randomValue2 = minValue + (int) (Math.random() * (maxValue - minValue + 1));

        return randomValue1.toString() + randomValue2;
    }

}
