package dp.fdis.controller;

import dp.fdis.dto.CongDTO;
import dp.fdis.dto.RouteDTO;
import dp.fdis.service.IRouteService;
import dp.fdis.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping(value = "/route")
@RequiredArgsConstructor
@RestController
public class RouteController {

    private final IRouteService routeService;

    @PostMapping(value = "getRouteInfo")
    public RouteDTO getRouteInfo(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getRoute Start!");

        String startY = CmmUtil.nvl(request.getParameter("sLat"));
        String startX = CmmUtil.nvl(request.getParameter("sLon"));
        String endY = CmmUtil.nvl(request.getParameter("eLat"));
        String endX = CmmUtil.nvl(request.getParameter("eLon"));
        String index = CmmUtil.nvl(request.getParameter("index"));


        log.info("startY : " + startY);
        log.info("startX : " + startX);
        log.info("endY : " + endY);
        log.info("endX : " + endX);

        RouteDTO pDTO = new RouteDTO();

        pDTO.setStartX(startX);
        pDTO.setStartY(startY);
        pDTO.setEndX(endX);
        pDTO.setEndY(endY);



        /**
         *    rDTO에 저장된 값
         *    totalDistance
         *    totalTime
         */
        RouteDTO rDTO = routeService.getRouteInfo(pDTO);


        if (rDTO == null) {
            rDTO = new RouteDTO();
        }

        rDTO.setIndex(index);
        log.info(this.getClass().getName() + ".getRouteInfo End!");

        return rDTO;
    }

}
