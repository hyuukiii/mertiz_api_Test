package com.example.demo.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiseaseMapper {
	// Map 1개가 아닌 List<Map>을 통째로 받는다
	void insertDiseaseBatch(List<Map<String, Object>> diseaseList);
}
