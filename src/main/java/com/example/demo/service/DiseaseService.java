package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.DiseaseMapper;

@Service
public class DiseaseService {
	
	@Autowired
	private DiseaseMapper diseaseMapper;
	
	// @Transactional을 붙이면 3건 저장 중 1건이라도 에러 나면 3건 다 취소
	@Transactional
	public void saveDiseaseList(List<Map<String, Object>> diseaseList) {
			diseaseMapper.insertDiseaseBatch(diseaseList);
	}
	
}
