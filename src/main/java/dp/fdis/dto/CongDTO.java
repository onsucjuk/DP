package dp.fdis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CongDTO {

    String poi; // POI

    String lat; // 위도
    String lng; // 경도
    int type; // 1 장소, 2 주변
    double congestion; // 혼잡도
    int congestionLevel; // 혼잡도 레벨 1: 여유, 2: 보통, 3: 혼잡, 4: 매우 혼잡
    String message; // 성공 여부

}
