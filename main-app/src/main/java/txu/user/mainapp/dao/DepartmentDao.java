package txu.user.mainapp.dao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import txu.user.mainapp.base.AbstractDao;
import txu.user.mainapp.entity.DepartmentEntity;

@Repository
public class DepartmentDao extends AbstractDao<DepartmentEntity> {

    @Override
    public DepartmentEntity findById(Object Id) {
        return super.findById(Id);
    }

    @Transactional
    public void remove(DepartmentEntity departmentEntity) {
        departmentEntity = merge(departmentEntity);
        getEntityManager().remove(departmentEntity);
    }



}
