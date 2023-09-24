package com.example.demo.student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class StudentController2 {

        @InjectMocks
        private StudentController studentController;

        @Mock
        private StudentService studentService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        void testGetAllStudents() {
            List<Student> students = new ArrayList<>();
            students.add(new Student(1L,"sebi","sebi@1234",Gender.MALE));
            students.add(new Student(2L, "Alice","sebi@1234",Gender.MALE));

            when(studentService.getAllStudents()).thenReturn(students);

            List<Student> result = studentController.getAllStudents();

            assertEquals(2, result.size());
                assertEquals("sebi", result.get(0).getName());
            assertEquals("sebi@1234", result.get(1).getEmail());
        }

        @Test
        void testAddStudent() {
            Student studentToAdd = new Student(3L, "Bob","sebi@1234",Gender.MALE);
            when(studentService.addStudent(studentToAdd)).thenReturn(studentToAdd);

            ResponseEntity responseEntity = studentController.addStudent(studentToAdd);

            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals(studentToAdd, responseEntity.getBody());
        }

        @Test
        void testDeleteStudent() {
            Long studentIdToDelete = 1L;

            ResponseEntity responseEntity = studentController.deleteStudent(studentIdToDelete);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(studentService, times(1)).deleteStudent(studentIdToDelete);
        }
    }


