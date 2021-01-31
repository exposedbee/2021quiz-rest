package services.DTO;

import database.DTO.MCQChoiceDTO;
import database.DTO.QuestionDTO;
import database.MCQChoice;
import database.Question;
import services.MCQChoiceDAO;
import services.QuestionDAO;
import database.DTO.QuizDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class QuestionReportingDataService {
    //    Logger l;
    private MCQChoiceDAO mcqChoiceDAO;
    private QuestionDAO questionDAO;

    //constructor
    public QuestionReportingDataService() {
        mcqChoiceDAO = new MCQChoiceDAO();
        questionDAO = new QuestionDAO();
    }

    //Conversion: function takes in a list<MCQChoice> and returns a List<QuestionDTO>
    public static List<QuestionDTO> convertFromDAO(List<MCQChoice> results) {
        Map<Integer, QuestionDTO> dtoList = new LinkedHashMap<Integer, QuestionDTO>();
        for (MCQChoice choice : results) {
            Integer currentId;
            Question question = choice.getQuestion();
            if (question != null) {
                currentId = question.getQuestionId();
                QuestionDTO questionDTO = dtoList.get(currentId);
                if (questionDTO == null) {
                    questionDTO = new QuestionDTO();
                    questionDTO.fromDataModel(question);
                    if (question.getQuiz() != null) {
                        QuizDTO q = new QuizDTO();
                        q.fromDataModel(question.getQuiz());
                        questionDTO.setQuiz(q);
                    }
                    dtoList.put(currentId, questionDTO);
                }
                List<MCQChoiceDTO> choices = questionDTO.getChoices();
                if (choices == null) {
                    choices = new ArrayList<>();
                    questionDTO.setChoices(choices);
                }
                MCQChoiceDTO choiceDTO = new MCQChoiceDTO();
                choiceDTO.fromDataModel(choice);
                choices.add(choiceDTO);
            }
        }
        return new ArrayList<>(dtoList.values());
    }

    //function is called to get all questions present in the database
    public List<QuestionDTO> searchQuestionAll() {
        List<MCQChoice> results = mcqChoiceDAO.searchAll();
        return convertFromDAO(results);
    }

    //function called to create Questions and its choices
    public boolean createQuestion(QuestionDTO newQuestionDTO) {
        if (newQuestionDTO.checkVaildity(0)) {
            Question newQuestion = newQuestionDTO.toDataModel();
            //new question creation
            questionDAO.create(newQuestion);
            List<MCQChoiceDTO> mcqChoiceDTOS = newQuestionDTO.getChoices();
            for (MCQChoiceDTO e : mcqChoiceDTOS) {
                MCQChoice choice = e.toDataModel();
                choice.setQuestion(newQuestion);
                //new choice creation
                mcqChoiceDAO.create(choice);
            }
            return true;
        }
        return false;
    }


    //function that already updates a existing question
    public boolean updateQuestion(QuestionDTO newquestion) {
        if (newquestion.checkVaildity(0)) {
            Question question = newquestion.toDataModelWithId();
            //update from QuestionDAO.class called to update question
            questionDAO.update(question);
            for (MCQChoiceDTO choice : newquestion.getChoices()) {
                MCQChoice newChoice;
                try {
                    newChoice = choice.toDataModelWithId();
                    newChoice.setQuestion(question);
                    //update from MCQChoiceDAO.class called to update choices when a existing choice is detected
                    mcqChoiceDAO.update(newChoice);
                } catch (Exception e) {
                    newChoice = choice.toDataModel();
                    newChoice.setQuestion(question);
                    //create from MCQChoiceDAO.class called when a new choice a detected
                    mcqChoiceDAO.create(newChoice);
                }
            }
            return true;
        } else
            return false;
    }

    //function to get question by its id
    private static List<Question> getQuestion(int id) {
        QuestionDAO questionDAO = new QuestionDAO();
        Question criteria = new Question();
        criteria.setQuestionId(id);
        return questionDAO.searchSpecificQuestion(criteria);
    }

    //function to get question by its id
    public List<QuestionDTO> getQuestionById(int id) {
        List<Question> result = getQuestion(id);
        if (result.size() >= 0) {
            MCQChoice criteri = new MCQChoice();
            criteri.setQuestion(result.get(0));
            return convertFromDAO(mcqChoiceDAO.searchByCriteriaId(criteri));
        }
        return null;
    }

    //function to access MCQChoices for a perticular question
    public MCQChoiceDTO getChoiceById(int id) {
        MCQChoiceDTO result = new MCQChoiceDTO();
        return result.fromDataModel(mcqChoiceDAO.find(MCQChoice.class, id));
    }
}
