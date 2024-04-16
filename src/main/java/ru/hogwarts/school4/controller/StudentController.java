package ru.hogwarts.school4.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school4.model.Faculty;
import ru.hogwarts.school4.model.Student;
import ru.hogwarts.school4.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Collection<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable("id") Long id) {
        return studentService.getById(id);
    }

    @GetMapping("/filtered")
    public Collection<Student> getByAge(@RequestParam("age") int age) {
        return studentService.getByAge(age);
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

   // @DeleteMapping("/{id}")
    //public void delete(@PathVariable("id") Long id) {
    //    studentService.remove(id);
   // }
    @DeleteMapping("{id}")
    @Operation(summary = "Delete student by id")
    public void delStudent(@PathVariable Long id) {
        if (id != null)
            studentService.delete(id);
    }

    @GetMapping("/filteredByBetween")
    public Collection<Student> filtered(@RequestParam int min, @RequestParam int max) {
        return studentService.getByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudent(@PathVariable Long id) {
        return studentService.getById(id).getFaculty();
    }
}