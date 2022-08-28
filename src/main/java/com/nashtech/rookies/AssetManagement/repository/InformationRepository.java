package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.entities.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information, Integer> {


    @Query(value = "select * from information i " +
            "where  i.staff_code like :prefix% order by id_information asc", nativeQuery = true)
    public List<Information> getListInforSamePrefix(String prefix);

    @Query(value = "select i.locations from information i where " +
            "i.id_account = (select id_account from accounts where user_name = :userName)", nativeQuery = true)
    public String getLocationByUserName(String userName);

    @Query(value = "select * from information i where i.id_account = :id", nativeQuery = true)
    Information getInformationByAccountsID(int id);
    
    @Query(value = "select * from information where locations = "
    		+ "(select locations from information i where i.id_account = :id)" +
            "and id_account in (select id_account from accounts where status = true)", nativeQuery = true)
    List<Information> getInformationByLocation(int id);

}
