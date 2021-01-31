package database.DTO;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import database.Quiz;

import java.util.Arrays;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizDTO {

    private int id;
    private String quizname;
    private String topics;
    private List<QuestionDTO> questionDTO;

    public List<QuestionDTO> getQuestionDTO() {
        return questionDTO;
    }

    public void setQuestionDTO(List<QuestionDTO> questionDTO) {
        this.questionDTO = questionDTO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizname() {
        return quizname;
    }

    public void setQuizname(String quizname) {
        this.quizname = quizname;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public Quiz toDataBase(){
        Quiz quiz=new Quiz();
        quiz.setQuizID(this.id);
        quiz.setQuizname(this.quizname);
        quiz.setTopics(this.topics);
        return quiz;
    }

    public void fromDataModel(Quiz quiz){
        this.id=quiz.getQuizID();
        this.quizname=quiz.getQuizname();
        this.topics=quiz.getTopics();
    }

    @Override
    public String toString() {
        return "QuizDTO{" +
                "id=" + id +
                ", quizname='" + quizname + '\'' +
                ", topics=" + topics +
                '}';
    }
}
