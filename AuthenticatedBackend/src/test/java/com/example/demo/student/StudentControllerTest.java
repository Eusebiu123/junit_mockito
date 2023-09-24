package com.example.demo.student;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @MockBean
    StudentService studentService;

    @Autowired
    MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    Student s1= new Student(1L,"sebi","sebi@1234",Gender.MALE);
    Student s2= new Student("sebi","sebi@12345",Gender.MALE);
    Student s3= new Student("sebi","sebi@123456",Gender.MALE);



    @Test
    public void addStudent() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"jamila@gmail.com\",\"name\":\"sabi\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(studentService).addStudent(any(Student.class));


    }
    @Test
    public void getAllStudents() throws Exception {
            List<Student> students = new ArrayList<>(Arrays.asList(s1,s2,s3));
        studentService.addStudent(s1);studentService.addStudent(s2);studentService.addStudent(s3);
        Mockito.when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/students/get")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value("sebi@1234"))
                .andExpect(status().isOk());
        verify(studentService).getAllStudents();
    }

    @Test
    public void deleteStudent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(studentService).deleteStudent(s1.getId());
    }
}
