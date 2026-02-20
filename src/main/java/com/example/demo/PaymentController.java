package com.example.demo;

import java.util.ArrayList;
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

@CrossOrigin(origins = "*")
@RestController
public class PaymentController {
	
@Autowired
private JdbcTemplate jdbcTemplate;

//API 주소매핑 (GET)
@GetMapping("/api/payments")
public List<Map<String, Object>> getPayments(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "100") int size) {
	
	// 페이징 계산 : 1페이지면 0 건너뜀, 2페이지면 100건 건너뜀
	int endRow = page * size;
	int startRow = (page - 1) * size;
	
	
	// 3단 서브 쿼리
	String sql = "SELECT * " +
				 "FROM (" +
			     "    SELECT ROWNUM AS RNUM, A.* " +
				 "    FROM (" +
			     "         SELECT PAY_ID, USER_ID, AMOUNT, PAY_DATE, STATUS " +
				 "         FROM PAYMENT_LOG " +
			     "         ORDER BY PAY_DATE DESC" +
				 "   )  A " +
			     "   WHERE ROWNUM <= ?" + //첫 번째 ? = endRow
				 ") " +
			     "WHERE RNUM > ?"; // 두번째 ? = startRow
	
	return jdbcTemplate.queryForList(sql,endRow, startRow);
				 		
 }


@PostMapping("/api/saveBatch")
public Map<String, Object> saveBatch(@RequestBody Map<String, List<Map<String, Object>>> payload) {
	// 1. 클라이언트가 보낸 데이터 리스트 꺼내기
	List<Map<String, Object>> customerList = payload.get("dlt_customerList");
	
	// 삭제할 데이터와 저장할 데이터를 담을 변수
	List<Object[]> upsertArgs = new ArrayList<>(); // 저장 할 리스트
	List<Object[]> deleteArgs = new ArrayList<>(); // 삭제 할 리스트
	
	
	// 반복문을 돌며 rowStatus 값에 따라 분류
	for(Map<String, Object> customer : customerList) {
		// 클라이언트에서 넣어둔 상태값(rowStatus) 꺼내기
		String rowStatus = (String) customer.get("rowStatus");
		
		if("D".equals(rowStatus) || "E".equals(rowStatus)) {
			deleteArgs.add(new Object[] { customer.get("custNo") });
		} else {
			// 저장/수정 대상 ( C, U, R)
			upsertArgs.add(new Object[] {
				customer.get("custNo"),
				customer.get("name"),
				customer.get("age"),
				customer.get("code"),
				customer.get("result"),
				customer.get("custNo"),
				customer.get("name"),
				customer.get("age"),
				customer.get("code"),
				customer.get("result")
			});
		}
	}
	
	// [저장/수정 쿼리 실행]
	if(!upsertArgs.isEmpty()) {
		// 2. DB에 인서트 할 SQL 준비
		String sql = "MERGE INTO CUSTOMER_INSURANCE T " +
					 "USING DUAL " +
				     "ON (T.CUST_NO = ?) " + // 고유키로 매칭
					 "WHEN MATCHED THEN " +  // 있으면 수정
				     "     UPDATE SET T.NAME = ?, T.AGE = ?, T.DISEASE_CODE = ?, T.RESULT_MSG = ? " +
					 "WHEN NOT MATCHED THEN " + // 없으면 추가
				     "     INSERT (CUST_NO, NAME, AGE, DISEASE_CODE, RESULT_MSG) " +
					 "     VALUES(?, ?, ?, ?, ?)";
		
		jdbcTemplate.batchUpdate(sql, upsertArgs);
	}
	
	// 삭제 쿼리 실행
	if(!deleteArgs.isEmpty()) {
		String deleteSql = "DELETE FROM CUSTOMER_INSURANCE WHERE CUST_NO = ?";
		jdbcTemplate.batchUpdate(deleteSql, deleteArgs);
	}
	
	// 프론트에 성공 응답 보내기
	Map<String, Object> response = new HashMap<>();
	response.put("status", "SUCCESS");
	response.put("message", "저장 및 삭제 처리가 완벽하게 완료되었습니다");
	
	return response;
	
	}

@GetMapping("/api/customers")
public Map<String, Object> getCustomers() {
	
	String sql = "SELECT CUST_NO AS \"custNo\", " +
				 "		 NAME AS \"name\", " +
			     "       AGE  AS \"age\", "  +
				 "       DISEASE_CODE AS \"code\", " +
				 "       RESULT_MSG AS \"result\" "  +
				 "FROM CUSTOMER_INSURANCE " +
				 "ORDER BY CUST_NO ASC"; //번호순 정렬
	
	List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	
	Map<String, Object> resultMap = new HashMap<>();
	resultMap.put("dlt_customerList", list);
	
	return resultMap;
					 
	}

}