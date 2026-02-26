package com.example.demo.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PremiumMapper {
	// 계산된 심사 결과를 DB에 INSERT 하는 메서드
	void insertCalcLog(Map<String, Object> logData);
}
