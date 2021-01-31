package services;

import database.Quiz;

import java.util.HashMap;
import java.util.List;

//class interacts directly with the Quiz Entity
public class QuizDAO extends GenericDAO<Quiz> {

    //function to create a new quiz
    @Override
    public void create(Quiz o) {
        super.create(o);
    }

    //function returns all the existing quizes
    public List<Quiz> searchAll() {

        return super.search(null, choice -> new HashMap(), "from Quiz");

    }

    //function to update a pre existing quiz
    public void update(Quiz quizUpdate) {
        Quiz quiz = find(Quiz.class, quizUpdate.getQuizID());
        System.out.println(quiz.toString());
        super.update(quizUpdate);
    }

    public Quiz find(Quiz quizUpdate) {
        return super.find(Quiz.class, quizUpdate.getQuizID());
    }
}
