package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select * from roles where name_role = :roleName", nativeQuery = true)
    Role findByRoleName(String roleName);
}
