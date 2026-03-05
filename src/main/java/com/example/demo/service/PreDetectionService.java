package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import dto.InsuInfoDTO;

import com.example.demo.mapper.PremiumMapper;
import com.example.demo.service.factory.PremiumCalculator;
import com.example.demo.service.factory.PremiumCalculatorFactory;

@Service
public class PreDetectionService {
	
	@Autowired
	private PremiumMapper premiumMapper;
	
	@Autowired
	private PremiumCalculatorFactory calculatorFactory;
	
	/**
	 * [비즈니스 로직] 보험료 산출 및 심사
	 * @param InsuInfoDTO dto
	 */
	public void calculatePremium(InsuInfoDTO dto) {
		
		// 클라이언트에게 받아온 나이에 맞는 클래스를 지정한다.
		PremiumCalculator calculator = calculatorFactory.getCalculator(dto.getAge());
		
		calculator.calculate(dto);
		
	}
	
	/**
	 * 화면에서 넘어온 심사계산 결과를 DB에 저장
	 * @param InsuInfo dto
	 */
	@Transactional
	public void savePremiumLog(InsuInfoDTO dto) { 
		premiumMapper.insertCalcLog(dto); 
	}
	
}	
