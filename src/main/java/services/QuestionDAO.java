package services;

import database.Question;
import database.Quiz;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

//class interacts directly with the Question Entity
public class QuestionDAO extends GenericDAO<Question> {
    //function to add a new question
    public void create(Question o) {
        super.create(o);
    }

    //function to update a pre existing question
    public void update(Question questionUpdate) {
        Question quiz = find(Question.class, questionUpdate.getQuestionId());
        quiz.setQuestionTitle(questionUpdate.getQuestionTitle());
        super.update(quiz);
    }

    //Function to search a question by its id
    public List<Question> searchSpecificQuestion(Question criteria) {
        Function<Question, Map<String, Object>> getParamsFunction = question -> {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("id", criteria.getQuestionId());
            return parameters;
        };
        System.out.println(criteria.getQuestionId());
        return super.search(criteria, getParamsFunction, "from Question where id=:id ");
    }

    //Functions to return questions that belong to a particular quiz entity(Quiz_id)
    public List<Question> getQuestionsOfQuiz(Question criteria) {
        Function<Question, Map<String, Object>> getParamsFunction = question -> {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("id", criteria.getQuiz().getQuizID());
            return parameters;
        };
        System.out.println(criteria.getQuestionId());
        return super.search(criteria, getParamsFunction, "from Question where quiz_id=:id ");
    }

    //Dunction to find a single question by id
    public Question find(Question toDataModelWithId) {
        return super.find(Question.class, toDataModelWithId.getQuestionId());
    }
}
