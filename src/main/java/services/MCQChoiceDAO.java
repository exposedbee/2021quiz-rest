package services;

import database.MCQChoice;
import database.Question;
import database.Quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MCQChoiceDAO extends GenericDAO<MCQChoice>{

    public void create(MCQChoice o) {
        super.create(o);
    }

    public List<MCQChoice> searchAll() {

        return super.search(null, choice -> new HashMap(), "from MCQChoice");

    }

    @Override
    public void update(MCQChoice o) {
        MCQChoice choice = find(MCQChoice.class,o.getId());
        choice.setQuestion(o.getQuestion());
        super.update(o);
    }

    public List<MCQChoice> searchByCriteriaId(MCQChoice criteria) {
        Function<MCQChoice, Map<String, Object>> getParamsFunction = choices -> {
            Map<String, Object> parameters = new HashMap<String,Object>();
            parameters.put("id", criteria.getQuestion().getQuestionId());
            return parameters;
        };
//        System.out.println(criteria.getQuestion().getQuestionId());
        return super.search(criteria, getParamsFunction, "from MCQChoice where question_id=:id");

    }

    public void delete(int id){
        MCQChoice choice=super.find(MCQChoice.class,id);
        choice.setQuestion(null);
        super.update(choice);
//        super.delete(choice);
    }

    public List<MCQChoice> searchByCriteriaIdWhere(MCQChoice criteria) {
        Function<MCQChoice, Map<String, Object>> getParamsFunction = choices -> {
            Map<String, Object> parameters = new HashMap<String,Object>();
            parameters.put("id", criteria.getQuestion().getQuestionId());
            parameters.put("valid", true);
            return parameters;
        };
        System.out.println(criteria.getQuestion().getQuestionId());
        return super.search(criteria, getParamsFunction, "from mcqchoices where question_id:id and valid=:valid ");
    }
}
