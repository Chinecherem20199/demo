package com.example.demo.business_logic;

import com.example.demo.model.Post;
import com.example.demo.repository.AbstractJpaDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostLogic extends AbstractJpaDao<Post> {
    Logger logger = Logger.getLogger(PostLogic.class);

    public List<Post> getByColunmName(String columName, String value) {
        List<Post> postList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Post> cr = cb.createQuery(Post.class);
            Root<Post> root = cr.from(Post.class);
            Predicate restriction = cb.equal(root.get(columName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Post> query = getCurrentSession().createQuery(cr);
            postList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return postList;


    }
}
