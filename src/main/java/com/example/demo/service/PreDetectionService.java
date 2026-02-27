package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.PremiumMapper;

@Service
public class PreDetectionService {
	
	@Autowired
	private PremiumMapper premiumMapper;
	
	/**
	 * [비즈니스 로직] 보험료 산출 및 심사
	 * @param age 고객 나이
	 * @param code 질병 코드
	 * @return Map<String, Object> 계산된 보험료와 결과 메시지
	 */
	public Map<String, Object> calculatePremium(int age, String code) {
		int finalPremium = 0;
		String msg = "";
		
		// 비즈니스 로직
		if("I10".equals(code)) {
			msg = "심사 거절 대상자(고혈압)";
			finalPremium = 0;
		} else {
			if (age >= 50) {
				finalPremium = 50000;
				msg = "심사 승인 : 실버 플랜";
			} else {
				finalPremium = 30000;
				msg = "심사 승인 : 베이직 플랜";
			}
		}
		
		// 계산 결과를 Map에 담아 반환
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("premium", finalPremium);
		resultMap.put("resultMsg", msg);
		
		return resultMap;
	}
	
	/**
	 * 화면에서 넘어온 심사계산 결과를 DB에 저장
	 */
	@Transactional
	public void savePremiumLog(Map<String, Object> logData) { 
		premiumMapper.insertCalcLog(logData); 
	}
	
}	
