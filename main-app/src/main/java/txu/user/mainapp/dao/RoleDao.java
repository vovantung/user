package txu.user.mainapp.dao;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import txu.user.mainapp.base.AbstractDao;
import txu.user.mainapp.entity.RoleEntity;

@Repository
public class RoleDao extends AbstractDao<RoleEntity> {

    @Override
    public RoleEntity findById(Object Id) {
        return super.findById(Id);
    }

    @Transactional
    public void remove(RoleEntity roleEntity) {
        roleEntity = merge(roleEntity);
        getEntityManager().remove(roleEntity);
    }
}
