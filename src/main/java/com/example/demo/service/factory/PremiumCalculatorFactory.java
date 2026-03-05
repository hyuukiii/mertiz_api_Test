package com.example.demo.service.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PremiumCalculatorFactory {
	
	@Autowired
	@Qualifier("basicCalculator")
	private PremiumCalculator basicCalculator;
	
	@Autowired
	@Qualifier("silverCalculator")
	private PremiumCalculator silverCalculator;
	
	// 나이에 따라 정해지는 객체가 달라짐
	public PremiumCalculator getCalculator(int age) {
		if(age >= 50) {
			return silverCalculator;
		}
		return basicCalculator;
	}
}
