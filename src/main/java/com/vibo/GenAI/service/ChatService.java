package com.vibo.GenAI.service;

import com.vibo.GenAI.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ChatService {

    Response chat(MessageDto message);
    CourseRecomendResponseDto recommendCourse(CourseRecomendRequestDto courseRecomendRequestDto);
    List<String> listOpConverting(String message);
    Movie beanOpConverting(String message);

    List<Movie> listOfBeanOpConverting(String message);
}
