package txu.user.mainapp.dao;

import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import txu.user.mainapp.base.AbstractDao;
import txu.user.mainapp.entity.EmployeeEntity;

import java.util.List;

@Repository
public class EmployeeDao extends AbstractDao<EmployeeEntity> {


    @Override
    public EmployeeEntity findById(Object Id) {
        return super.findById(Id);
    }

    public List<EmployeeEntity> getAll() {
        String queryString = "SELECT e FROM EmployeeEntity AS e ";
        Query query;
        query = getEntityManager().createQuery(queryString);
        return getRessultList(query);
    }
}
