package bk.scholar.app.web;

import bk.scholar.app.tool.FruitTools;
import bk.scholar.app.tool.UtilityTools;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatLocalOllamaResource {

  private final ChatClient chatClient;

  public ChatLocalOllamaResource(ChatClient.Builder builder, ChatMemory chatMemory, FruitTools fruitTools) {
    this.chatClient = getChatClient(builder, chatMemory, fruitTools);
  }

  private ChatClient getChatClient(Builder builder, ChatMemory chatMemory, FruitTools fruitTools) {
    // return builder.build();
    var simpleLoggerAdvisor = new SimpleLoggerAdvisor();
    return builder
        .defaultTools(fruitTools, new UtilityTools())
        .defaultAdvisors(simpleLoggerAdvisor)
        .build();
  }

  @GetMapping("/chat-local/{prompt}")
  String chat(@PathVariable String prompt) {
    return chatClient.prompt(prompt).call().content();
  }

  @GetMapping("/chat-template/{prompt}/{number}")
  String useOfTemplate(@PathVariable String prompt, @PathVariable int number) {
    String response;

    // Dhaka, Bangladesh
    PromptTemplate pt = new PromptTemplate("""
        I want to visit the city give me best locations to visit there:
        {place}
        Give me {number} locations for this place.
        Make sure the title is relevant to the place with small line.
        """);

    Map<String, Object> vars = Map.of("place", prompt, "number", number);
    Message message = pt.createMessage(vars);
    return chatClient.prompt().messages(message).call().content();
  }
  @GetMapping("/chat-today/{prompt}")
  String chatToday(@PathVariable String prompt) {
    /*.defaultSystem("""
                You are a helpful assistant for SivaLabs company.
                You always respond based on the data you have from tools available to you.
                If you don't know the answer, you will respond with "I don't know".
                """)*/
    //return chatClient.prompt(prompt).call().content();
    return chatClient.prompt(prompt).system("""
        If you don't know the answer about the question
        You can use tools based on that you can answer and otherwise 
        Just say I don't know the ANSWER !!!
        """).call().content();
  }

  @GetMapping("/chat-fruit/{prompt}")
  String chatFruit(@PathVariable String prompt) {
    return chatClient.prompt(prompt).system("""
        If you don't know the answer about the question
        You can use tools based on that you can answer and otherwise 
        Just say I don't know the ANSWER ???
        """).call().content();
  }

}
