package com.example.demo.student;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/management/api/v1/student")
public class StudentManagementController {

    private static final Set<Student> students = new HashSet<>(Arrays.asList(
        new Student(1, "student name 1", "student surname 1"),
        new Student(2, "student name 2", "student surname 2"),
        new Student(3, "student name 3", "student surname 3")
    ));


    @GetMapping
    public Collection<Student> getAll() {
        return students;
    }


    @PostMapping
    public void registerStudent(@RequestParam Student student) {
        System.out.println(student);
    }


    @DeleteMapping("/{id}")
    public void removeStudent(@PathVariable int id) {
        System.out.println(id);
    }


    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestParam Student student) {
        System.out.printf("%s%s%n", id, student);
    }
}
