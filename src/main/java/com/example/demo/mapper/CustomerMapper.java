package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper {

// 고객 목록 조회 메뉴
List<Map<String, Object>> getCustomer();

// 고객 정보 저장(추가/수정) 메뉴 - MERGE INTO
void upsertCustomer(Map<String, Object> customer);

// 고객 정보 삭제 메뉴
void deleteCustomer(String custNo);
	
}
