package com.vibo.GenAI.service.impl;


import com.vibo.GenAI.dto.*;
import com.vibo.GenAI.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
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


        String template = """
                keep the response in one or two lines 
                Rules:
                1.should not include * , ** 
                and new line symbols
                2.use bullet points 
                """;
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create();

        String result = chatClient.prompt(prompt).user(message.getMessage()).call().content();

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

    @Override
    public List<String> listOpConverting(String message) {

        String promptMessage = """
                Name top 5 only movies name
                of {name}
                {format}
                """;
        ListOutputConverter opConverter = new ListOutputConverter(new DefaultConversionService());

        PromptTemplate template =new PromptTemplate(promptMessage);
        Prompt prompt = template.create(Map.of("name",message,"format",opConverter.getFormat()));

        String response = chatClient.prompt(prompt).call().content();
        if (response == null){
            throw new IllegalArgumentException("AI not responsed");
        }
       return opConverter.convert(response);
    }

    @Override
    public Movie beanOpConverting(String message) {
        String promptMessage = """
                Any one movie
                of {name}
                {format}
                """;

        BeanOutputConverter<Movie> opConverter = new BeanOutputConverter<>(Movie.class);

        PromptTemplate template =new PromptTemplate(promptMessage);
        Prompt prompt = template.create(Map.of("name",message,"format",opConverter.getFormat()));

        return opConverter.convert(chatClient.prompt(prompt).call().content());
    }


    @Override
    public List<Movie> listOfBeanOpConverting(String message) {
        String promptMessage = """
                Top 5 movies
                of {name}
                {format}
                """;

        BeanOutputConverter<List<Movie>> opConverter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<Movie>>() {});
        PromptTemplate template =new PromptTemplate(promptMessage);
        Prompt prompt = template.create(Map.of("name",message,"format",opConverter.getFormat()));

        return opConverter.convert(chatClient.prompt(prompt).call().content());
    }

}
