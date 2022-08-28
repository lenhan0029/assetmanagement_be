package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.dto.respond.AssetDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.rookies.AssetManagement.model.entities.Asset;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AssetRepository extends JpaRepository<Asset, Integer>{




	//@org.springframework.data.jpa.repository.Query(value = "select a from Asset a where a.assetCode like :assetCode%"
//				+ " or a.category.catogeryName like :categoryName%", nativeQuery = false)
	//JPQL.
	@Query(value = "select NEW com.nashtech.rookies.AssetManagement.model.dto.respond.AssetDetailDTO(" +
			"ast.assetId, ast.assetCode, ast.assetName" +
			", ast.installedDate, ast.location, ast.state, ast.category.categoryName) " +
			"from Asset ast " +
			"where " +
			"(lower(ast.assetCode)  like  %:searchCode% or lower(ast.assetName)  like %:searchCode%) " +
			"and " +
			"ast.location = :location " +
			"and ast.state in :state " +
			"and ast.category.categoryId in :categoryName", nativeQuery = false)
	Page<AssetDetailDTO> listAssetDetailBySearch(String searchCode, String[] state, int[] categoryName, Pageable page , String location);

	@Query(value = "select * from assets where asset_code like :assetCode% order by id_asset asc", nativeQuery = true)
	List<Asset> getListAssetSameKeys(String assetCode);

	//or a.state like :state
	
	@Query(value = "select * from assets where states = 'AVAILABLE' ", nativeQuery = true)
	List<Asset> getListAssetAvalable();


}
