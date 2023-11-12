package dp.fdis.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dp.fdis.dto.CongDTO;
import dp.fdis.service.ICongService;
import dp.fdis.util.CmmUtil;
import dp.fdis.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CongService implements ICongService {

    @Value("${tMap.api.key}")
    private String apiKey;

    private Map<String, String> setTMapInfo() {
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("appkey", apiKey);

        log.info("appKey : " + apiKey);

        return requestHeader;
    }
    @Override
    public CongDTO getCong(CongDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getCong Start!");

        // 컨트롤러에서 받은 POI, 위도, 경도 값

        String poi = CmmUtil.nvl(pDTO.getPoi());
        String lat = CmmUtil.nvl(pDTO.getLat());
        String lng = CmmUtil.nvl(pDTO.getLng());

        String apiParam = poi + "?lat=" + lat + "&lng=" + lng;

        log.info("apiParam : " + apiParam);

        String json = NetworkUtil.get(ICongService.apiURL + apiParam, this.setTMapInfo());
        log.info("json : " + json);

        // JSON 구조를 Map 데이터 구조로 변경하기
        Map<String, Object> rMap = new ObjectMapper().readValue(json, LinkedHashMap.class);
        Map<String, Object> rContents = (Map<String, Object>) rMap.get("contents");

        String poiId = String.valueOf(rContents.get("poiId"));
        log.info("복잡도 POI : " + poiId);

        // 받아온 리턴 값의 내부 속성(층) "contents"

        List<Map<String, Object>> rltm = (List<Map<String, Object>>) rContents.get("rltm");


        double congestion = Double.valueOf(String.valueOf(rltm.get(0).get("congestion"))); // 혼잡도 정보
        int congestionLevel = Integer.valueOf(String.valueOf(rltm.get(0).get("congestionLevel"))); // 혼잡도 수준 정보
        int type = Integer.valueOf(String.valueOf(rltm.get(0).get("type"))); // 혼잡도 수준 정보

        log.info("congestion : " + congestion);
        log.info("congestionLevel : " + congestionLevel);
        log.info("type : " + type);

        CongDTO rDTO = new CongDTO();

        rDTO.setCongestion(congestion);
        rDTO.setCongestionLevel(congestionLevel);
        rDTO.setType(type);

        log.info(this.getClass().getName() + ".getCong End!");

        return rDTO;

    }
}
