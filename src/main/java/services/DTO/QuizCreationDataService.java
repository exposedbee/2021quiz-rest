package services.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.DTO.QuestionDTO;
import database.Question;
import database.Quiz;
import database.DTO.QuizDTO;
import services.MCQChoiceDAO;
import services.QuestionDAO;
import services.QuizDAO;


import java.util.*;

public class QuizCreationDataService {

    private final QuizDAO quizDAO;
    private final MCQChoiceDAO mcqChoiceDAO;
    private final QuestionDAO questionDAO;

    //Constructor
    public QuizCreationDataService() {
        quizDAO = new QuizDAO();
        mcqChoiceDAO = new MCQChoiceDAO();
        questionDAO = new QuestionDAO();

    }

    //function returns all the prsent quizes in quiz table
    public List<QuizDTO> searchQuizAll() {
        List<Quiz> result = quizDAO.searchAll();
        Map<Integer, QuizDTO> dtoList = new LinkedHashMap<Integer, QuizDTO>();
        for (Quiz q : result) {
            Integer currentId = q.getQuizID();
            QuizDTO quizDTO = new QuizDTO();
            quizDTO.fromDataModel(q);
            dtoList.put(currentId, quizDTO);
        }
        return new ArrayList<>(dtoList.values());
    }

    //function searches quiz by id from quiz table
    public QuizDTO searchQuizById(int id) {
        QuizDTO result = new QuizDTO();
        result.fromDataModel(quizDAO.find(Quiz.class, id));
        return result;
    }

    //function adds new Questions to a quiz
    public void addQuestionToQuiz(QuestionDTO input) {
        try {
            Quiz quiz = quizDAO.find(input.getQuiz().toDataBase());
            Question question = questionDAO.find(input.toDataModelWithId());
            question.setQuiz(quiz);
            questionDAO.update(question);
        } catch (Exception e) {
            //log
        }
    }

    //TODO:Implement the function
    public void removeQuestionFromQuiz(QuestionDTO input) {
        try {
            Question question = questionDAO.find(input.toDataModelWithId());
            question.setQuiz(null);
            questionDAO.update(question);
        } catch (Exception e) {
            //log
        }
    }

    //function that converts jsonstring to quiz object
    public void createQuiz(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Quiz quiz = objectMapper.readValue(jsonString, Quiz.class);
        quizDAO.create(quiz);
    }

    //function to update a exiting Quiz
    public void updateQuiz(QuizDTO quizDTO) {
        Quiz result;
        result = quizDTO.toDataBase();
        quizDAO.update(result);
    }

    //function to delete choice by id
    public boolean deleteChoice(int id) {
        try {
            mcqChoiceDAO.delete(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
