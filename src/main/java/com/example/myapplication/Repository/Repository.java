package com.example.myapplication.Repository;


import java.util.Optional;

public interface Repository<T,ID> {
    Optional<T> findById(ID id);
    T save(T entity);
    void deleteById(ID id);
}
