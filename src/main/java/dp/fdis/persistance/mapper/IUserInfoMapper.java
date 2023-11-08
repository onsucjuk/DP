package dp.fdis.persistance.mapper;

import dp.fdis.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IUserInfoMapper {

    // 회원 가입하기
    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    // 회원 가입 전 아이디 중복 체크(DB조회)
    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    // 회원 가입 전 이메일 중복 체크(DB조회)
    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;

    List<UserInfoDTO> getUserList() throws Exception;

    UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserId(UserInfoDTO pDTO) throws Exception;

    int updatePassword(UserInfoDTO pDTO) throws  Exception;
}
