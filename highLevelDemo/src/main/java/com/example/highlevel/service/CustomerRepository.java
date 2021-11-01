package com.example.highlevel.service;

import com.example.highlevel.pojo.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sebastian
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    @Override
    <S extends Customer> List<S> saveAll(Iterable<S> iterable);
}
