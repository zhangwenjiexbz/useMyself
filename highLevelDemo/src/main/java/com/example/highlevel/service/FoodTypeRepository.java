package com.example.highlevel.service;

import com.example.highlevel.pojo.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sebastian
 */
@Repository
public interface FoodTypeRepository extends JpaRepository<Type,Integer> {

    /**
     * 根据id查类型
     * @param id 
     * @return
     */
    List<Type> queryTypeById(Integer id);

    /**
     * 查所有
     * @return
     */
    List<Type> readAllByIdIsNotNull();
    
}
