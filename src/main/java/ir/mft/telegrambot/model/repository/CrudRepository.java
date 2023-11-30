package ir.mft.telegrambot.model.repository;


import ir.mft.telegrambot.common.JPA;
import ir.mft.telegrambot.model.entity.Base;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Map;

public class CrudRepository<T extends Base, I> implements AutoCloseable {
    private EntityManager entityManager;


    public CrudRepository() {
        entityManager= JPA.getJpa().getEntityManager();
    }


    public T insert(T t) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(t);
        entityTransaction.commit();
        return t;
    }

    public T update(T t) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.merge(t);
        entityTransaction.commit();
        return t;
    }

    public T physicalDelete(Class<T> tClass, I id) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        T entity = entityManager.find(tClass, id);
        entityManager.remove(entity);
        entityTransaction.commit();
        return entity;
    }

    public T logicalDelete(T t) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        t.setDeleted(true);
        entityManager.merge(t);
        entityTransaction.commit();
        return t;
    }

    public T selectById(Class<T> tClass, I id) {
        T entity = entityManager.find(tClass, id);
        return entity;
    }

    public List<T> selectAll(Class<T> tClass) {
        String sql = "select entity from " + tClass.getAnnotation(Entity.class).name() + " entity ";
        Query query = entityManager.createQuery(sql);
        List<T> tList = query.getResultList();
        return tList;
    }

    public List<T> selectAll(Class<T> tClass, int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(tClass);
        Root<T> root = criteriaQuery.from(tClass);
        criteriaQuery.select(root);
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);

        int first = (pageNumber-1) * pageSize  + 1;
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }



    public List<T> getResultList(String queryName, Map<String, Object> paramMap) {
        Query query = entityManager.createNamedQuery(queryName);
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                query.setParameter(key, paramMap.get(key));
            }
        }
        List<T> tList = query.getResultList();
        return tList;
    }

    public T getSingleResult(String queryName, Map<String, Object> paramMap) {
        Query query = entityManager.createNamedQuery(queryName);
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                query.setParameter(key, paramMap.get(key));
            }
        }
        List<T> tList = query.getResultList();
        if (tList.isEmpty()){
            return null;
        }
        return tList.get(0);
    }

    public long getRecordCount(Class tClass){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(tClass)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public void close() throws Exception {
        entityManager.close();
    }
}
