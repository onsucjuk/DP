package dp.fdis.controller;

import dp.fdis.dto.CongDTO;
import dp.fdis.service.ICongService;
import dp.fdis.util.CmmUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping(value = "/cong")
@RequiredArgsConstructor
@RestController
public class CongController {

        private final ICongService congService;

        @GetMapping(value = "getCongestion")
        public CongDTO getCongestion(HttpServletRequest request) throws Exception {

            log.info(this.getClass().getName() + ".getCongestion Start!");

            String poi = CmmUtil.nvl(request.getParameter("poi"));
            String lat = CmmUtil.nvl(request.getParameter("lat"));
            String lon = CmmUtil.nvl(request.getParameter("lon"));

            CongDTO pDTO = new CongDTO();

            pDTO.setPoi(poi);
            pDTO.setLat(lat);
            pDTO.setLng(lon);

            CongDTO rDTO = congService.getCong(pDTO);

            if (rDTO == null) {
                rDTO = new CongDTO();
            }

            log.info(this.getClass().getName() + ".getCongestion End!");

            return rDTO;
        }

}
