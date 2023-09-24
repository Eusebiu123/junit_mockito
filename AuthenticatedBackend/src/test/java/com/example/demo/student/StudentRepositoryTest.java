package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {
    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void itShouldCheckIfStudentEmailExists() {
        // given
        String email="jamila@gmail.com";
        String name="Jamila";
        Student student = new Student(
                name,email,Gender.FEMALE
        );
        underTest.save(student);
        //when
        Boolean expected = underTest.selectExistsEmail(email);
        //then
        assertThat(expected).isTrue();
    }
    @Test
    void itShouldCheckWhenStudentEmailDoesNotExist() {
        // given
        String email="jamila@gmail.com";
        //when
        Boolean expected = underTest.selectExistsEmail(email);
        //then
        assertThat(expected).isFalse();
    }
}