package rest.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Question;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ConfigurableApplicationContext;
import services.DTO.QuestionReportingDataService;
import services.DTO.QuizCreationDataService;
import services.DTO.QuizReportingDataService;
import services.QuestionDAO;
import database.DTO.*;

import java.util.List;
import java.util.Map;


@SpringBootApplication
@RestController
public class quizApplication {

    private QuizCreationDataService quizCreationDataService;
    private QuizReportingDataService quizReportingDataService;
    private QuestionReportingDataService questionReportingDataService;
    ObjectMapper mapper;

    quizApplication() {
        quizReportingDataService = new QuizReportingDataService();
        quizCreationDataService = new QuizCreationDataService();
        questionReportingDataService = new QuestionReportingDataService();
        mapper = new ObjectMapper();

    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(quizApplication.class, args);
        System.out.println("listening in port : http://localhost:8080/hello");
    }

    //function to check the working of the application
    @GetMapping("/hello")
    public static String hello(@RequestParam(value = "name", defaultValue = "World") String name) {

        Question question = new Question();
        question.setDifficulty(3);
        question.setQuestionTitle("This is a test");
        QuestionDAO questionDAO = new QuestionDAO();
        questionDAO.create(question);
        return String.format("Hello %s!", name);
    }


    //function to create a new quiz entity and add it to the quiz table
    @RequestMapping(
            value = "/create/quiz")
    public ResponseEntity<String> createQuiz(@RequestBody Map<String, Object> payload)
            throws Exception {
        String jsonString = mapper.writeValueAsString(payload);
        quizCreationDataService.createQuiz(jsonString);
        return ResponseEntity.ok("Success");

    }

    //request to get all the quizes present in the quiz table
    @GetMapping(
            value = "/search/quiz")
    public ResponseEntity<String> searchQuiz()
            throws Exception {
        List<QuizDTO> quizDTO = quizCreationDataService.searchQuizAll();
        String jsonString = mapper.writeValueAsString(quizDTO);
        return ResponseEntity.ok(jsonString);

    }

    //function searche a quiz by its id(quizes that are stored in quiz table in the db)
    @GetMapping(
            value = "/search/quiz/id")
    public ResponseEntity<String> searchQuizById(@RequestParam(value = "id") int id)
            throws Exception {
        String jsonString = mapper.writeValueAsString(quizCreationDataService.searchQuizById(id));
        return ResponseEntity.ok(jsonString);

    }

    //function to update a already existing quiz
    @RequestMapping(
            value = "/update/quiz")
    public ResponseEntity<Map<String, Object>> updateQuiz(@RequestBody Map<String, Object> payload)
            throws Exception {
        QuizDTO quiz;
        String jsonString = mapper.writeValueAsString(payload);
        quiz = mapper.readValue(jsonString, QuizDTO.class);
        quizCreationDataService.updateQuiz(quiz);
        return ResponseEntity.ok(payload);
    }

