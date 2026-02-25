package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CommonMapper;

@Service
public class CommonService {
	
	
	@Autowired
	private CommonMapper commonMapper;
	
	
	//xml파일에 작성 된 쿼리문으로 DB 내 테이블의 공통코드를 받아오고 mapper interface에 저장 후 서비스에 다시 담는다
	public List<Map<String, Object>> getCommonList(String grpCd) { return commonMapper.getCommonCodeList(grpCd); }

}
