package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.PreDetectionService;

@CrossOrigin(origins = "*")
@RestController
public class PreDetectionController {

	@Autowired
	private PreDetectionService preDetectionService; 
	
	
	/*
	 * [API] 보험료 간편 심사 요청
	 * @param requestData 웹스퀘어에서 전송한 고객 정보 (Key: dma_insuInfo)
	 */
	@PostMapping("/api/calculate")
	public Map<String, Object> calcuatePremium(@RequestBody Map<String, Object> requestData) {
		
		
		// 클라이언트에서 온 데이터의 Map 형태의 데이터셋을 해제 시킴
		@SuppressWarnings("unchecked")
		Map<String, Object> insuInfo = (Map<String, Object>) requestData.get("dma_insuInfo");
		
		// 나이,질병 코드 추출
		int age = Integer.parseInt(insuInfo.get("age").toString());
		String code = (String) insuInfo.get("diseaseCode");
		
		// 서비스 레이어에서 결과를 받아온다.
		Map<String, Object> calcResult = preDetectionService.calculatePremium(age, code);
		
		// 서비스가 준 결과를 웹스퀘어 insuInfo에 추가
		insuInfo.put("premium", calcResult.get("premium"));
		insuInfo.put("resultMsg", calcResult.get("resultMsg"));
		
		// 웹스퀘어 데이터리스트 이름에 맞게 수정 하여 리턴
		Map<String, Object> response = new HashMap<>();
		response.put("dma_insuInfo", insuInfo);
		
		return response;
		
	}
}
