package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
// 결제 내역 조회
List<Map<String, Object>> getPayments(Map<String, Object> param);
	
}
