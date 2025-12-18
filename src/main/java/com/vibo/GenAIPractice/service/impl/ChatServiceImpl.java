package com.vibo.GenAIPractice.service.impl;


import com.vibo.GenAIPractice.dto.*;
import com.vibo.GenAIPractice.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    //      for multiple model use
    //    public OllamaChatController(OllamaChatModel ollamaChatModel) {
    //        this.chatClient = ChatClient.create(ollamaChatModel);
    //    }

    @Override
    public Response chat(MessageDto message){

//        ChatResponse chatResponse = chatClient
//                .prompt(message.getMessage())
//                .call()
//                .chatResponse(); // to get Metadata
//
//                String response = chatResponse
//                        .getResult()
//                        .getOutput()
//                        .getText();  //alter for .call().content()
//
//        if (chatResponse != null) {
//            System.out.println(chatResponse.getMetadata().getModel());
//        }


        String result = chatClient.prompt().user(message.getMessage()).call().content();

        Response response =new Response();
        response.setResponse(result);

        return response;
    }

    public CourseRecomendResponseDto recommendCourse(CourseRecomendRequestDto courseRecomendRequestDto){
        String template = """
                You are an API, not a chat assistant.
                I am a {level} level and
                I want to learn about the topic of {topic}.
                Recommend me best course to learn in the {platform}.
                
                Format exactly like this:

                Course Title: <title>
                Rating: <rating>
                Duration: <duration>
                Reason: <short reason>
                and also dont use * ** "\n" new line symbol
                and keep short and simple
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt=promptTemplate.create(Map.of("topic", courseRecomendRequestDto.getTopic(),"level", courseRecomendRequestDto.getLevel(),"platform", courseRecomendRequestDto.getPlatform()));

        String result= chatClient.prompt(prompt).call().content();
        CourseRecomendResponseDto response =new CourseRecomendResponseDto();
        response.setMessage(result);
        return response;
    }


}
