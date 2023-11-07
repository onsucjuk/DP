package dp.fdis.controller;

import dp.fdis.dto.MsgDTO;
import dp.fdis.dto.NoticeDTO;
import dp.fdis.dto.TourDTO;
import dp.fdis.dto.UserInfoDTO;
import dp.fdis.service.ITourInfoService;
import dp.fdis.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequestMapping(value = "/tour")
@RequiredArgsConstructor
@Controller
public class TourController {

    private final ITourInfoService tourInfoService;

    /**
     * 여행 정보 페이지로 이동
     */
    
    @GetMapping(value = "tourInfo")
    public String tourInfo(ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".tourInfo Start!");

        TourDTO pDTO = new TourDTO();

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("SS_USER_ID : " + userId);


        pDTO.setUserId(userId);

        List<TourDTO> rList = Optional.ofNullable(tourInfoService.getTourList(pDTO))
                .orElseGet(ArrayList::new);


        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        return "thymeleaf/tour/tourInfo";
    }
    
    /**
     * 여행 등록 페이지로 이동
     */
    @GetMapping(value = "tourInfoReg")
    public String tourInfoReg() throws Exception {

        log.info(this.getClass().getName() + ".tourInfo Start!");

        return "thymeleaf/tour/tourInfoReg";

    }

    /**
     * 여행 등록 페이지로 이동
     */
    @GetMapping(value = "tourPlaceReg")
    public String tourPlaceReg() throws Exception {

        log.info(this.getClass().getName() + ".tourPlaceReg Start!");

        return "thymeleaf/tour/tourPlaceReg";

    }

    /**
     * 여행 수정 페이지로 이동
     */
    @GetMapping(value = "tourNameEdit")
    public String tourNameEdit(HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".tourNameEdit Start!");

        return "thymeleaf/tour/tourNameEdit";

    }

    /**
     * 목적지 등록 페이지로 이동
     */
    @GetMapping(value = "tourDayInfo")
    public String tourDayInfo(HttpSession session, HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".tourDayInfo Start!");

        String userId = (String) session.getAttribute("SS_USER_ID");
        String tourSeq = (String) session.getAttribute("SS_TOUR_SEQ");
        String daySeq = request.getParameter("nSeq");

        log.info("userId : " + userId);
        log.info("tourSeq : " + tourSeq);
        log.info("daySeq : " + daySeq);

        session.setAttribute("SS_USER_ID", userId);
        session.setAttribute("SS_TOUR_SEQ", tourSeq);
        session.setAttribute("SS_DAY_SEQ", daySeq);


        return "thymeleaf/tour/tourDayInfo";

    }

    /**
     * 목적지 등록 페이지로 이동
     */
    @GetMapping(value = "tourPlaceRegForm")
    public String tourPlaceRegForm() throws Exception {

        log.info(this.getClass().getName() + ".tourPlaceRegForm Start!");

        return "thymeleaf/tour/tourPlaceRegForm";

    }

    /**
     * 여행 정보 등록
     */
    @ResponseBody
    @PostMapping(value = "insertTourInfo")
    public MsgDTO insertTourInfo(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".insertTourInfo Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // 로그인 아이디
            String tourName = CmmUtil.nvl(request.getParameter("tourName")); // 제목

            log.info("session user_id : " + userId);
            log.info("tourName : " + tourName);


            TourDTO pDTO = new TourDTO();
            pDTO.setUserId(userId);
            pDTO.setTourName(tourName);

            if(userId.length() > 0) {
                tourInfoService.insertTourInfo(pDTO);

                TourDTO rDTO = Optional.ofNullable(tourInfoService.getTourSeq(pDTO)).orElseGet(TourDTO::new);

                tourInfoService.addTourDay(rDTO);

                msg = "등록되었습니다.";

            } else {

                msg = "로그인 해주세요.";

            }

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".insertTourInfo End!");
        }

        return dto;
    }

    /**
     * 회원 가입 전 아이디 중복체크하기(Ajax를 통해 입력한 아이디 정보 받음)
     */
    @ResponseBody
    @PostMapping(value = "getTourNameExists")
    public TourDTO getUserExists(HttpServletRequest request, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".getTourNameExists Start!");

        String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));
        String tourName = CmmUtil.nvl(request.getParameter("tourName")); // 여행명

        log.info("tourName : " + tourName);
        log.info("userId : " + userId);

        TourDTO pDTO = new TourDTO();
        pDTO.setTourName(tourName);
        pDTO.setUserId(userId);

        //회원아이디를 통해 중복된 아이디인지 조회
        TourDTO rDTO = Optional.ofNullable(tourInfoService.getTourNameExists(pDTO)).orElseGet(TourDTO::new);

        log.info(this.getClass().getName() + ".getTourNameExists End!");

        return rDTO;
    }
    
    /**
     *   여행 정보 삭제
     **/
    @ResponseBody
    @PostMapping(value = "deleteTourInfo")
    public MsgDTO deleteTourInfo(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".deleteTourInfo Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 여행 번호(PK)
            String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

            log.info("nSeq : " + nSeq);
            log.info("userId : " + userId);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(nSeq);
            pDTO.setUserId(userId);

            // 여행 정보 삭제하기 DB
            tourInfoService.deleteTourInfo(pDTO);
            tourInfoService.deleteTourDayAll(pDTO);
            tourInfoService.deleteTourPlace(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".deleteTourInfo End!");

        }

        return dto;
    }

    /**
     *   여행 시작일 수정
     **/
    @ResponseBody
    @PostMapping(value = "updateTourStart")
    public MsgDTO updateTourStart(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".updateTourStart Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String nSeq = CmmUtil.nvl((String)session.getAttribute("SS_TOUR_SEQ")); // 여행 번호(PK)
            String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

            log.info("nSeq : " + nSeq);
            log.info("userId : " + userId);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(nSeq);
            pDTO.setUserId(userId);

            tourInfoService.updateTourStart(pDTO);

            msg = "여행 시작일이 수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);
            
            log.info(this.getClass().getName() + ".updateTourStart End!");
            
        }

        return dto;
    }

    /**
     *   여행 종료일 수정
     **/
    @ResponseBody
    @PostMapping(value = "updateTourEnd")
    public MsgDTO updateTourEnd(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".updateTourEnd Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String nSeq = CmmUtil.nvl((String)session.getAttribute("SS_TOUR_SEQ")); // 여행 번호(PK)
            String tourProcess = CmmUtil.nvl(request.getParameter("tourProcess")); // 여행 진행도
            String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

            log.info("nSeq : " + nSeq);
            log.info("userId : " + userId);
            log.info("tourProcess : " + tourProcess);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(nSeq);
            pDTO.setUserId(userId);
            pDTO.setTourProcess(tourProcess);

            tourInfoService.updateTourEnd(pDTO);

            msg = "여행 시작일이 수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".updateTourEnd End!");

        }

        return dto;
    }

    /**
     *   여행 정보 수정
     *   수정 할 수 있는 정보가 이름 뿐임
     **/
    @ResponseBody
    @PostMapping(value = "updateTourName")
    public MsgDTO updateTourName(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".updateTourName Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String nSeq = CmmUtil.nvl((String)session.getAttribute("SS_TOUR_SEQ")); // 여행 번호(PK)
            String tourName = CmmUtil.nvl(request.getParameter("tourName")); // 여행 번호(PK)
            String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

            log.info("nSeq : " + nSeq);
            log.info("userId : " + userId);
            log.info("tourName : " + tourName);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(nSeq);
            pDTO.setUserId(userId);
            pDTO.setTourName(tourName);


            tourInfoService.updateTourName(pDTO);

            msg = "여행명 수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".updateTourName End!");

        }

        return dto;
    }
    
    /**
     *  여행 요일 관리 페이지 이동
     **/
    @GetMapping(value = "tourDayList")
    public String tourDayList(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".tourDayList Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 여행 번호(PK)
        String ssUserId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); //

        session.setAttribute("SS_TOUR_SEQ", nSeq);
        session.setAttribute("SS_USER_ID", ssUserId);

        String ssSeq = (String) session.getAttribute("SS_TOUR_SEQ");
        log.info("nSeq : " + nSeq);
        log.info("ssSeq : " + ssSeq);
        log.info("ssUserId : " + ssUserId);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        TourDTO pDTO = new TourDTO();
        pDTO.setTourSeq(nSeq);

        List<TourDTO> rList2 = Optional.ofNullable(tourInfoService.getTourDay(pDTO))
                .orElseGet(ArrayList::new);
        List<TourDTO> rList1 = Optional.ofNullable(tourInfoService.getTourYn(pDTO))
                .orElseGet(ArrayList::new);

        List<TourDTO> rList = Stream.concat(rList2.stream(), rList1.stream())
                .distinct()
                .collect(Collectors.toList());


        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".tourDayList End!");


        return "thymeleaf/tour/tourDayList";

    }


    /* ############################### 여기서부터 TourDay 컨트롤러 ######################################################*/

    /**
     * 여행 일자 등록
     */
    @ResponseBody
    @PostMapping(value = "addTourDay")
    public MsgDTO tourDayAdd(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".addTourDay Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String tourSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 여행 번호(PK)

            log.info("tourSeq : " + tourSeq);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(tourSeq);

            if(tourSeq.length() > 0) {
                tourInfoService.addTourDay(pDTO);

                msg = "등록되었습니다.";

            } else {

                msg = "여행을 먼저 등록하세요.";

            }

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".addTourDay End!");
        }

        return dto;
    }

    @ResponseBody
    @PostMapping(value = "addTourPlaceInfo")
    public String addTourPlaceInfo(ModelMap model, HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".addTourPlaceInfo Start!");

        String itemSeq = (String) session.getAttribute("SS_ITEM_SEQ");
        String placeName = request.getParameter("placeName"+itemSeq);
        String placeAddr = request.getParameter("placeAddr"+itemSeq);
        String lat = request.getParameter("lat"+itemSeq);
        String lon = request.getParameter("lon"+itemSeq);

        log.info("itemSeq : " + itemSeq);
        log.info("placeName : " + placeName);
        log.info("placeAddr : " + placeAddr);
        log.info("lat : " + lat);
        log.info("lon : " + lon);

        TourDTO pDTO = new TourDTO();

        pDTO.setPlaceName(placeName);
        pDTO.setPlaceAddr(placeAddr);
        pDTO.setLat(lat);
        pDTO.setLon(lon);

        model.addAttribute(pDTO);

        log.info(this.getClass().getName() + ".addTourPlaceInfo End!");

        return "thymeleaf/tour/tourPlaceRegForm";
    }
}