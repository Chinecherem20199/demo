package com.example.demo.business_logic;

import com.example.demo.model.Comment;
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
public class CommentLogic extends AbstractJpaDao<Comment> {

    Logger logger = Logger.getLogger(CommentLogic.class);



    public List<Comment> getByColunmName(String columName, String value) {
        List<Comment> commentList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Comment> cr = cb.createQuery(Comment.class);
            Root<Comment> root = cr.from(Comment.class);
            Predicate restriction = cb.equal(root.get(columName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Comment> query = getCurrentSession().createQuery(cr);
            commentList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return commentList;
    }

}
