package txu.user.mainapp.dao;

import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import txu.user.mainapp.base.AbstractDao;
import txu.user.mainapp.entity.PostEntity;


import java.util.List;

@Repository
public class PostDao extends AbstractDao<PostEntity> {

    @Transactional
    public PostEntity save(PostEntity postEntity) {
        if (postEntity.getId() == null || postEntity.getId() == 0) {
            persist(postEntity);
            return postEntity;
        } else {
            return merge(postEntity);
        }
    }

    @Override
    public PostEntity findById(Object Id) {
        return super.findById(Id);
    }

    public PostEntity getByUnsignedTitle(String unsignedTitle) {

        String queryString = "SELECT p FROM PostEntity AS p WHERE p.unsignedTitle = :unsignedTitle";
        Query query;
        query = getEntityManager().createQuery(queryString);
        query.setParameter("unsignedTitle", unsignedTitle);
        return getSingle(query);


    }

    public List<PostEntity> getAllPost() {
        String queryString = "SELECT p FROM PostEntity AS p";
        Query query;
        query = getEntityManager().createQuery(queryString);
        return getRessultList(query);
    }

    @Transactional
    public void delete(PostEntity postEntity) {
        remove(postEntity);
    }


}
