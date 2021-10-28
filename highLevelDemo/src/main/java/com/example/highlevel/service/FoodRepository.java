package com.example.highlevel.service;

import com.example.highlevel.pojo.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sebastian
 */
@Repository
public interface FoodRepository extends JpaRepository<Food,Integer> {

    /**
     * 联查详情
     * @return 食物详情
     */
    @Query(value = "select f.id as foodId,f.name as foodName,f.price as foodPrice,t.name as typeName,s.name as supplierName from Food f left join Type t on f.typeId = t.id left join Supplier s on f.supplier = s.id")
    List<Object[]> getFoodJoinTypeAndSupplier();
    
}
