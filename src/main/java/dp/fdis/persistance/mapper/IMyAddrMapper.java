package dp.fdis.persistance.mapper;

import dp.fdis.dto.MyAddrDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMyAddrMapper {

    //즐겨찾기 리스트
    List<MyAddrDTO> getMyAddrList(MyAddrDTO pDTO) throws Exception;

    //즐겨찾기 글 등록
    void insertMyAddr(MyAddrDTO pDTO) throws Exception;

    //즐겨찾기 상세보기
    MyAddrDTO getMyAddr(MyAddrDTO pDTO) throws Exception;

    //즐겨찾기 조회수 업데이트

    //즐겨찾기 글 수정
    void updateMyAddr(MyAddrDTO pDTO) throws Exception;

    //즐겨찾기 글 삭제
    void deleteMyAddr(MyAddrDTO pDTO) throws Exception;

}
