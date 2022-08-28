package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentDetailDto;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @org.springframework.data.jpa.repository.Query(value = "select * from assignments " +
            "where id_account = :id", nativeQuery = true)
    List<Assignment> getListAssignmentByAccountId(int id);


    @Query(value = "select new com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentDetailDto( " +
            "ast.assignmentId, ast.assignedDate, ast.note, ast.state, st.assetCode, " +
            "st.assetName, ac.userName, cr.userName, " +
            "ac.accountId, cr.accountId , st.specification ) " +
            "from Assignment ast " +
            "join ast.asset st " +
            "join ast.accounts ac " +
            "join ast.creators cr " +
            "join ac.information i " +
            "where  " +
            " ( ( ast.state like :filter or  ast.state like :filter1 or ast.state like :filter2 ) and ( function('to_char',ast.assignedDate,'dd/mm/yyyy')  like :day ) )" +
            " and ( lower(st.assetCode) like %:searchCode% or lower(st.assetName) like %:searchCode%  " +
            " or lower(ac.userName) like %:searchCode%) "+
            "")
    Page<AssignmentDetailDto> listAssignmentBySearch(String searchCode , Pageable pageable, String filter , String filter1 , String filter2 , String day);


    @Query(value = "select * from assignments where id_account = :assignUserId", nativeQuery = true)
    List<Assignment> getAssignmentByAssignedUser(int assignUserId);

}
