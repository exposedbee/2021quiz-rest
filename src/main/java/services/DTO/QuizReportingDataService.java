package services.DTO;

import database.DTO.QuestionDTO;
import database.MCQChoice;
import database.Question;
import database.Quiz;
import services.MCQChoiceDAO;
import services.QuestionDAO;
import services.QuizDAO;
import database.DTO.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuizReportingDataService {
    private QuizDAO quizDAO;
    private MCQChoiceDAO mcqChoiceDAO;
    private QuestionDAO questionDAO;
    private QuestionReportingDataService questionReportingDataService;

    public QuizReportingDataService() {
        questionReportingDataService = new QuestionReportingDataService();
        quizDAO = new QuizDAO();
        mcqChoiceDAO = new MCQChoiceDAO();
        questionDAO = new QuestionDAO();
    }

    //function returns all the questions present iin a perticular quiz
    public QuizDTO getQuestionsForQuiz(int id, int val) {

        Quiz quiz = new Quiz();
        quiz.setQuizID(id);
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.fromDataModel(quizDAO.find(quiz));
        Question creiteria = new Question();
        creiteria.setQuiz(quizDAO.find(quiz));
        List<Question> result = questionDAO.getQuestionsOfQuiz(creiteria);
        Map<Integer, QuestionDTO> dtoList = new LinkedHashMap<Integer, QuestionDTO>();

        for (Question r : result) {
            MCQChoice criteri = new MCQChoice();
            criteri.setQuestion(r);
            List<MCQChoice> choices = mcqChoiceDAO.searchByCriteriaId(criteri);
            for (MCQChoice choice : choices) {
                Integer currentId;
                Question question = choice.getQuestion();
                currentId = question.getQuestionId();

                QuestionDTO questionDTO = dtoList.get(currentId);
                if (questionDTO == null) {
                    questionDTO = new QuestionDTO();
                    questionDTO.fromDataModel(question);
                    dtoList.put(currentId, questionDTO);
                }
                List<MCQChoiceDTO> choicess = questionDTO.getChoices();
                if (choicess == null) {
                    choicess = new ArrayList<>();
                    questionDTO.setChoices(choicess);
                }
                MCQChoiceDTO choiceDTO = new MCQChoiceDTO();
                choiceDTO.fromDataModel(choice);
                if (val == 1) choiceDTO.setValid(false);
                choicess.add(choiceDTO);
            }
        }
        quizDTO.setQuestions(new ArrayList<>(dtoList.values()));
        return quizDTO;
    }

    //function computes the quiz result(validates the quiz taken)
    public Validate getQuizResult(QuizDTO quiz) {
        if (quizDAO.find(Quiz.class, quiz.getId()) == null || quiz.getQuestions().size() == 0)
            System.out.println("error in here");
        else {
            Validate result = new Validate();
            for (QuestionDTO questionDTO : quiz.getQuestions()) {
                try {
                    List<QuestionDTO> ans = questionReportingDataService.getQuestionById(questionDTO.getId());
                    result.setTotalQuestions(result.getTotalQuestions() + 1);
                    if (compute(questionDTO.getChoices(), ans.get(0).getChoices()))
                        result.setScore(result.getScore() + 1);
                } catch (Exception e) {

                }
            }
            result.setPercentageAccuracy((((float) result.getScore() / (float) result.getTotalQuestions()) * 100));
            return result;
        }
        return null;
    }
    //function that checks answers one question at a time
    //TODO:Needs to be updates to make it fast and efficient
    private boolean compute(List<MCQChoiceDTO> givenChoices, List<MCQChoiceDTO> rightChoices) {
        int i = 0;
        if (givenChoices.size() != rightChoices.size())
            return false;
        while (rightChoices.size() > i) {
            MCQChoiceDTO r = rightChoices.get(i);
            for (MCQChoiceDTO c : givenChoices
            ) {
                if (c.getId() == r.getId() && c.getValid() == r.getValid()) {
                    givenChoices.remove(c);
                    break;
                }
            }
            i++;
        }
        if (givenChoices.size() == 0)
            return true;
        return false;
    }
}
