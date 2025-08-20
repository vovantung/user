package txu.user.mainapp.dao;

import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import txu.user.mainapp.base.AbstractDao;
import txu.user.mainapp.entity.CommentEntity;


import java.util.List;

@Repository
public class CommentDao extends AbstractDao<CommentEntity> {

    @Transactional
    public CommentEntity save(CommentEntity commentEntity) {
        if (commentEntity.getId() == null || commentEntity.getId() == 0) {
            persist(commentEntity);
            return commentEntity;
        } else {
            return merge(commentEntity);
        }
    }

    @Override
    public CommentEntity findById(Object Id) {
        return super.findById(Id);
    }

    public List<CommentEntity> getCommentsOfPost(int postId) {
        String queryString = "SELECT c FROM CommentEntity AS c WHERE c.post.id=:postId";
        Query query;
        query = getEntityManager().createQuery(queryString);
        query.setParameter("postId", postId);
        List<CommentEntity> rs = getRessultList(query);
        return rs;
    }

    public List<CommentEntity> getAllArticle() {
        String queryString = "SELECT c FROM CommentEntity AS c";
        Query query;
        query = getEntityManager().createQuery(queryString);
        List<CommentEntity> rs = getRessultList(query);
        return rs;
    }

    @Transactional
    public void delete(CommentEntity commentEntity) {
        remove(commentEntity);
    }


}
