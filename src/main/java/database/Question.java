package database;

import javax.persistence.*;
import java.util.Arrays;
//entiry for Question table
@Entity
@Table(name = "QUESTIONS")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer questionId;

    private String questionTitle;
    private String topic;
    private Integer difficulty;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @ManyToOne
    private Quiz quiz;

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topics) {
        this.topic = topics;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", questionTitle='" + questionTitle + '\'' +
                ", topics=" + topic +
                ", difficulty=" + difficulty +
                '}';
    }
}
