package com.example.demo.repository;

import com.example.demo.model.Role;
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
public class RoleLogic extends AbstractJpaDao<Role> {
    Logger logger = Logger.getLogger(RoleLogic.class);



    public List<Role> getByColunmName( String columName,String value) {
        List<Role> roleList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Role> cr = cb.createQuery(Role.class);
            Root<Role> root = cr.from(Role.class);
            Predicate restriction = cb.equal(root.get(columName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Role> query = getCurrentSession().createQuery(cr);
            roleList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return roleList;
    }


}
