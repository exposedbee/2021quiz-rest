package rest.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Question;
import database.Quiz;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ConfigurableApplicationContext;
import services.DTO.QuestionCreationDataService;
import services.DTO.QuizCreationDataService;
import services.DTO.QuizReportingDataService;
import services.QuestionDAO;
import database.DTO.*;
import services.QuizDAO;

import java.util.List;
import java.util.Map;


@SpringBootApplication
@RestController
public class quizApplication {

    private QuizCreationDataService quizCreationDataService;
    private QuizReportingDataService quizReportingDataService;
    private QuestionCreationDataService questionCreationDataService;
    ObjectMapper mapper;

    quizApplication(){
        quizReportingDataService= new QuizReportingDataService();
        quizCreationDataService =new QuizCreationDataService();
        questionCreationDataService =new QuestionCreationDataService();
        mapper = new ObjectMapper();

    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context=SpringApplication.run(quizApplication.class, args);
        System.out.println("listening in port : http://localhost:8080/hello");
    }

    @GetMapping("/hello")
    public static String hello(@RequestParam(value = "name", defaultValue = "World") String name) {

        Question question=new Question();
        question.setDifficulty(3);
        question.setQuestionTitle("This is a test");
        QuestionDAO questionDAO=new QuestionDAO();
        questionDAO.create(question);
        return String.format("Hello %s!", name);
    }


    @RequestMapping(
            value = "/process")
    public void process(@RequestBody Map<String, Object> payload)
            throws Exception {
        Question question=new Question();
        question.setQuestionId(94);
        QuestionDAO questionDAO=new QuestionDAO();
        List<Question> questionDAOList= questionDAO.searchSpecificQuestion(question);
        for(Question temp: questionDAOList)
        {
            System.out.println(temp.toString());
        }
        System.out.println(payload);
    }

    @RequestMapping(
            value = "/create/quiz")
    public String createQuiz(@RequestBody Map<String, Object> payload)
            throws Exception {
        String jsonString = mapper.writeValueAsString(payload);
        System.out.println(jsonString);
        quizCreationDataService.createQuiz(jsonString);
        System.out.println(payload);
        return String.format("Success");

    }

    @GetMapping(
            value = "/search/quiz")
    public String searchQuiz()
            throws Exception {
        List<QuizDTO> quizDTO= quizCreationDataService.searchQuizAll();
        String jsonString = mapper.writeValueAsString(quizDTO);
        System.out.println(jsonString);
        return jsonString;

    }
    @GetMapping(
            value = "/search/quiz/id")
    public String searchQuizById(@RequestParam(value = "id") int id)
            throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(quizCreationDataService.searchQuizById(id));
        System.out.println(jsonString);
        return jsonString;

    }

    @RequestMapping(
            value = "/update/quiz")
    public String updateQuiz(@RequestBody Map<String, Object> payload)
            throws Exception {
        QuizDTO quiz;
        String jsonString = mapper.writeValueAsString(payload);
        quiz= mapper.readValue(jsonString, QuizDTO.class);
        quizCreationDataService.updateQuiz(quiz);
        return String.valueOf(payload);
    }

//    @RequestMapping(
//            value = "/delete/quiz")
//    public String deleteQuiz(@RequestBody Map<String, Object> payload)
//            throws Exception {
//        QuizDTO quiz;
//        String jsonString = mapper.writeValueAsString(payload);
//        quiz= mapper.readValue(jsonString, QuizDTO.class);
//        quizReportingDataService.updateQuiz(quiz);
//        return String.valueOf(payload);
//    }

    @RequestMapping(
            value = "/create/question")
    public ResponseEntity<String> createNewQuestion(@RequestBody Map<String, Object> payload)
            throws Exception {
        QuestionDTO questionDTO;
        try {
            String jsonString = mapper.writeValueAsString(payload);
            questionDTO = mapper.readValue(jsonString, QuestionDTO.class);
//            System.out.println(questionDTO.toString())

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if(!questionCreationDataService.createQuestion(questionDTO)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Json missing element");
        return ResponseEntity.ok("Success");
    }
    @GetMapping(
            value = "/search/question")
    public String getAllQuestion() throws JsonProcessingException {
        List<QuestionDTO> result= questionCreationDataService.searchQuestionAll();
        ObjectMapper mapper = new ObjectMapper();
        String output = mapper.writeValueAsString(result);
        System.out.println(result);
        return String.format(output);
    }
    @GetMapping(
            value = "/search/question/id")
    public String getAllQuestion(@RequestParam(value = "id") int id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String output = mapper.writeValueAsString(questionCreationDataService.getQuestionByIdWhere(id));
        return String.format(output);
    }
    @PostMapping(
            value = "/delete/choice")
    public ResponseEntity<String> deleteChoiceById(@RequestParam(value = "id") int id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if(quizCreationDataService.deleteChoice(id))
            return ResponseEntity.ok("");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad id");
    }

    @PostMapping(
            value = "/update/question")
    public ResponseEntity<Object> updateQuestion(@RequestBody Map<String, Object> payload) throws JsonProcessingException {
        QuestionDTO question;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(payload);
        try {
            question = mapper.readValue(jsonString, QuestionDTO.class);
            if(questionCreationDataService.updateQuestion(question))

                return ResponseEntity.ok("");
        }
        catch (Exception e){
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");

    }

    @PostMapping(
            value = "/quiz/addQuestion")
    public String addQuestionToQuiz(@RequestBody Map<String, Object> payload) throws JsonProcessingException {
        QuestionDTO question;
        String jsonString = mapper.writeValueAsString(payload);
        question= mapper.readValue(jsonString, QuestionDTO.class);
//        System.out.println(question.getQuiz()+" Questioni d="+question.getId());
        quizCreationDataService.addQuestionToQuiz(question);
        return null;
    }

    @GetMapping(
            value = "/quiz/questions")
    public String getAllQuestionForQuiz(@RequestParam(value = "id") int id) throws JsonProcessingException {
        String output = mapper.writeValueAsString(quizReportingDataService.getQuestionsForQuiz(id,0));
        return String.format(output);
    }

    @GetMapping(
            value = "/quiz/takeQuiz")
    public String takeQuizById(@RequestParam(value = "id") int id) throws JsonProcessingException {
        String output = mapper.writeValueAsString(quizReportingDataService.getQuestionsForQuiz(id,1));
        return String.format(output);
    }
    @GetMapping(
            value = "/quiz/validate")
    public String quizValidation(@RequestBody Map<String, Object> payload) throws JsonProcessingException {
        QuizDTO question;
        String jsonString = mapper.writeValueAsString(payload);
        question= mapper.readValue(jsonString, QuizDTO.class);
        Validate r=quizReportingDataService.getQuizResult(question);
        return String.format(r.toString());
    }

}
