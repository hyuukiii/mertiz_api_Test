package com.example.demo.service.factory;

import org.springframework.stereotype.Component;

import dto.InsuInfoDTO;
/** 50세 미만용 심사 계산기 **/
@Component("basicCalculator") // 스프링 빈으로 등록하기
public class BasicPlanCalculator implements PremiumCalculator {
	
	@Override
	public void calculate(InsuInfoDTO dto) {
		int basePremium = 30000;
		double surchargeRate = 1.0;
		
		String msg = "[승인] 베이직 플랜";
		
		// BMI 계산 (몸무게 / 키(m)의 제곱)
		double heightInMeter = dto.getHeight() / 100.0;
		double bmi = dto.getWeight() / (heightInMeter * heightInMeter);
		
		
		// 다중 체크박스 null 포인터 방지
		String history = dto.getDiseaseHistory() != null ? dto.getDiseaseHistory() :  "";
				
		// ==========================================
		// 심사 거절 대상 ( H02[1년 내 수술/입원], H03[5년 내 암 등 중대질환] 이력이 있으면 무조건 거절 )
		// ==========================================
		if(history.contains("H02") || history.contains("H03")) {
			dto.setPremium(0);
			dto.setResultMsg("인수 거절: 중대 질환 또는최근 수술.입원 이력 존재");
			return;
		}
				
		// 고도비만(BMI 35 이상) 또는 극저체중(BMI 15 미만)은 가입 거절
		if(bmi >= 35.0 || bmi < 15.0) {
			dto.setPremium(0);
			dto.setResultMsg(String.format("인수 거절 : BMI 수치 이상(BMI: %.1f)", bmi));
			return;
		}
		
		// ==========================================
        // 보험료 할증 룰
        // ==========================================
		
		// I10(고혈압)이면 30% 할증
		if("I10".equals(dto.getDiseaseCode())) { msg += "(고혈압 30% 할증)"; surchargeRate += 0.3; }
		
		// 성별 M(남자)라면 10% 할증
	    if("M".equals(dto.getGender())) { surchargeRate += 0.1;}
	    
		// 생활습관 할중
		if("Y".equals(dto.getSmokeYn())) { msg += " (+흡연)"; surchargeRate += 0.1; }
		if("HIGH".equals(dto.getDrinkFreq())) { msg += " (+잦은음주)"; surchargeRate += 0.1; }
		
		// 경도비만 (BMI 30 이상 ~ 35 미만) 할증
		if(bmi >= 30.0 && bmi < 35.0) { msg += " (+비만할증)"; surchargeRate += 0.1; }
		
		
		// 직업 위험도 할증 (2급 : 10%, 3급: +20%)
		if("2".equals(dto.getJobClass())) { surchargeRate += 0.1; }
		 else if("3".equals(dto.getJobClass())) { surchargeRate += 0.2; }
		
		// 최종 보험료 계산 (기본료 * 최종 할증률)
		dto.setPremium((int) (basePremium * surchargeRate));
		dto.setResultMsg(msg);
	}
	
}
