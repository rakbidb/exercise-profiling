package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author muhammad.khadafi
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<StudentCourse> getAllStudentsWithCourses() {
        List<Student> students = studentRepository.findAll();
        List<StudentCourse> allStudentCourses = studentCourseRepository.findAll();

        Map<Long, List<StudentCourse>> studentCoursesByStudentId = allStudentCourses.stream()
                .collect(Collectors.groupingBy(sc -> sc.getStudent().getId()));

        List<StudentCourse> studentCourses = new ArrayList<>();
        for (Student student : students) {
            List<StudentCourse> studentCoursesByStudent = studentCoursesByStudentId.get(student.getId());
            if (studentCoursesByStudent != null) {
                for (StudentCourse studentCourseByStudent : studentCoursesByStudent) {
                    StudentCourse studentCourse = new StudentCourse();
                    studentCourse.setStudent(student);
                    studentCourse.setCourse(studentCourseByStudent.getCourse());
                    studentCourses.add(studentCourse);
                }
            }
        }
        return studentCourses;
    }

    public Optional<Student> findStudentWithHighestGpa() {
        List<Student> students = studentRepository.findAll();
        Student highestGpaStudent = null;
        double highestGpa = 0.0;
        for (Student student : students) {
            if (student.getGpa() > highestGpa) {
                highestGpa = student.getGpa();
                highestGpaStudent = student;
            }
        }
        return Optional.ofNullable(highestGpaStudent);
    }

    public String joinStudentNames() {
        List<Student> students = studentRepository.findAll();
        StringBuilder result = new StringBuilder();
        for (Student student : students) {
            result.append(student.getName()).append(", ");
        }
        if (!result.isEmpty()) {
            result.setLength(result.length() - 2);
        }
        return result.toString();
    }
}

