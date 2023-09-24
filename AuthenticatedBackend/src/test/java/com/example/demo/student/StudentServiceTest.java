package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    private StudentService underTest;

    @BeforeEach
    void setUp() {

        underTest = new StudentService(studentRepository);
    }


    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        //given
        String email="jamila@gmail.com";
        String name="Jamila";
        Student student = new Student(
                name,email,Gender.FEMALE
        );
        //when
        underTest.addStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }
    @Test
    void willThrowWhenEmailIsTaken() {
        //given
        String email="jamila@gmail.com";
        String name="Jamila";
        Student student = new Student(
                name,email,Gender.FEMALE
        );
        given(studentRepository.selectExistsEmail(anyString())).willReturn(true);
        //when
        //then
        assertThatThrownBy(()-> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
        verify(studentRepository,never()).save(any());
    }
    @Test
    void canDeleteStudent() {
        //given
        String email="jamila@gmail.com";
        String name="Jamila";
        Long id=1L;
        Student student = new Student(id,
                name,email,Gender.FEMALE
        );
        underTest.addStudent(student);
        given(studentRepository.existsById(id)).willReturn(true);
        //when

        underTest.deleteStudent(student.getId());
        //then

        verify(studentRepository,times(1)).deleteById(id);

    }
    @Test
    void canDeleteStudentThrow() {
        //given
        String email="jamila@gmail.com";
        String name="Jamila";
        Long id=1L;
        Student student = new Student(id,
                name,email,Gender.FEMALE
        );
        underTest.addStudent(student);
        given(studentRepository.existsById(student.getId())).willReturn(false);
        //when

        assertThatThrownBy(()-> underTest.deleteStudent(student.getId()))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + student.getId() + " does not exists");
        verify(studentRepository,never()).delete(any());

    }


}