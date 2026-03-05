package dto;

import lombok.Data;

@Data
public class InsuInfoDTO {
	private String gender;
	private int age;
	private String jobClass;
	private String diseaseCode;
	private String diseaseHistory;
	
	private double height;
	private double weight;
	private String smokeYn;
	private String drinkFreq;
	
	// 계산 결과값을 담을 필드
	private int premium;
	private String resultMsg;
}
