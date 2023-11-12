package dp.fdis.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dp.fdis.dto.RouteDTO;
import dp.fdis.service.IRouteService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RouteService implements IRouteService {

    @Value("${tMap.api.key}")
    private String apiKey;

/*
    private Map<String, String> setRouteInfo() {
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("appkey", apiKey);

        log.info("appKey : " + apiKey);

        return requestHeader;
    }
*/

    @Override
    public RouteDTO getRouteInfo(RouteDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getRouteInfo Start!");

            String startX = pDTO.getStartX();
            String startY = pDTO.getStartY();
            String endX = pDTO.getEndX();
            String endY = pDTO.getEndY();

            log.info("startX : " + startX );
            log.info("startY : " + startY );
            log.info("endX : " + endX );
            log.info("endY : " + endY );



        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"endX\":" + endX + ",\"endY\": " + endY + ",\"startX\": " + startX + ",\"startY\":" + startY + "}");
        Request request = new Request.Builder()
                .url(IRouteService.apiURL)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("appKey", apiKey)
                .build();

        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();

        Map<String, Object> rMap = new ObjectMapper().readValue(responseBody, LinkedHashMap.class);
        List<Map<String, Object>> features = (List<Map<String, Object>>) rMap.get("features");
        Map<String, Object> features1 = features.get(0);
        Map<String, Object> properties = (Map<String, Object>) features1.get("properties");

        // totalDistance와 totalTime 가져오기
        int totalDistance = (int) properties.get("totalDistance");
        int totalTime = (int) properties.get("totalTime");

        log.info("Total Distance: " + totalDistance);
        log.info("Total Time: " + totalTime);

        RouteDTO rDTO = new RouteDTO();

        // RouteDTO에 totalDistance와 totalTime 설정
        rDTO.setTotalDistance(totalDistance);
        rDTO.setTotalTime(totalTime);



        log.info(this.getClass().getName() + ".getRouteInfo End!");

        // 최종적으로 설정된 RouteDTO 반환
        return rDTO;

    }


}
