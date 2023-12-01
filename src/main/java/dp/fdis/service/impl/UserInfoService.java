package dp.fdis.service.impl;

import dp.fdis.dto.MailDTO;
import dp.fdis.dto.UserInfoDTO;
import dp.fdis.persistance.mapper.IUserInfoMapper;
import dp.fdis.service.IMailService;
import dp.fdis.service.IUserInfoService;
import dp.fdis.util.CmmUtil;
import dp.fdis.util.DateUtil;
import dp.fdis.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService implements IUserInfoService {

    private final IUserInfoMapper userInfoMapper; // 회원 관련 SQL 사용하기 위한 Mapper 가져오기

    private final IMailService mailService; // 메일 발송을 위한 MailService 자바 객체 가져오기
    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + "getUserIdExists Start!");

        UserInfoDTO rDTO = userInfoMapper.getUserIdExists(pDTO);

        log.info(this.getClass().getName() + "getUserIdExists End!");

        return rDTO;
    }

    @Override
    public UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".emailAuth Start!");

        // DB 이메일이 존재하는지 SQL 쿼리 실행
        // SQL 쿼리에 COUNT()를 사용하기 때문에 반드시 조회 결과 존재
        UserInfoDTO rDTO = userInfoMapper.getEmailExists(pDTO);
        log.info("rDTO : " + rDTO);

        String existsYn = CmmUtil.nvl(rDTO.getExistsYn());
        log.info("existsTn : " + existsYn);

        if(existsYn.equals("N")) {
            // 6자리 랜덤 숫자 생성하기

            int authNumber = ThreadLocalRandom.current().nextInt(100000,1000000);

            log.info("authNumber : " + authNumber);

            // 인증번호 발송 로직
            MailDTO dto = new MailDTO();

            dto.setTitle("이메일 중복 확인 인증번호 발송 메일");
            dto.setContents("인증번호는 " + authNumber + "입니다.");
            dto.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mailService.doSendMail(dto); // 메일 발송

            //메일 변수값 초기화
            dto = null;
            rDTO.setAuthNumber(authNumber); // 인증번호를 결과값에 넣어주기

        }

        log.info(this.getClass().getName() + ".emailAuth End!");

        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + "insertUserInfo Start!");

        //회원가입 성공 : 1, 아이디 중복으로인한 가입 취소 : 2, 기타 에러 발생 : 0
        int res = 0;

        // 회원가입
        int successs = userInfoMapper.insertUserInfo(pDTO);

        if (successs > 0) {
            res = 1;

            MailDTO mDTO = new MailDTO();

            //회원정보화면에서 입력받은 이메일 변수(아직 암호화되어 넘어오기 때문에 복호화 수행함)
            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mDTO.setTitle("회원가입을 축하드립니다."); // 제목

            //메일 내용에 가입자 이름넣어서 내용 발송
            mDTO.setContents(CmmUtil.nvl(pDTO.getUserName()) + "님의 회원가입을 진심으로 축하드립니다.");

            //회원 가입이 성공했기 때문에 메일을 발송함
            mailService.doSendMail(mDTO);

        } else {
            res = 0;
        }

        log.info(this.getClass().getName() + ".insertUserInfo End!");
        return res;
    }

    @Override
    public List<UserInfoDTO> getUserList() throws Exception {

        log.info(this.getClass().getName() + ".getUserList start!");

        return userInfoMapper.getUserList();

    }

    @Override
    public UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserInfo start!");

        return userInfoMapper.getUserInfo(pDTO);
    }

    @Override
    public UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getLogin Start!");

        // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기 위한 mapper 호출하기
        // userInfoMapper.getUserLoginCheck(pDTO) 함수 실행 결과가 NULL 발생하면, UserInfoDTO 메모리에 올리기

        UserInfoDTO rDTO = Optional.ofNullable(userInfoMapper.getLogin(pDTO)).orElseGet(UserInfoDTO::new);

        if(CmmUtil.nvl(rDTO.getUserId()).length() > 0) {
            MailDTO mDTO = new MailDTO();

            // 아이디, 패스워드 일치 체크 -> 이메일 값 받아오기 (복호화 필요)
            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(rDTO.getEmail())));
            mDTO.setTitle("로그인 알림!"); // 제목

            //메일 내용에 가입자 이름 넣어서 내용 발송
            mDTO.setContents(DateUtil.getDateTime("yyyy.MM.dd hh:mm:ss") + "에 " + CmmUtil.nvl(rDTO.getUserName()) + "님이 로그인하였습니다.");

            //회원 가입이 성공했기 때문에 메일을 발송함
            mailService.doSendMail(mDTO);

        }

        log.info(this.getClass().getName() + ".getLogin End!");


        return rDTO;
    }

    @Override
    public UserInfoDTO searchUserIdOrPasswordPro(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc Start!");

        UserInfoDTO rDTO = userInfoMapper.getUserId(pDTO);

        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc End!");

        return rDTO;
    }

    @Override
    public int newPasswordProc(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".newPasswordProc Start!");

        // 비밀번호 재설정
        int success = userInfoMapper.updatePassword(pDTO);

        log.info(this.getClass().getName() + ".newPasswordProc END!");

        return success;
    }

    @Override
    public UserInfoDTO sendEmailAuth(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".sendEmailAuth Start!");

        // DB 이메일이 존재하는지 SQL 쿼리 실행
        // SQL 쿼리에 COUNT()를 사용하기 때문에 반드시 조회 결과 존재
        UserInfoDTO rDTO = userInfoMapper.getEmailExists(pDTO);
        log.info("rDTO : " + rDTO);

        String existsYn = CmmUtil.nvl(rDTO.getExistsYn());
        log.info("existsTn : " + existsYn);

        if(existsYn.equals("Y")) {
            // 6자리 랜덤 숫자 생성하기

            int authNumber = ThreadLocalRandom.current().nextInt(100000,1000000);

            log.info("authNumber : " + authNumber);

            // 인증번호 발송 로직
            MailDTO dto = new MailDTO();

            dto.setTitle("비밀번호 변경 이메일 확인 인증번호 발송 메일");
            dto.setContents("인증번호는 " + authNumber + "입니다.");
            dto.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mailService.doSendMail(dto); // 메일 발송

            //메일 변수값 초기화
            dto = null;
            rDTO.setAuthNumber(authNumber); // 인증번호를 결과값에 넣어주기

        }

        log.info(this.getClass().getName() + ".sendEmailAuth End!");

        return rDTO;
    }

    @Override
    public void updateUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateUserInfo Start!");

        userInfoMapper.updateUserInfo(pDTO);

    }


}
