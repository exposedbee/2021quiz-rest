package database;

import javax.persistence.*;
//Entiry for MCQChoice table
@Entity
@Table(name = "MCQCHOICES")
public class MCQChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String choice;
    private boolean valid;

    @ManyToOne
    private Question question;

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "MCQChoice [id=" + id + ", choice=" + choice + ", valid=" + valid + ", question_id=" + question + "]";
    }


}
