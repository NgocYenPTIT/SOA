package com.example.course_service.controller;

import com.example.course_service.model.Course;
import com.example.course_service.security.JwtTokenProvider;
import com.example.course_service.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
public class CourseController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CourseService courseService;

    public CourseController(JwtTokenProvider jwtTokenProvider,CourseService courseService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.courseService = courseService;
    }

    @GetMapping("/semester/{id}")
    public ResponseEntity<?> getCourse(@PathVariable("id") Long  semesterId , HttpServletRequest request ) {
        return  ResponseEntity.ok(this.courseService.getListCourse(semesterId));
    }

    @GetMapping("course/{id}")
    public ResponseEntity<Course> getDetail(@PathVariable Long id) {
        Course course = courseService.getDetail(id);
        return ResponseEntity.ok(course);
    }
}