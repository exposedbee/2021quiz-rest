package services;

import database.Quiz;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

//Hybernate and entity manager generic class for all the entities present
public class GenericDAO<T> {

    protected EntityManager em;

    GenericDAO() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        em = emf.createEntityManager();
    }

    public void create(T o) {
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();

    }

    public List<T> search(T criteria, Function<T, Map<String, Object>> getParamsFunction, String queryString) {
        em.getTransaction().begin();
        Query query = em.createQuery(queryString);

        Map<String, Object> params = getParamsFunction.apply(criteria);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        em.getTransaction().commit();
        List<T> resultList = query.getResultList();
        return resultList;

    }

    public void update(T o) {
        em.getTransaction().begin();
        em.merge(o);
        em.getTransaction().commit();
    }

    public T find(Class<T> o, Integer val) {
        return em.find(o, val);
    }

    protected void delete(T o) {
        em.remove(o);
        em.getTransaction().commit();
    }
}
