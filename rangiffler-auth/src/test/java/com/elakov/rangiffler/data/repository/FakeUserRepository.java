package com.elakov.rangiffler.data.repository;

import com.elakov.rangiffler.data.UserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * EO-style fake: a real in-memory implementation of {@link UserRepository},
 * not a mock. Used by {@code RangifflerUserDetailsServiceWithFakeObjectsTest}
 * to demonstrate the fake-over-mock approach (Bugayenko, Elegant Objects).
 *
 * Only {@link #findByUsername} and {@link #save} — the methods the production
 * code actually exercises — carry behaviour. The rest of the wide JpaRepository
 * surface throws UnsupportedOperationException: a deliberate illustration that
 * faking a fat framework interface is costly (the very reason a stub is often
 * preferred for such collaborators).
 */
public class FakeUserRepository implements UserRepository {

    private final Map<String, UserEntity> byUsername = new HashMap<>();

    public FakeUserRepository(UserEntity... seed) {
        for (UserEntity user : seed) {
            byUsername.put(user.getUsername(), user);
        }
    }

    @Override
    public UserEntity findByUsername(String username) {
        return byUsername.get(username);
    }

    @Override
    public <S extends UserEntity> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        byUsername.put(entity.getUsername(), entity);
        return entity;
    }

    // --- unused JpaRepository surface ---

    @Override public <S extends UserEntity> List<S> saveAll(Iterable<S> entities) { throw unsupported(); }
    @Override public Optional<UserEntity> findById(UUID id) { throw unsupported(); }
    @Override public boolean existsById(UUID id) { throw unsupported(); }
    @Override public List<UserEntity> findAll() { throw unsupported(); }
    @Override public List<UserEntity> findAllById(Iterable<UUID> ids) { throw unsupported(); }
    @Override public long count() { throw unsupported(); }
    @Override public void deleteById(UUID id) { throw unsupported(); }
    @Override public void delete(UserEntity entity) { throw unsupported(); }
    @Override public void deleteAllById(Iterable<? extends UUID> ids) { throw unsupported(); }
    @Override public void deleteAll(Iterable<? extends UserEntity> entities) { throw unsupported(); }
    @Override public void deleteAll() { throw unsupported(); }
    @Override public List<UserEntity> findAll(Sort sort) { throw unsupported(); }
    @Override public Page<UserEntity> findAll(Pageable pageable) { throw unsupported(); }
    @Override public void flush() { throw unsupported(); }
    @Override public <S extends UserEntity> S saveAndFlush(S entity) { throw unsupported(); }
    @Override public <S extends UserEntity> List<S> saveAllAndFlush(Iterable<S> entities) { throw unsupported(); }
    @Override public void deleteAllInBatch(Iterable<UserEntity> entities) { throw unsupported(); }
    @Override public void deleteAllByIdInBatch(Iterable<UUID> ids) { throw unsupported(); }
    @Override public void deleteAllInBatch() { throw unsupported(); }
    @Override public UserEntity getOne(UUID id) { throw unsupported(); }
    @Override public UserEntity getById(UUID id) { throw unsupported(); }
    @Override public UserEntity getReferenceById(UUID id) { throw unsupported(); }
    @Override public <S extends UserEntity> Optional<S> findOne(Example<S> example) { throw unsupported(); }
    @Override public <S extends UserEntity> List<S> findAll(Example<S> example) { throw unsupported(); }
    @Override public <S extends UserEntity> List<S> findAll(Example<S> example, Sort sort) { throw unsupported(); }
    @Override public <S extends UserEntity> Page<S> findAll(Example<S> example, Pageable pageable) { throw unsupported(); }
    @Override public <S extends UserEntity> long count(Example<S> example) { throw unsupported(); }
    @Override public <S extends UserEntity> boolean exists(Example<S> example) { throw unsupported(); }
    @Override public <S extends UserEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) { throw unsupported(); }

    private static UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException("not needed by the unit under test");
    }
}