    //Function to create new questions and its choices
    @RequestMapping(
            value = "/create/question")
    public ResponseEntity<String> createNewQuestion(@RequestBody Map<String, Object> payload)
            throws Exception {
        QuestionDTO questionDTO;
        try {
            String jsonString = mapper.writeValueAsString(payload);
            questionDTO = mapper.readValue(jsonString, QuestionDTO.class);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (!questionReportingDataService.createQuestion(questionDTO))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Json missing element");
        return ResponseEntity.ok("Success");
    }

    //request to get access to all the questions present in the database qwith their choices
    @GetMapping(
            value = "/search/question")
    public ResponseEntity<String> getAllQuestion() throws JsonProcessingException {
        List<QuestionDTO> result = questionReportingDataService.searchQuestionAll();
        ObjectMapper mapper = new ObjectMapper();
        String output = mapper.writeValueAsString(result);
        return ResponseEntity.ok(output);
    }

    //search a question by its id
    @GetMapping(
            value = "/search/question/id")
    public ResponseEntity<String> getAllQuestion(@RequestParam(value = "id") int id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String output = mapper.writeValueAsString(questionReportingDataService.getQuestionById(id));
        return ResponseEntity.ok(output);
    }

    //requesto to delete a choice present in question
    @PostMapping(
            value = "/delete/choice")
    public ResponseEntity<String> deleteChoiceById(@RequestParam(value = "id") int id) throws JsonProcessingException {
        if (quizCreationDataService.deleteChoice(id)) {
            String jsonString = mapper.writeValueAsString(questionReportingDataService.getChoiceById(id));
            return ResponseEntity.ok(jsonString);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad id");
    }


    //Request to update a existing quiz
    @PostMapping(
            value = "/update/question")
    public ResponseEntity<Object> updateQuestion(@RequestBody Map<String, Object> payload) throws JsonProcessingException {
        QuestionDTO question;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(payload);
        try {
            question = mapper.readValue(jsonString, QuestionDTO.class);
            if (questionReportingDataService.updateQuestion(question)) {
                jsonString = mapper.writeValueAsString(questionReportingDataService.getQuestionById(question.getId()));
                return ResponseEntity.ok(jsonString);
            }
        } catch (Exception e) {
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");

    }

    //adds a question to a quiz
    //INPUT: required question id and quiz id json format
    //OUTPUT: questionDTO jsonformat
    @PostMapping(
            value = "/quiz/addQuestion")
    public ResponseEntity<String> addQuestionToQuiz(@RequestBody Map<String, Object> payload) throws JsonProcessingException {
        QuestionDTO question;
        String jsonString = mapper.writeValueAsString(payload);
        question = mapper.readValue(jsonString, QuestionDTO.class);
        quizCreationDataService.addQuestionToQuiz(question);
        jsonString = mapper.writeValueAsString(questionReportingDataService.getQuestionById(question.getId()));
        return ResponseEntity.ok(jsonString);
    }

    //removes a question from a quiz
    @PostMapping(
            value = "/quiz/remove/Question")
    public ResponseEntity<String> removeQuestionFromQuiz(@RequestParam(value = "id") int id) throws JsonProcessingException {
        QuestionDTO question = new QuestionDTO();
        String jsonString;
        question.setId(id);
        quizCreationDataService.removeQuestionFromQuiz(question);
        jsonString = mapper.writeValueAsString(questionReportingDataService.getQuestionById(question.getId()));
        return ResponseEntity.ok(jsonString);
    }

    //Request to get all questions that belong to a perticular quiz
    @GetMapping(
            value = "/quiz/questions")
    public ResponseEntity<String> getAllQuestionForQuiz(@RequestParam(value = "id") int id) throws JsonProcessingException {
        String output = mapper.writeValueAsString(quizReportingDataService.getQuestionsForQuiz(id, 0));
        return ResponseEntity.ok(output);
    }

    //sents all the questions of a quiz but changes the choice validity to false for all choices
    @GetMapping(
            value = "/quiz/takeQuiz")
    public ResponseEntity<String> takeQuizById(@RequestParam(value = "id") int id) throws JsonProcessingException {
        String output = mapper.writeValueAsString(quizReportingDataService.getQuestionsForQuiz(id, 1));
        return ResponseEntity.ok(output);
    }


    //called when you want to validate a quiz and get result
    //Indput:Questions with all their choices and valid:true or false
    @GetMapping(
            value = "/quiz/validate")
    public ResponseEntity<String> quizValidation(@RequestBody Map<String, Object> payload) throws JsonProcessingException {
        QuizDTO question;
        String jsonString = mapper.writeValueAsString(payload);
        question = mapper.readValue(jsonString, QuizDTO.class);
        jsonString = mapper.writeValueAsString(quizReportingDataService.getQuizResult(question));
        return ResponseEntity.ok(jsonString);
    }


}
