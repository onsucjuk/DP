package dp.fdis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImgDTO {

    //naver Object Storage의 파일 주소
    private String uploadFileUrl;
    // 업로드 할 때 파일 이름
    private String uploadFileName;
    private String imgSeq;
    private String userId;
    private String tourSeq;
    private String tourDay;
    private String placeSeq;
    private String title;
    private String contents;
    private String regId;
    private String regDt;
    private String chgId;
    private String chgDt;
    private int likeCnt;
    // 메타 데이터 확인용 lat, lon
    private double lat;
    private double lon;
    private String imgURL;

    // 장소 사진 등록 여부 1이상이면 존재
    private int existCnt;


}
