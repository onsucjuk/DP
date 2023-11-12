package dp.fdis.service;

import dp.fdis.dto.CongDTO;

public interface ICongService {

    String apiURL = "https://apis.openapi.sk.com/puzzle/place/congestion/rltm/pois/";

    CongDTO getCong(CongDTO pDTO) throws Exception;

}
