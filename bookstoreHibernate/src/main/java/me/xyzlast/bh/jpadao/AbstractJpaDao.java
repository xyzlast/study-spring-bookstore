package me.xyzlast.bh.jpadao;

import me.xyzlast.bh.entities.BaseEntity;
import me.xyzlast.bh.intefaces.EntityDao;
import me.xyzlast.bh.utils.JpaAction;
import me.xyzlast.bh.utils.JpaExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by ykyoon on 2/26/14.
 */
public abstract class AbstractJpaDao<T extends BaseEntity> implements EntityDao<T> {

    protected AbstractJpaDao(String entityName) {
        this.entityName = entityName;
    }

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    protected JpaExecutor executor;
    protected final String entityName;

    @Override
    public List<T> getAll() {
        return em.createQuery("from " + entityName).getResultList();
    }

    @Override
    public void deleteAll() {
        System.out.println(em);
        em.createQuery("delete from " + entityName).executeUpdate();
    }

    @Override
    public T getById(final int id) {
        Query query = em.createQuery("from " + entityName + " where id = :id");
        query.setParameter("id", id);
        return (T) query.getSingleResult();
    }

    @Override
    public void add(final T entity) {
        em.merge(entity);
    }

    @Override
    public void update(final T entity) {
        em.merge(entity);
    }

    @Override
    public int countAll() {
        return em.createQuery("from " + entityName).getResultList().size();
    }
}
