package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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
	public Map<String, Object> calculatePremium(int age, String gender, String jobClass, String code, String diseaseHistory) {
		
		// 기본 보험료 세팅
		int basePremium = (age >= 50) ? 50000 : 30000;
		double surchargeRate = 1.0;
		String msg = (age>= 50) ? "[승인] 실버 플랜" : "[승인] 베이직 플랜";
		
		
		// 인수 거절 검사
		if(diseaseHistory.contains("H02") || diseaseHistory.contains("H03")) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("premium", 0);
			resultMap.put("resultMsg", "인수 거절: 중대 질환 또는 최근 수술/입원 이력 존재");
			return resultMap;
		}
		
		// ==========================================
        // 보험료 할증 검사
        // ==========================================
		
		// I10(고혈압)이면 30% 할증
		if("I10".equals(code)) {
			msg += "(고혈압 30% 할증)";
			surchargeRate += 0.3;
		}
		
		// 성별 M(남자)라면 10% 할증
		if("M".equals(gender)) {
			surchargeRate += 0.1;
		}
		
		// 직업 위험도 할증 (2급 : 10%, 3급: +20%)
		if("2".equals(jobClass)) {
			surchargeRate += 0.1;
		} else if("3".equals(jobClass)) {
			surchargeRate += 0.2;
		}
		
		// 최종 보험료 계산 (기본료 * 최종 할증률)
		int finalPremium = (int) (basePremium * surchargeRate);
		
		// 결과 반환
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
