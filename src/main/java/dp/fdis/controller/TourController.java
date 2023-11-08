package dp.fdis.controller;

import dp.fdis.dto.MsgDTO;
import dp.fdis.dto.TourDTO;
import dp.fdis.service.ITourInfoService;
import dp.fdis.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
     * 여행 수정 페이지로 이동
     */
    @GetMapping(value = "tourNameEdit")
    public String tourNameEdit(HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".tourNameEdit Start!");

        return "thymeleaf/tour/tourNameEdit";

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
            String tourName = CmmUtil.nvl(request.getParameter("tourName")); // 여행 이름(PK)
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
        pDTO.setUserId(ssUserId);

        String existsYn = tourInfoService.tourSeqExists(pDTO).getExistsYn();

        log.info("existsYn : " + existsYn);

        if (existsYn.equals("N")) {

                return "/tour/tourInfo";

        } else {

            List<TourDTO> rList = Optional.ofNullable(tourInfoService.getTourDayList(pDTO))
                    .orElseGet(ArrayList::new);

            // 조회된 리스트 결과값 넣어주기
            model.addAttribute("rList", rList);


            log.info(this.getClass().getName() + ".tourDayList End!");

            return "thymeleaf/tour/tourDayList";
        }
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

    /**
     * 여행 일자 정보 페이지로 이동
     */
    @GetMapping(value = "tourDayInfo")
    public String tourDayInfo(HttpSession session, HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".tourDayInfo Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
        String tourDay = CmmUtil.nvl(request.getParameter("nSeq"));

        log.info("userId : " + userId);
        log.info("tourSeq : " + tourSeq);
        log.info("daySeq : " + tourDay);

        session.setAttribute("SS_USER_ID", userId);
        session.setAttribute("SS_TOUR_SEQ", tourSeq);
        session.setAttribute("SS_DAY_SEQ", tourDay);

        TourDTO pDTO = new TourDTO();

        pDTO.setTourSeq(tourSeq);
        pDTO.setTourDay(tourDay);

        List<TourDTO> rList = Optional.ofNullable(tourInfoService.getTourPlace(pDTO))
                .orElseGet(ArrayList::new);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        return "thymeleaf/tour/tourDayInfo";

    }


    /**
     * 목적지 찾기 페이지로 이동
     */
    @GetMapping(value = "tourPlaceFind")
    public String tourPlaceFind() throws Exception {

        log.info(this.getClass().getName() + ".tourPlaceFind Start!");

        return "thymeleaf/tour/tourPlaceFind";

    }

    /**
     * 목적지 찾기 페이지(수정)로 이동
     */
    @GetMapping(value = "tourPlaceFind_E")
    public String tourPlaceFind_E() throws Exception {

        log.info(this.getClass().getName() + ".tourPlaceFind_E Start!");

        return "thymeleaf/tour/tourPlaceFind_E";

    }

    /**
     * 목적지 등록 페이지로 이동
     */
    @GetMapping(value = "tourPlaceRegForm")
    public String tourPlaceRegForm(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".tourPlaceRegForm Start!");

        String placeName = CmmUtil.nvl(request.getParameter("placeName"));
        String placeAddr = CmmUtil.nvl(request.getParameter("placeAddr"));
        String lat = CmmUtil.nvl(request.getParameter("lat"));
        String lon = CmmUtil.nvl(request.getParameter("lon"));

        log.info("placeName : " + placeName);
        log.info("placeAddr : " + placeAddr);
        log.info("lat : " + lat);
        log.info("lon : " + lon);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        TourDTO pDTO = new TourDTO();



        pDTO.setPlaceName(placeName);
        pDTO.setPlaceAddr(placeAddr);
        pDTO.setLat(lat);
        pDTO.setLon(lon);


        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("pDTO", pDTO);

        log.info(this.getClass().getName() + ".tourPlaceRegForm End!");

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
     * 목적지 등록
     */
    @ResponseBody
    @PostMapping(value = "insertTourPlace")
    public MsgDTO insertTourPlace(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".insertTourPlace Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
            String daySeq = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));
            String placeNick = CmmUtil.nvl(request.getParameter("placeNick"));
            String placeName = CmmUtil.nvl(request.getParameter("placeName"));
            String placeAddr = CmmUtil.nvl(request.getParameter("placeAddr"));
            String memo = CmmUtil.nvl(request.getParameter("memo"));
            String lat = CmmUtil.nvl(request.getParameter("lat"));
            String lon = CmmUtil.nvl(request.getParameter("lon"));

            log.info("session tourSeq : " + tourSeq);
            log.info("session daySeq : " + daySeq);
            log.info("placeNick : " + placeNick);
            log.info("placeName : " + placeName);
            log.info("placeAddr : " + placeAddr);
            log.info("memo : " + memo);
            log.info("lat : " + lat);
            log.info("lon : " + lon);

            TourDTO pDTO = new TourDTO();

            pDTO.setTourSeq(tourSeq);
            pDTO.setTourDay(daySeq);
            pDTO.setPlaceNick(placeNick);
            pDTO.setPlaceName(placeName);
            pDTO.setPlaceAddr(placeAddr);
            pDTO.setMemo(memo);
            pDTO.setLat(lat);
            pDTO.setLon(lon);

            if(userId.length() > 0) {
                tourInfoService.insertTourPlace(pDTO);

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

            log.info(this.getClass().getName() + ".insertTourPlace End!");
        }

        return dto;
    }

    /**
     * 목적지 수정 페이지로 이동
     */
    @GetMapping(value = "tourPlaceEditForm")
    public String tourPlaceEditForm(HttpSession session, HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".tourPlaceEditForm Start!");

        String placeNick = CmmUtil.nvl(request.getParameter("placeNick"));
        String placeName = CmmUtil.nvl(request.getParameter("placeName"));
        String placeAddr = CmmUtil.nvl(request.getParameter("placeAddr"));
        String lat = CmmUtil.nvl(request.getParameter("lat"));
        String lon = CmmUtil.nvl(request.getParameter("lon"));
        String memo = CmmUtil.nvl(request.getParameter("memo"));
        String placeSeq = CmmUtil.nvl(request.getParameter("placeSeq"));

        log.info("placeNick : " + placeNick);
        log.info("placeName : " + placeName);
        log.info("placeAddr : " + placeAddr);
        log.info("lat : " + lat);
        log.info("lon : " + lon);
        log.info("memo : " + memo);
        log.info("placeSeq : " + placeSeq);

        // 장소 찾기 들어가서 정보 가져왔을 때 placeSeq, nick, memo는 넘겨받지 못하므로 임시로 세션에 저장함

        session.setAttribute("SS_PLACE_NICK", placeNick);
        session.setAttribute("SS_MEMO", memo);
        session.setAttribute("SS_PLACE_SEQ", placeSeq);

        log.info("SS_PLACE_NICK : " + session.getAttribute("SS_PLACE_NICK"));
        log.info("SS_MEMO : " + session.getAttribute("SS_MEMO"));
        log.info("SS_PLACE_SEQ : " + session.getAttribute("SS_PLACE_SEQ"));

        TourDTO pDTO = new TourDTO();

        pDTO.setPlaceNick(placeNick);
        pDTO.setPlaceName(placeName);
        pDTO.setPlaceAddr(placeAddr);
        pDTO.setLat(lat);
        pDTO.setLon(lon);
        pDTO.setMemo(memo);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("pDTO", pDTO);

        log.info(this.getClass().getName() + ".tourPlaceEditForm End!");

        return "thymeleaf/tour/tourPlaceEditForm";
    }

    /**
     *   목적지 수정
     **/
    @ResponseBody
    @PostMapping(value = "updateTourPlace")
    public MsgDTO updateTourPlace(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".updateTourPlace Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String tourSeq = CmmUtil.nvl((String)session.getAttribute("SS_TOUR_SEQ"));
            String tourDay = CmmUtil.nvl((String)session.getAttribute("SS_DAY_SEQ"));
            String placeSeq = CmmUtil.nvl((String)session.getAttribute("SS_PLACE_SEQ"));
            String placeNick = CmmUtil.nvl(request.getParameter("placeNick"));
            String placeName = CmmUtil.nvl(request.getParameter("placeName"));
            String placeAddr = CmmUtil.nvl(request.getParameter("placeAddr"));
            String lat = CmmUtil.nvl(request.getParameter("lat"));
            String lon = CmmUtil.nvl(request.getParameter("lon"));
            String memo = CmmUtil.nvl(request.getParameter("memo"));

            log.info("tourSeq : " + tourSeq);
            log.info("tourDay : " + tourDay);
            log.info("placeSeq : " + placeSeq);
            log.info("placeNick : " + placeNick);
            log.info("placeName : " + placeName);
            log.info("placeAddr : " + placeAddr);
            log.info("lat : " + lat);
            log.info("lon : " + lon);
            log.info("memo : " + memo);

            TourDTO pDTO = new TourDTO();

            pDTO.setTourSeq(tourSeq);
            pDTO.setTourDay(tourDay);
            pDTO.setPlaceSeq(placeSeq);
            pDTO.setPlaceNick(placeNick);
            pDTO.setPlaceName(placeName);
            pDTO.setPlaceAddr(placeAddr);
            pDTO.setLat(lat);
            pDTO.setLon(lon);
            pDTO.setMemo(memo);

            if (tourSeq.length() > 0 && tourDay.length() > 0 && placeSeq.length() > 0) {

                tourInfoService.updateTourPlace(pDTO);

                msg = "목적지 정보가 수정되었습니다.";

            } else {

                msg = "여행 번호 혹은 일자 혹은 장소 번호가 맞지 않습니다.";
            }

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".updateTourPlace End!");

        }

        return dto;
    }

    /**
     *   목적지 정보 삭제
     **/
    @ResponseBody
    @PostMapping(value = "deleteTourPlaceOne")
    public MsgDTO deleteTourPlaceOne(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".deleteTourPlaceOne Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String placeSeq = CmmUtil.nvl(request.getParameter("placeSeq"));
            String tourDay = CmmUtil.nvl((String)session.getAttribute("SS_DAY_SEQ"));
            String tourSeq = CmmUtil.nvl((String)session.getAttribute("SS_TOUR_SEQ"));

            log.info("placeSeq : " + placeSeq);
            log.info("tourDay : " + tourDay);
            log.info("tourSeq : " + tourSeq);


            if (placeSeq.length() > 0 && tourDay.length() > 0 && tourSeq.length() > 0) {

                TourDTO pDTO = new TourDTO();

                pDTO.setTourSeq(tourSeq);
                pDTO.setTourDay(tourDay);
                pDTO.setPlaceSeq(placeSeq);


                // 목적지 한 개 삭제하기 DB
                tourInfoService.deleteTourPlaceOne(pDTO);

                msg = "삭제되었습니다.";

            } else {

                msg = "여행 번호 혹은 목적지 번호 혹은 일자 번호가 올바르지 않습니다. 화면을 새로고침 하겠습니다.";

            }

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".deleteTourPlaceOne End!");

        }

        return dto;
    }


}