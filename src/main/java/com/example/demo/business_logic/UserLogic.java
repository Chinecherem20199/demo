package com.example.demo.business_logic;

import com.example.demo.model.Person;
import com.example.demo.model.User;
import com.example.demo.repository.AbstractJpaDao;
import org.apache.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class UserLogic extends AbstractJpaDao<User> {
    Logger logger = Logger.getLogger(UserLogic.class);



    public List<User> getByColunmName( String columName,String value) {
        List<User> userList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            Predicate restriction = cb.equal(root.get(columName), value);
//            Predicate restriction1 = cb.equal(root.get(columName), value);
//            cr.select(root).where(restriction, restriction1).orderBy(cb.asc(root.get("id")));
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<User> query = getCurrentSession().createQuery(cr);
            userList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return userList;
    }

    public List<User> getByUsernameAndEmail( String userName,String email) {
        List<User> userList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            Join<User, Person> personJoin = root.join("person");

            Predicate personPredicate = cb.equal(personJoin.get("email" ),email);

            Predicate restriction = cb.equal(root.get("userName"), userName);
            Predicate joinAll =cb.or(personPredicate,restriction);
            cr.select(root).where(joinAll).orderBy(cb.asc(root.get("id")));
            Query<User> query = getCurrentSession().createQuery(cr);
            userList = query.getResultList();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return userList;
    }
}
