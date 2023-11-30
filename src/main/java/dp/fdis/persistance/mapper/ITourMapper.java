package dp.fdis.persistance.mapper;

import dp.fdis.dto.TourDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITourMapper {

    List<TourDTO> getTourList(TourDTO pDTO) throws Exception;

    List<TourDTO> getTourEndList(TourDTO pDTO) throws Exception;

    //여행지 상세보기
    TourDTO getTourInfo(TourDTO pDTO) throws Exception;

    TourDTO getTourSeq(TourDTO pDTO) throws Exception;

    // 여행지 번호 있는지 조회
    TourDTO tourSeqExists(TourDTO pDTO) throws Exception;

    //여행지 등록
    void insertTourInfo(TourDTO pDTO) throws Exception;

    // 여행지 이름 수정
    void updateTourName(TourDTO pDTO) throws Exception;

    //여행 시작
    void updateTourDays(TourDTO pDTO) throws Exception;

    void resetTourStart(TourDTO pDTO) throws Exception;

    //여행 종료
    void updateTourEndAdd(TourDTO pDTO) throws Exception;
    void updateTourEndSub(TourDTO pDTO) throws Exception;

    //진행도 갱신
    void updateTourProcess(TourDTO pDTO) throws Exception;

    //여행지 삭제
    void deleteTourInfo(TourDTO pDTO) throws Exception;

    /**
     *  여기서부터 TourDay Mapper
     */

    // Day 숫자 Count
    int countDay(TourDTO pDTO) throws Exception;

    // 여행 진행도 장소 방문 완료 수
    /*    TourDTO getTourYn(TourDTO pDTO) throws Exception;*/
    TourDTO getTourDayYn(TourDTO pDTO) throws Exception;

    // 여행 일자 리스트
    List<TourDTO> getTourDayList(TourDTO pDTO) throws Exception;

    //여행 일자 상세보기
    TourDTO getTourDayInfo(TourDTO pDTO) throws Exception;

    TourDTO getTourNameExists(TourDTO pDTO) throws Exception;

    TourDTO getTourEditExists(TourDTO pDTO) throws Exception;

    //여행 일자 등록
    void addTourDay(TourDTO pDTO) throws Exception;

    // 방문 완료 체크
    void updateTourDayYN(TourDTO pDTO) throws Exception;

    // 여행 시작일자 등록
    void updateTourDaySt(TourDTO pDTO) throws Exception;

    void resetTourDaySt(TourDTO pDTO) throws Exception;

    void deleteTourDayAll(TourDTO pDTO) throws Exception;

    void deleteTourDay(TourDTO pDTO) throws Exception;


    /**
     *  여기서부터 TourPlace Mapper
     */

    List<TourDTO> getTourPlace(TourDTO pDTO) throws Exception;

    TourDTO getTourPlaceOne(TourDTO pDTO) throws Exception;

    void deleteDayPlace(TourDTO pDTO) throws Exception;
    void deleteTourPlace(TourDTO pDTO) throws Exception;

    void deleteTourPlaceOne(TourDTO pDTO) throws Exception;

    void insertTourPlace(TourDTO pDTO) throws Exception;

    void updateTourPlace(TourDTO pDTO) throws Exception;
    void updatePlaceEnd(TourDTO pDTO) throws Exception;
    void resetPlaceEnd(TourDTO pDTO) throws Exception;





}
