package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.FreezerAssetsItem;
import com.pams.entity.FreezerAssetOrder;
import java.util.List;

public interface FreezerAssetsItemRepository extends JpaRepository<FreezerAssetsItem, Long> {
	
	List<FreezerAssetsItem> findByFreezerAssetOrder(FreezerAssetOrder freezerassetorder);

}
