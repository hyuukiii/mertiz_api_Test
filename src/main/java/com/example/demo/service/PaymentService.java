package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.PaymentMapper;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentMapper paymentMapper;
	
	// 결제 내역 조회
	public List<Map<String, Object>> getPaymentList(int page, int size, String userId) {
		
		// 페이징 계산
		int endRow = page * size;
		int startRow = (page -1) * size;
		
		// XML에 넘겨줄 파라미터들을 Map에 담는다
		Map<String, Object> params = new HashMap<>();
		params.put(("endRow"), endRow);
		params.put(("startRow"), startRow);
		params.put(("userId"), userId);
		
		return paymentMapper.getPayments(params);
	}
}
