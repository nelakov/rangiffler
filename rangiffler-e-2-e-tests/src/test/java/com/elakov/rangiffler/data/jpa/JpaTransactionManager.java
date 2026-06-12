package com.elakov.rangiffler.data.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;

public abstract class JpaTransactionManager {

    private final EntityManagerFactory emf;

    public JpaTransactionManager(EntityManagerFactory emf) {
        this.emf = emf;
    }

    protected EntityManager em() {
        return emf.createEntityManager();
    }

    protected void persist(Object entity) {
        transaction(em -> em.persist(entity));
    }

    protected void remove(Object entity) {
        // The entity was loaded by an earlier transaction, so it is detached from
        // this call's EntityManager; re-attach it before removing.
        transaction(em -> em.remove(em.contains(entity) ? entity : em.merge(entity)));
    }

    protected void merge(Object entity) {
        transaction(em -> em.merge(entity));
    }

    protected void transaction(Consumer<EntityManager> consumer) {
        EntityManager em = em();
        EntityTransaction transaction = em.getTransaction();

        if (transaction.isActive()) {
            transaction.rollback();
        }
        em.clear();
        transaction.begin();
        try {
            consumer.accept(em);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
