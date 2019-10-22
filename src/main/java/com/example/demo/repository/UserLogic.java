package com.example.demo.repository;

import com.example.demo.model.User;
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
public class UserLogic extends AbstractJpaDao<User> {
    Logger logger = Logger.getLogger(UserLogic.class);



    public List<User> getByColunmName( String columName,String value) {
        List<User> userList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            Predicate restriction = cb.equal(root.get(columName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<User> query = getCurrentSession().createQuery(cr);
            userList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return userList;
    }


}
