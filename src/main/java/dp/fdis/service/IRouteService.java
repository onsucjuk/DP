package dp.fdis.service;

import dp.fdis.dto.RouteDTO;

public interface IRouteService {

    String apiURL = "https://apis.openapi.sk.com/tmap/routes?version=1";

    RouteDTO getRouteInfo(RouteDTO pDTO) throws Exception;

}
