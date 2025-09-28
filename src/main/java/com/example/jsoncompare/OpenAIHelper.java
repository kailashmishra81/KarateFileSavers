package com.example.jsoncompare;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.util.*;

public class OpenAIHelper {



    public static String getAiSummary(List<String> changes,String apiKey) {
        if (changes == null || changes.isEmpty()) return "No changes detected.";

        OpenAiService service = new OpenAiService(apiKey);

        //String prompt = "Please analyze the following JSON comparison result: \n" + String.join("\n", changes);
        String prompt = "Please analyze the following JSON comparison result: \n" + changes + "\nExplain the following changes in JSON files: \n" + "No further explaination just explain what is changed";
        ChatMessage message = new ChatMessage("user", prompt);
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(Collections.singletonList(message))
                .build();

        return service.createChatCompletion(request)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
