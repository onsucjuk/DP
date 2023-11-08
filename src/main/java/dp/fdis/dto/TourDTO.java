package dp.fdis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TourDTO {

    //--------------------------------------------------------------- TOUR_INFO

    private String tourSeq; // 여행 번호
    private String tourName; // 여행명
    private String tourProcess; // 여행 진행도
    private String tourStart; // 여행 시작일자
    private String tourEnd; // 여행 종료일자
    private String userId; // 아이디

    private String existsYn; // 존재 여부 확인


    //--------------------------------------------------------------- TOUR_DAY

    private String placeSeq; // 여행 방문지 순서 번호
    // 위 2개는 매퍼에서 자동 생성(mxa()+1)
    private String tourDay; // 여행 일자 <- 이건 일자 직접 등록 Day1(1), Day2(2) ...

    private String dayStart;

    //--------------------------------------------------------------- TOUR_PLACE

    private String placeNick; // 사용자 설정 목적지명
    private String placeName; // 목적지명
    private String placeAddr; // 목적지 주소
    private String memo; // 메모

    private String tourYn; // Y or N 여부 [default N]
    private int tourY; // Y의 갯수
    private int tourN; // N의 갯수
    private String lat; // 위도
    private String lon; // 경도
    private String lng; // 실시간 서울 도시 데이터의 경도


}
