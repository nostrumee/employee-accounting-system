package com.innowise.accountingsystem.model.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, T> {

    List<T> findAll();

    Optional<T> findById(K id);

    T save(T entity);

    boolean update(T entity);

    boolean delete(K id);
}
