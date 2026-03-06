package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 기본 생성자 ( 웹스퀘어 JSON 변환 시 필수 )
@AllArgsConstructor // 모든 필드를 갖춘 생성자( 빌더 작동 시 필수 )
public class InsuInfoDTO {
	private String gender;
	private Integer age;
	private String jobClass;
	private String diseaseCode;
	private String diseaseHistory;
	
	private Double height;
	private Double weight;
	private String smokeYn;
	private String drinkFreq;
	
	// 계산 결과값을 담을 필드
	private Integer premium;
	private String resultMsg;
}
