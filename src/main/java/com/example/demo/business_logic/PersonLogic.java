package com.example.demo.business_logic;


import com.example.demo.model.Person;
import com.example.demo.repository.AbstractJpaDao;
import org.apache.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonLogic extends AbstractJpaDao<Person> {
    Logger logger = Logger.getLogger(PersonLogic.class);



    public List<Person> getByColunmName(String columName, String value) {
        List<Person> personList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Person> cr = cb.createQuery(Person.class);
            Root<Person> root = cr.from(Person.class);
            Predicate restriction = cb.equal(root.get(columName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Person> query = getCurrentSession().createQuery(cr);
            personList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return personList;
    }
}
