package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RequestRepository extends JpaRepository<Request,Integer> {


    @Modifying
    @Query(value = "delete from request where id_request = :id", nativeQuery = true)
    void deleteByRequestId(int id);
}
