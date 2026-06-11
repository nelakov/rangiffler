package com.elakov.rangiffler.data.jpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.metamodel.Metamodel;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ThreadLocalEmf implements EntityManagerFactory {

    private final EntityManagerFactory delegate;
    private final ThreadLocal<EntityManager> threadLocalEm;

    public ThreadLocalEmf(EntityManagerFactory delegate) {
        this.delegate = delegate;
        threadLocalEm = ThreadLocal.withInitial(delegate::createEntityManager);
    }

    @Override
    public EntityManager createEntityManager() {
        return threadLocalEm.get();
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        threadLocalEm.set(delegate.createEntityManager(map));
        return threadLocalEm.get();
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        threadLocalEm.set(delegate.createEntityManager(synchronizationType));
        return threadLocalEm.get();
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        threadLocalEm.set(delegate.createEntityManager(synchronizationType, map));
        return threadLocalEm.get();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return delegate.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return delegate.getMetamodel();
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Cache getCache() {
        return delegate.getCache();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return delegate.getPersistenceUnitUtil();
    }

    @Override
    public void addNamedQuery(String name, Query query) {
        delegate.addNamedQuery(name, query);
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return delegate.unwrap(cls);
    }

    @Override
    public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
        delegate.addNamedEntityGraph(graphName, entityGraph);
    }

    @Override
    public <R> R callInTransaction(Function<EntityManager, R> work) {
        return delegate.callInTransaction(work);
    }

    @Override
    public void runInTransaction(Consumer<EntityManager> work) {
        delegate.runInTransaction(work);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public SchemaManager getSchemaManager() {
        return delegate.getSchemaManager();
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return delegate.getTransactionType();
    }

    @Override
    public <E> Map<String, EntityGraph<? extends E>> getNamedEntityGraphs(Class<E> entityType) {
        return delegate.getNamedEntityGraphs(entityType);
    }

    @Override
    public <R> Map<String, TypedQueryReference<R>> getNamedQueries(Class<R> resultType) {
        return delegate.getNamedQueries(resultType);
    }
}
