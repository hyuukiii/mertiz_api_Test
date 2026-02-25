package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.CustomerService;
import com.example.demo.service.DiseaseService;
import com.example.demo.service.PaymentService;

@CrossOrigin(origins = "*")
@RestController
public class PaymentController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private DiseaseService diseaseService;
	
	//결제 내역 조회 및 페이징
	@GetMapping("/api/payments")
	public List<Map<String, Object>> getPayments(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "100") int size,
			@RequestParam(required = false) String userId) {
		
		
		// 받은 파라미터를 서비스에 리턴 한다
		return paymentService.getPaymentList(page, size, userId);
					 		
	}
	
	
	@PostMapping("/api/saveBatch")
	public Map<String, Object> saveBatch(@RequestBody Map<String, List<Map<String, Object>>> payload) {
		// 클라이언트가 보낸 데이터 리스트 받기
		List<Map<String, Object>> customerList = payload.get("dlt_customerList");
		
		// 서비스 레이어 메서드 호출
		customerService.saveCustomerBatch(customerList);
		
		// 작업 처리 후 성공 메시지 클라이언트에게 전송
		Map<String, Object> response = new HashMap<>();
		response.put("status", "SUCCESS");
		response.put("message", "저장 및 삭제 처리가 완벽하게 완료되었습니다.");
		
		
		return response;
	}
	
	@GetMapping("/api/customers")
	public Map<String, Object> getCustomers() {
		
		// 고객 명단 가져오기
		List<Map<String, Object>> list = customerService.getCustomerList();
		
		// 클라이언트가 기다리는 상자에 담아서 서빙
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dlt_customerList", list);
		
		return resultMap;
						 
	}
	
	@PostMapping("/api/disease")
	public Map<String, Object> saveDiseaseHistory(@RequestBody Map<String, Object> requestData) {
		/* 1. 일단 프론트에서 넘어오는 중괄호 {}를 Map으로 통째로 받는다.
		   2. 박스 안에 웹스퀘어 DataList 이름("dlt_diseaseList")표가 붙은 리스트 알맹이만 쏙 꺼낸다. */
		List<Map<String, Object>> diseaseList = (List<Map<String, Object>>) requestData.get("dlt_diseaseList");
		
		if(diseaseList != null && !diseaseList.isEmpty()) {
			diseaseService.saveDiseaseList(diseaseList);
		}
		
	    //프론트엔드로 성공 메시지 반환
	    Map<String, Object> response = new HashMap<>();
	    response.put("status", "SUCCESS");
	    response.put("message", "질병 정보 " + diseaseList.size() + "건 저장 완료");
	    
		return response;
	}
 }