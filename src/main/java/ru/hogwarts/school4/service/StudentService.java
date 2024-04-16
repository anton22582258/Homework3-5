package ru.hogwarts.school4.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school4.exception.StudentNotFoundException;
import ru.hogwarts.school4.model.Faculty;
import ru.hogwarts.school4.model.Student;
import ru.hogwarts.school4.repository.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }


    public Student getById(Long id) {
        return studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    public Student update(Long id, Student student) {
        Student existingStudent = getById(id);
        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());
        studentRepository.save(existingStudent);
        return existingStudent;
    }

    public Student delete(Long id) {
        Student existingStudent = getById(id);
        studentRepository.delete(existingStudent);
        return existingStudent;
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> getByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        return studentRepository.findAllByAgeBetween(min, max);
    }
    public Faculty getFacultyById(Long studentId) {
        return studentRepository.findById(studentId).get().getFaculty();
    }

}