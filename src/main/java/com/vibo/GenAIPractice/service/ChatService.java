package com.vibo.GenAIPractice.service;

import com.vibo.GenAIPractice.dto.CourseRecomendRequestDto;
import com.vibo.GenAIPractice.dto.CourseRecomendResponseDto;
import com.vibo.GenAIPractice.dto.MessageDto;
import com.vibo.GenAIPractice.dto.Response;
import org.springframework.stereotype.Component;

@Component
public interface ChatService {

    Response chat(MessageDto message);
    CourseRecomendResponseDto recommendCourse(CourseRecomendRequestDto courseRecomendRequestDto);
}
