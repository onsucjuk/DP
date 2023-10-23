package dp.fdis.service;

import dp.fdis.dto.MailDTO;
import dp.fdis.dto.NoticeDTO;
import dp.fdis.dto.UserInfoDTO;

import java.util.List;

public interface IUserInfoService {

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    List<UserInfoDTO> getUserList() throws Exception;

    /**
     * 유저정보 상세보기
     *
     * @param pDTO 상세내용 조회할 userId 값
     * @return 조회 경과
     */
    UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    // 아이디, 비밀번호 찾기에 활용
    UserInfoDTO searchUserIdOrPasswordPro(UserInfoDTO pDTO) throws Exception;

    int newPasswordProc(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO sendEmailAuth(UserInfoDTO pDTO) throws Exception;

}
