package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.CustomerMapper;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerMapper customerMapper;
	
	
	// 고객 목록 조회
	public List<Map<String, Object>> getCustomerList() {
		return customerMapper.getCustomer();
	}
	
	
	// 고객 데이터 일괄 저장/수정/삭제
	@Transactional
	public void saveCustomerBatch(List<Map<String, Object>> customerList) {
		
		// 프론트에서 넘어온 데이터를 1개씩 확인
		for(Map<String, Object> customer : customerList) {
			String rowStatus = (String) customer.get("rowStatus");
			
			if("D".equals(rowStatus) || "E".equals(rowStatus)) {
				// 삭제 대상이면 -> Map에서 CustNo만 꺼내서 삭제
				String custNo = (String) customer.get("custNo");
				customerMapper.deleteCustomer(custNo);
			} else {
				// 추가/수정 대상 -> Map 통째로 던져서 병합
				customerMapper.upsertCustomer(customer);
			}
		}
	}
}
