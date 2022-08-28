package com.nashtech.rookies.AssetManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.nashtech.rookies.AssetManagement.model.dto.respond.AccountDetailDto;
import com.nashtech.rookies.AssetManagement.model.entities.Accounts;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Integer> {

	@Query("select a from Accounts a where a.userName = ?1")
	Optional <Accounts> findByuserName(String userName);

	@org.springframework.data.jpa.repository.Query(value = "select * from accounts where user_name like :userName% order by id_account asc ", nativeQuery = true)
	List<Accounts> findAccountWithSameName(String userName);


	@org.springframework.data.jpa.repository.Query( value = "select NEW " +
			"com.nashtech.rookies.AssetManagement.model.dto.respond.AccountDetailDto(" +
			"a.accountId, i.staffCode, i.firstName, " +
			"i.lastName, a.userName," +
			"i.dateOfBirth, i.gender, i.joinDate, r.roleName, i.locations) " +
			"from Accounts a " +
			"join a.information i " +
			"join a.role r " +
			"where " +
			"i.locations = :locations " +
			"and r.roleName like :filter " +
			"and a.userName <> :userName " +
			"and a.status = true " +
			"and (lower(i.staffCode) like %:searchCode% or lower(a.userName) like %:searchCode% " +
			"or lower(concat(i.firstName,concat(' ', i.lastName)) ) like %:searchCode%)", nativeQuery = false)
	Page<AccountDetailDto> listAccountDetailBySearch(String searchCode, Pageable pageable,
													 String locations, String filter, String userName
	);


	@org.springframework.data.jpa.repository.Query(value = "select locations from " +
			"information where id_account = " +
			"(select id_account from accounts where user_name = :userName)", nativeQuery = true)
	String findLocationByUserName(String userName);

	@org.springframework.data.jpa.repository.Query(value = "delete from information where staff_code in (select staff_code from information group by staff_code having count(staff_code) > 1) " +
			"", nativeQuery = true)
	@Modifying
	void deleteDuplicateStaffCode();

	@org.springframework.data.jpa.repository.Query(value = "delete from accounts where " +
			"user_name in " +
			"(select user_name from accounts group by user_name having count(user_name) > 1)", nativeQuery = true)
	@Modifying
	void deleteDuplicateUserName();
}