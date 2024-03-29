package com.example.demo.repository;

import com.example.demo.model.User;

import java.io.Serializable;
import java.util.List;

public interface IGenericDao <T extends Serializable> {


    T findOne(final Object id);

    List<T> findAll();

    void create(final T entity);

    T update(final T entity);

    void delete(final T entity);

    void deleteById(final Object entityId);

}
