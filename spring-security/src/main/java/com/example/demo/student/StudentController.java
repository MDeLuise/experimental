package com.example.demo.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;


import java.util.Set;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {

    private static final Set<Student> students = new HashSet<>(Arrays.asList(
        new Student(1, "student name 1", "student surname 1"),
        new Student(2, "student name 2", "student surname 2"),
        new Student(3, "student name 3", "student surname 3")
    ));


    @GetMapping
    public Collection<Student> getAll() {
        return students;
    }


    @GetMapping("/{studentId}")
    public Student get(@PathVariable int studentId) {
        return students.stream()
            .filter(student -> student.getId() == studentId)
            .findFirst()
            .orElseThrow(NullPointerException::new);
    }
}
