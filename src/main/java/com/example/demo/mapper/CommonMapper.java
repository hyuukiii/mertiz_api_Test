package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

	// 그룹코드를 던져주면 해당 그룹의 코드 목록 가져오기
	List<Map<String, Object>> getCommonCodeList(String grpCd);
}
