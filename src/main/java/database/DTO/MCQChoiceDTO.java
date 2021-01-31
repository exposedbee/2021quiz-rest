package database.DTO;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import database.MCQChoice;
@JsonIgnoreProperties(ignoreUnknown = true)
public class MCQChoiceDTO {
    private int id;

    private String choiceTitle;
    private Boolean valid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getChoiceTitle() {
        return choiceTitle;
    }

    public void setChoiceTitle(String choiceTitle) {
        this.choiceTitle = choiceTitle;
    }

    public MCQChoice toDataModel() {
        MCQChoice choice = new MCQChoice();
//         choice.setId(this.id);
        choice.setChoice(choiceTitle);
        choice.setValid(valid);
        return choice;
    }

    public MCQChoice toDataModelWithId() {
        MCQChoice choice = new MCQChoice();
        choice.setId(this.id);
        choice.setChoice(choiceTitle);
        choice.setValid(valid);
        return choice;
    }

    public MCQChoiceDTO fromDataModel(MCQChoice choice) {
        this.choiceTitle = choice.getChoice();
        this.id = choice.getId();
        this.valid = choice.isValid();
        return this;
    }

    @Override
    public String toString() {
        return "MCQChoiceDTO [id=" + id + ", choiceTitle=" + choiceTitle + "valid=" +valid + "]";
    }
}
