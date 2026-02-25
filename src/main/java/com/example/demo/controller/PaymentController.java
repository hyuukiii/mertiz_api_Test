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

import com.example.demo.service.CommonService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.DiseaseService;
import com.example.demo.service.PaymentService;

/**
 * [시스템명] 	: 메리츠화재 시스템 개발
 * [업무명] 	: 보험료 간편 심사 및 고객/결제 내역 관리 Controller
 * @author 윤현기
 * @since 2026.02.25
 * @description 웹스퀘어 화면과 통신하여 고객 목록, 결제 내역, 질병 이력을 처리하는 REST API 컨트롤러
 */
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
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * [트랜잭션] 상단 그리드 (고객 목록) 다건 데이터 일괄 C/U/D
	 * 웹스퀘어의 rowStatus에 따라 추가, 수정, 삭제를 처리
	 * * @param payload 웹스퀘어 서브미션에서 전송한 고객 데이터 객체 (Key: dlt_customerList)
	 * @return Map<String, Object> 처리 성공 여부 및 결과 메시지
	 */
	@PostMapping("/api/saveBatch")
	public Map<String, Object> saveBatch(@RequestBody Map<String, List<Map<String, Object>>> payload) {
		// 클라이언트가 보낸 데이터 리스트 받기
		List<Map<String, Object>> customerList = payload.get("dlt_customerList");
		
		// 서비스 레이어 메서드 호출
		customerService.saveCustomerBatch(customerList);
		
		// 작업 처리 후 성공 메시지 클라이언트에게 전송
		Map<String, Object> response = new HashMap<>();
		response.put("status", "SUCCESS");
		response.put("message", "저장 및 삭제 처리가 완료되었습니다.");
		
		
		return response;
	}
	
	
	/**
	 * [조회] 상단 그리드 (고객 목록) 전체 조회
	 * * @return Map<String, Object> 웹스퀘어 DataList 매핑용 고객 데이터 목록 (Key: dlt_customerList)
	 */
	@GetMapping("/api/customers")
	public Map<String, Object> getCustomers() {
		
		// 고객 명단 가져오기
		List<Map<String, Object>> list = customerService.getCustomerList();
		
		// 클라이언트가 기다리는 상자에 담아서 서빙
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dlt_customerList", list);
		
		return resultMap;
						 
	}
	
	/**
	 * [조회] 상단 그리드 (고객 목록) 질병 공통 코드 불러오기
	 * * @return Map<String, Object> 웹스퀘어 DataList 매핑용 고객 데이터 목록 (Key: dlt_disaseMaster)
	 */
	@GetMapping("/api/disease-master")
	public Map<String, Object> getDiseaseMaster() {
		
		// 서비스에서 순수 리스트를 가져온다
		List<Map<String, Object>> list = commonService.getCommonList("DISEASE");
		
		// 웹스퀘어의 데이터리스트(dlt_diseasemaster)에 기대하는 타입 Map이므로 변환한다.
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dlt_diseaseMaster", list);
		
		return resultMap;
		
	}
	
	
	
	/**
	 * [조회] 하단 그리드 (고객 결제 내역) 페이징 조회
	 * * @param page 현재 페이지 번호 (기본값: 1)
	 * @param size 페이지당 노출 데이터 건수 (기본값: 100)
	 * @param userId 조회할 특정 고객의 ID (조건 검색 시 사용)
	 * @return List<Map<String, Object>> 결제 내역 목록 리스트
	 */
	@GetMapping("/api/payments")
	public List<Map<String, Object>> getPayments(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "100") int size,
			@RequestParam(required = false) String userId) {
		
		
		// 받은 파라미터를 서비스에 리턴 한다
		return paymentService.getPaymentList(page, size, userId);
					 		
	}
	
	
	/**
	 * [트랜잭션] 알릴의무 질병 이력 다건 일괄 저장 (Bulk Insert)
	 * * @param requestData 웹스퀘어 서브미션에서 전송한 질병 이력 데이터 객체 (Key: dlt_diseaseList)
	 * @return Map<String, Object> 처리 성공 여부 및 저장 건수 메시지
	 */
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