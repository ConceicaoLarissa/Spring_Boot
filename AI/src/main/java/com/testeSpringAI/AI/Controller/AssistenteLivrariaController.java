package com.testeSpringAI.AI.Controller;


import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/livraria")
public class AssistenteLivrariaController {


    private final OpenAiChatModel chatModel;

    @Autowired
    public AssistenteLivrariaController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }


    @GetMapping("/gerarPiada")
    public Flux<String> gerarStream(@RequestParam(value = "mensagem",
            defaultValue = "Conte-me uma piada") String mensagem) {
        Prompt prompt = new Prompt(new UserMessage(mensagem));
        return chatModel.stream(prompt).map(response -> response.getResult().getOutput().getContent());
    }

    @GetMapping("/gerarCiencista")
    public String gerarCiencista(@RequestParam(value = "mensagem",
            defaultValue = "quando nasceu albert einstein?") String mensagem) {
       return chatModel.call(mensagem);
    }


    @GetMapping("/resenhas")
    public String obterResenhas(@RequestParam(value = "livro", defaultValue = "Dom Quixote") String livro) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                  Por favor, forneça um breve resumo do livro {livro} 
                  e também a biografia do seu autor.
                """);
        promptTemplate.add("livro", livro);
        return this.chatModel.call(promptTemplate.create()).getResult().getOutput().getContent();
    }



}
