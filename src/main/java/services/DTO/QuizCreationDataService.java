package services.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.DTO.MCQChoiceDTO;
import database.DTO.QuestionDTO;
import database.MCQChoice;
import database.Question;
import database.Quiz;
import database.DTO.QuizDTO;
import services.MCQChoiceDAO;
import services.QuestionDAO;
import services.QuizDAO;


import java.util.*;

public class QuizCreationDataService {

    private QuizDAO quizDAO;
    private MCQChoiceDAO mcqChoiceDAO;
    private QuestionDAO questionDAO;

    public QuizCreationDataService(){
        quizDAO=new QuizDAO();
        mcqChoiceDAO= new MCQChoiceDAO();
        questionDAO= new QuestionDAO();

    }

    public List<QuizDTO> searchQuizAll(){
        List<Quiz> result=quizDAO.searchAll();
        Map<Integer, QuizDTO> dtoList = new LinkedHashMap<Integer, QuizDTO>();
        for(Quiz q: result){
            Quiz quiz=q;
            Integer currentId=q.getQuizID();
//            QuizDTO quizDTO=dtoList.get(currentId);
            QuizDTO quizDTO=new QuizDTO();
            quizDTO.fromDataModel(q);
            dtoList.put(currentId,quizDTO);
        }
        return new ArrayList<>(dtoList.values());
    }

    public QuizDTO searchQuizById(int id){
        QuizDTO result= new QuizDTO();
        result.fromDataModel(quizDAO.find(Quiz.class,id));
        return result;
    }

    public void addQuestionToQuiz(QuestionDTO input){
        try {
            Quiz quiz = quizDAO.find(input.getQuiz().toDataBase());
            Question question= questionDAO.find(input.toDataModelWithId());
            question.setQuiz(quiz);
            questionDAO.update(question);
        }
        catch (Exception e){

        }
    }

    public void createQuiz(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Quiz quiz=objectMapper.readValue(jsonString, Quiz.class);
//        System.out.println(quiz.toString());
        quizDAO.create(quiz);
    }
    public void updateQuiz(QuizDTO quizDTO){
        Quiz result;
        result=quizDTO.toDataBase();
        quizDAO.update(result);
    }


    public boolean deleteChoice(int id) {
        try{
            mcqChoiceDAO.delete(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
