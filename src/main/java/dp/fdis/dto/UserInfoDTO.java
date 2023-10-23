package dp.fdis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//Json결과값 변환 필요할 때 : 값이 존재하는 것만 Json으로 변환
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserInfoDTO {

    private String userId;
    private String userName;
    private String password;
    private String email;
    private String addr1;
    private String addr2;
    private String regId;
    private String regDt;
    private String chgId;
    private String chgDt;

    // 회원 가입 시 , 중복 가입을 방지하기 위해 사용할 변수
    // DB를 조회해서 회원이 존재하면 Y반환
    // 테이블에 존재하지 않는 가상 칼럼(ALIAS)

    private String existsYn;
    private String Cnt;

    // 이메일 중복체크를 위한 인증번호
    private int authNumber;
}
