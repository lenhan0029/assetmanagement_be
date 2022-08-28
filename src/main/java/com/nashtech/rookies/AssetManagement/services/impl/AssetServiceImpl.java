package com.nashtech.rookies.AssetManagement.services.impl;

import com.nashtech.rookies.AssetManagement.exceptions.ApiDeniedException;
import com.nashtech.rookies.AssetManagement.exceptions.ConstraintViolateException;
import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.AssetManagement.model.AssetState;
import com.nashtech.rookies.AssetManagement.model.dto.*;
import com.nashtech.rookies.AssetManagement.model.dto.request.AssetUpdateDTO;
import com.nashtech.rookies.AssetManagement.model.dto.request.CreateAssetRequest;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssetDetailDTO;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentRespondDTO;
import com.nashtech.rookies.AssetManagement.model.dto.respond.MessageResponse;
import com.nashtech.rookies.AssetManagement.model.entities.Asset;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import com.nashtech.rookies.AssetManagement.model.entities.Category;
import com.nashtech.rookies.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.AssetManagement.repository.InformationRepository;
import com.nashtech.rookies.AssetManagement.security.UserLocal;
import com.nashtech.rookies.AssetManagement.services.AssetService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.nashtech.rookies.AssetManagement.utils.MyDateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImpl implements AssetService {
	@Autowired
	AssetRepository assetRepository;

	@Autowired
	UserLocal userLocal;

	InformationRepository informationRepository;

	AdminServiceImpl adminService;


	MyDateUtils myDateUtils;

	CategoryRepository categoryRepository;

	AssignmentRepository assignmentRepository;

	@Autowired
	public AssetServiceImpl(InformationRepository informationRepository, AdminServiceImpl adminService,
							MyDateUtils myDateUtils, CategoryRepository categoryRepository, AssignmentRepository assignmentRepository) {
		this.informationRepository = informationRepository;
		this.adminService = adminService;
		this.myDateUtils = myDateUtils;
		this.categoryRepository = categoryRepository;
		this.assignmentRepository = assignmentRepository;
	}

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseEntity<?> listAssetDetailBySearch(String searchCode, String categorySearch, String stateSearch, int page, String sort ) {

		//The categorySearch must be String like 1 or 1 23 or 1 23 ... Like an sequence with string many number in String, each number is an category's ID.
		//Then we split it into many smaller String. Then convert into integer, and pass into query to execute (in) query.
		//The same with stateSearch, but is many other string like AVAILABLE, UNAVAILABLE, ...

		String[] categoryArr = null;


		if(categorySearch.equals("0")){
			List<Category> listAllCategory = this.categoryRepository.findAll();
			categoryArr = listAllCategory.stream().map(Category::getCategoryId).map(String::valueOf).toArray(String[]::new);
		}else{
			categoryArr = categorySearch.split(",");
		}

		String[] stateArr = stateSearch.split(",");
		int[] categoryId = Stream.of(categoryArr).mapToInt(Integer::parseInt).toArray();


		String userName = userLocal.getLocalUserName();
		String location = this.informationRepository.getLocationByUserName(userName);

		if (!searchCode.equals(""))
		{
			Pageable newPage = createPage(page,sort);

			Page<AssetDetailDTO> pageAsset= this.assetRepository.
					listAssetDetailBySearch(searchCode.toLowerCase(), stateArr, categoryId, newPage, location);
			if (pageAsset.hasContent())
			{
				return ResponseEntity.ok(pageAsset);
			}
			throw new ResourceNotFoundException("This page is empty");
		}
		throw new ConstraintViolateException("Please enter at least one character");
	}

	@Override
	public ResponseEntity<?> getAssetById(Integer id) {
		Optional<Asset> asset = assetRepository.findById(id);
		if(!asset.isPresent()) {
			throw new ResourceNotFoundException("Not found asset");
		}
		AssetDto assetDto = modelMapper.map(asset, AssetDto.class);
		assetDto.setCategoryName(asset.get().getCategory().getCategoryName());
		List<Assignment> assignmentList = asset.get().getAssignments();

		List<AssignmentRespondDTO> assignmentRespondDTOList = assignmentList.stream()
				.map(assignment -> {
					AssignmentRespondDTO assignmentRespondDTO = modelMapper.map(assignment, AssignmentRespondDTO.class);
					assignmentRespondDTO.setAssignedBy(assignment.getCreators().getUserName());
					assignmentRespondDTO.setAssignedTo(assignment.getAccounts().getUserName());
					return assignmentRespondDTO;
				}).collect(Collectors.toList());

		assetDto.setAssignmentDtoList(assignmentRespondDTOList);

		return ResponseEntity.ok(assetDto);
	}

	@Override
	public AssetDto createNewAsset(CreateAssetRequest createAssetRequest) {
		Asset asset = modelMapper.map(createAssetRequest, Asset.class);

		String userName = userLocal.getLocalUserName();
		String adminLocation = this.informationRepository.getLocationByUserName(userName);

		String assetPrefix = createAssetRequest.getAssetPrefix();
		Category categorySelected = this.categoryRepository.findCatogeryByPrefix(assetPrefix);
		if(categorySelected.equals(null)){
			throw new ResourceNotFoundException("Category Not Found!!");
		}

		String assetCode = generateCode(assetPrefix,"000000");
		String dateInString = createAssetRequest.getInstalledAt();

		Date installDate = myDateUtils.getDate(dateInString);

		asset.setAssetCode(assetCode);
		asset.setLocation(adminLocation);
		asset.setInstalledDate(installDate);
		asset.setAssetId(0);

		String state = createAssetRequest.getStatus().toLowerCase();


		switch (state) {

			case "available" :
				asset.setState("AVAILABLE");
				break;
			case "not_available" :
				asset.setState("NOT_AVAILABLE");
				break;
			default:
				throw new ResourceNotFoundException("Not found that state or state not available now");
		}


		asset.setCategory(categorySelected);
		AssetDto assetDto = modelMapper.map(this.assetRepository.save(asset), AssetDto.class);
		assetDto.setCategoryName(categorySelected.getCategoryName());
		return assetDto;
	}

	@Override
	public ResponseEntity<?> editAssetInfo(Integer id, AssetUpdateDTO assetUpdateDTO) {
		Optional<Asset> oldAsset = assetRepository.findById(id);
		if(!oldAsset.isPresent()) {
			throw new ResourceNotFoundException("Not found asset");
		}

		Asset asset = oldAsset.get();

		asset.setAssetName(assetUpdateDTO.getAssetName());
		asset.setSpecification(assetUpdateDTO.getSpecification());
		String state = assetUpdateDTO.getState().toLowerCase();

		switch (state) {
			case "available":
				asset.setState("AVAILABLE");
				break;
			case "not_available":
				asset.setState("NOT_AVAILABLE");
				break;
			case "waiting_for_recycling":
				asset.setState("WAITING_FOR_RECYCLING");
				break;
			case "recycled":
				asset.setState("RECYCLED");
				break;
			default:
				throw new ResourceNotFoundException("Not found that state or state not available now");
		}

		String newDate = assetUpdateDTO.getInstalledDate();
		Date installDate = myDateUtils.getDate(newDate);
		asset.setInstalledDate(installDate);
		Asset newAsset = this.assetRepository.save(asset);
		Date installedDateNewAsset = newAsset.getInstalledDate();
		System.out.println(installedDateNewAsset);
		List<Assignment> listAssignment = asset.getAssignments();

		listAssignment.forEach(assignment -> {
			if(assignment.getAssignedDate().getTime() < installedDateNewAsset.getTime() ){
				assignment.setAssignedDate(installedDateNewAsset);
				this.assignmentRepository.save(assignment);
			}
		});

		AssetDetailDTO assetDetailDTO = modelMapper.map(newAsset, AssetDetailDTO.class);
		assetDetailDTO.setCategoryName(newAsset.getCategory().getCategoryName());

		return ResponseEntity.ok(assetDetailDTO);
	}

	@Override
	public ResponseEntity<?> deleteAsset(int assetId)
	{
		Optional<Asset> assetOptional = this.assetRepository.findById(assetId);
		if(assetOptional.isPresent())
		{
			Asset asset = assetOptional.get();
			if(asset.getAssignments().isEmpty())
			{
				this.assetRepository.delete(asset);
				return ResponseEntity.ok(new MessageResponse("Delete Asset Successfully !"));
			}
			throw new ApiDeniedException("Cannot delete the asset because it belongs to one or more historical assignments. " +
					"If the asset is not able to be used anymore, please update its state in Edit Asset page” ");
		}
		throw new ResourceNotFoundException("Asset Not Found With ID : " + assetId);
	}

	public String generateCode(String prefix, String number)
	{
		String staffCodeBefore = prefix + number;
		List<Asset> listAssetSameKeys = this.assetRepository.getListAssetSameKeys(prefix);
		int staffCodeAdded = listAssetSameKeys.size() + 1; //If 0 then 1, If 1 then 2

		if(listAssetSameKeys.size() == 0 )
		{
			return prefix + "000001";
		}

		for(int i = staffCodeAdded; i >0; i /= 10)
		{
			staffCodeBefore = staffCodeBefore.substring(0, staffCodeBefore.length()-1); // If 1 then 0000
		}


		Asset lastAssetOfList = listAssetSameKeys.get(listAssetSameKeys.size()-1);
		String lastStaffCode = lastAssetOfList.getAssetCode();
		int staffCodeCompare = Integer.parseInt(lastStaffCode.replaceAll("[A-Z]+", ""));

		return staffCodeBefore + (staffCodeCompare + 1); // If 1 then 00001
	}

	@Override
	public ResponseEntity<?> getAllAssetAvailable() {
		List<Asset> entities = assetRepository.getListAssetAvalable();
		List<AssetDto> dto  = entities.stream().map(asset -> modelMapper.map(asset, AssetDto.class))
				.collect(Collectors.toList());
		for(int i = 0; i < dto.size(); i++) {
			dto.get(i).setCategoryName(entities.get(i).getCategory().getCategoryName());
		}
		return ResponseEntity.ok(dto);
	}


	public Pageable createPage(int page, String sortBy){

		char[] sortArr = sortBy.toCharArray();
		char sortCol = sortArr[0];
		char sortMode = sortArr[1];

		String sortName;
		Sort sort;

		switch (sortCol){
			case 'c':
				sortName = "assetCode";
				break;
			case 'n':
				sortName = "assetName";
				break;
			case 'e':
				sortName = "category.categoryName";
				break;
			case 's' :
				sortName = "state";
				break;
			default:
				throw new ResourceNotFoundException("NO COLUMN SORT FOUND !");
		}

		switch (sortMode){
			case 'a':
				sort = Sort.by(Sort.Direction.ASC, sortName);
				break;
			case 'd':
				sort = Sort.by(Sort.Direction.DESC, sortName);
				break;
			default:
				throw new ResourceNotFoundException("NO MODE SORT FOUND !");
		}

		return PageRequest.of(page,20,sort);

	}
}
