package com.vibo.GenAI.service;

import com.vibo.GenAI.dto.CourseRecomendRequestDto;
import com.vibo.GenAI.dto.CourseRecomendResponseDto;
import com.vibo.GenAI.dto.MessageDto;
import com.vibo.GenAI.dto.Response;
import org.springframework.stereotype.Component;

@Component
public interface ChatService {

    Response chat(MessageDto message);
    CourseRecomendResponseDto recommendCourse(CourseRecomendRequestDto courseRecomendRequestDto);
}
