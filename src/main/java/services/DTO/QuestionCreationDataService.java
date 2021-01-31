package services.DTO;

import database.DTO.MCQChoiceDTO;
import database.DTO.QuestionDTO;
import database.MCQChoice;
import database.Question;
import services.MCQChoiceDAO;
import services.QuestionDAO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class QuestionCreationDataService {
    Logger l;
    private MCQChoiceDAO mcqChoiceDAO;
    private QuestionDAO questionDAO;

    public QuestionCreationDataService(){
        mcqChoiceDAO= new MCQChoiceDAO();
        questionDAO= new QuestionDAO();
    }

    public static List<QuestionDTO> convertFromDAO(List<MCQChoice> results){
        Map<Integer, QuestionDTO> dtoList = new LinkedHashMap<Integer, QuestionDTO>();
        for (MCQChoice choice : results) {
            Integer currentId;
            Question question = choice.getQuestion();
            if(question!=null) {
                currentId = question.getQuestionId();
                QuestionDTO questionDTO = dtoList.get(currentId);
                if (questionDTO == null) {
                    questionDTO = new QuestionDTO();
                    questionDTO.fromDataModel(question);
                    dtoList.put(currentId, questionDTO);
//                System.out.println(questionDTO.toString());
                }
                List<MCQChoiceDTO> choices = questionDTO.getChoices();
                if (choices == null) {
                    choices = new ArrayList<>();
                    questionDTO.setChoices(choices);
                }
                MCQChoiceDTO choiceDTO = new MCQChoiceDTO();
                choiceDTO.fromDataModel(choice);
                choices.add(choiceDTO);
//                System.out.println(choices);
            }
        }
        return new ArrayList<>(dtoList.values());
    }

    public List<QuestionDTO> searchQuestionAll(){
        List<MCQChoice> results=mcqChoiceDAO.searchAll();
        return convertFromDAO(results);
    }


    public boolean createQuestion(QuestionDTO newQuestionDTO){
        if(newQuestionDTO.checkVaildity(0))
        {
            Question newQuestion= newQuestionDTO.toDataModel();
            questionDAO.create(newQuestion);
            List<MCQChoiceDTO> mcqChoiceDTOS=newQuestionDTO.getChoices();
            for (MCQChoiceDTO e:mcqChoiceDTOS) {
            MCQChoice choice = e.toDataModel();
            choice.setQuestion(newQuestion);
            mcqChoiceDAO.create(choice);
        }
            return true;
        }
        return false;
    }



    public boolean updateQuestion(QuestionDTO newquestion) {
        if(newquestion.checkVaildity(0)) {
            Question question= newquestion.toDataModelWithId();
            questionDAO.update(question);
            for (MCQChoiceDTO choice : newquestion.getChoices()) {
                MCQChoice newChoice;
                try {
                    newChoice = choice.toDataModelWithId();
                    newChoice.setQuestion(question);
                    mcqChoiceDAO.update(newChoice);
                }
                catch (Exception e){
                    newChoice =choice.toDataModel();
                    newChoice.setQuestion(question);
                    mcqChoiceDAO.create(newChoice);
                }
            }
            return true;
        }
        else
            return false;
    }


    private static List<Question> getQuestion(int id){
        QuestionDAO questionDAO= new QuestionDAO();
        Question criteria= new Question();
        criteria.setQuestionId(id);
        return questionDAO.searchSpecificQuestion(criteria);
    }

    public List<QuestionDTO> getQuestionById(int id){
        List<Question> result=getQuestion(id);
        if(result.size()>=0){
            MCQChoice criteri= new MCQChoice();
            criteri.setQuestion(result.get(0));
            return convertFromDAO(mcqChoiceDAO.searchByCriteriaId(criteri));
        }
        return null;
    }

    public List<QuestionDTO> getQuestionByIdWhere(int id) {
        List<Question> result=getQuestion(id);
        if(result.size()>=0){
            MCQChoice criteri= new MCQChoice();
            criteri.setQuestion(result.get(0));
            return convertFromDAO(mcqChoiceDAO.searchByCriteriaIdWhere(criteri));
        }
        return null;
    }
}
