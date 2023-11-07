package dp.fdis.service;

import dp.fdis.dto.TourDTO;

import java.util.List;

public interface ITourInfoService {
    List<TourDTO> getTourList(TourDTO pDTO) throws Exception;

    TourDTO getTourNameExists(TourDTO pDTO) throws Exception;

    TourDTO getTourSeq(TourDTO pDTO) throws Exception;

    List<TourDTO> getTourYn(TourDTO pDTO) throws Exception;

    //여행지 상세보기
    TourDTO getTourInfo(TourDTO pDTO) throws Exception;

    //여행지 등록
    void insertTourInfo(TourDTO pDTO) throws Exception;

    // 여행지 이름 수정
    void updateTourName(TourDTO pDTO) throws Exception;

    //여행 시작
    void updateTourStart(TourDTO pDTO) throws Exception;

    //여행 종료
    void updateTourEnd(TourDTO pDTO) throws Exception;

    //진행도 갱신
    void updateTourProcess(TourDTO pDTO) throws Exception;

    //여행지 삭제
    void deleteTourInfo(TourDTO pDTO) throws Exception;

    /**
     *  여기서부터 TourDay Service
     */

    // 여행 일수, 진행도
    List<TourDTO> getTourDay(TourDTO pDTO) throws Exception;

    //여행 일자 상세보기
    TourDTO getTourDayInfo(TourDTO pDTO) throws Exception;

    //여행 일자 등록
    void addTourDay(TourDTO pDTO) throws Exception;

    // 여행 시작일자 등록
    void updateTourDaySt(TourDTO pDTO) throws Exception;


    void deleteTourDayAll(TourDTO pDTO) throws Exception;


    // 여행 일자 삭제
    void deleteTourDay(TourDTO pDTO) throws Exception;

    /**
     *  여기서부터 TourPlace Service
     */


    // 여행 목적지 삭제
    void deleteTourPlace(TourDTO pDTO) throws Exception;

    // 여행 목적지 등록
    void insertTourPlace(TourDTO pDTO) throws Exception;

    void insertTourPlaceMany(TourDTO pDTO) throws Exception;

    // 여행 목적지 수정
    void updateTourPlace(TourDTO pDTO) throws Exception;

    // 여행 메모 수정
    void updateTourDayMemo(TourDTO pDTO) throws Exception;

    // 방문 완료 체크
    void updateTourDayYN(TourDTO pDTO) throws Exception;

}