package dp.fdis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDTO {

    /**
     * endLon
     */
    String endX;

    /**
     * endLat
     */
    String endY;

    /**
     * StartLon
     */
    String startX;

    /**
     * StartLat
     */
    String startY;

    String index; // 원래 tr의 index값 축출용

    int totalDistance; // 총 거리 (단위 m)
    int totalTime; //총 시간 (단위 : 초)


}
