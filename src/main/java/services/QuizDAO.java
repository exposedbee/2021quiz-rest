package services;

import database.Quiz;

import java.util.HashMap;
import java.util.List;

public class QuizDAO extends GenericDAO<Quiz>{

    @Override
    public void create(Quiz o) {
        super.create(o);
    }

    public List<Quiz> searchAll() {

        return super.search(null, choice -> new HashMap(), "from Quiz");

    }

    public void update(Quiz quizUpdate){
        Quiz quiz = find(Quiz.class,quizUpdate.getQuizID());
        System.out.println(quiz.toString());
        super.update(quizUpdate);
    }
    public Quiz find(Quiz quizUpdate){
        return super.find(Quiz.class,quizUpdate.getQuizID());
    }
//    public List<Quiz> searchAll(Quiz criteria) {
//
//        Function<Quiz, Map<String, Object>> getParamsFunction = quiz -> {
//            Map<String, Object> parameters = new HashMap<String,Object>();
//            parameters.put("id", criteria.getQuizID());
//            return parameters;
//        };
//        System.out.println(criteria.getQuizID());
//        return super.search(criteria, getParamsFunction, "from Question where id=:id ");
//    }
}
