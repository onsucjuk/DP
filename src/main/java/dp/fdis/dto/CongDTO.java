package dp.fdis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CongDTO {

    String poi; // POI
    String congesion; // 혼잡도
    String congestionLevel; // 혼잡도 레벨 1: 여유, 2: 보통, 3: 혼잡, 4: 매우 혼잡
    String type; // 1 장소, 2 주변

}
