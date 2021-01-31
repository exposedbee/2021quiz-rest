package database.DTO;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import database.MCQChoice;
import database.Question;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDTO {

    private int id;
    private String questionTitle;
    private List<MCQChoiceDTO> choices;
    private String topic;
    private QuizDTO quiz;

    public QuizDTO getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizDTO quiz) {
        this.quiz = quiz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public List<MCQChoiceDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<MCQChoiceDTO> choices) {
        this.choices = choices;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Question toDataModel() {
        Question question = new Question();
//        if(this.id) question.setQuestionId(id);
        question.setQuestionTitle(questionTitle);
        question.setTopic(topic);
        return question;
    }

    public Question toDataModelWithId() {
        Question question = new Question();
        question.setQuestionId(id);
        question.setTopic(topic);
        question.setQuestionTitle(questionTitle);
        return question;
    }

    public void fromDataModel(Question question) {
        this.questionTitle = question.getQuestionTitle();
        this.topic=question.getTopic();
        this.id = question.getQuestionId();
    }

    public boolean checkVaildity(int i) {
        boolean flag = false;
        if (i == 0) {
            int c = choices.size();
            if ((this.questionTitle.length() < 10) || (c < 2)) {
                return false;
            }
            for (MCQChoiceDTO choiceDTO : choices) {
                if (choiceDTO.getValid() == true)
                    flag = true;
            }
        }
        System.out.println("Size is equal to=" + choices.size());
        return flag;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                ", questionTitle='" + questionTitle + '\'' +
                ", choices=" + choices +
                ", topic='" + topic + '\'' +
                '}';
    }
}
