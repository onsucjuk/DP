package dp.fdis.controller;

import dp.fdis.dto.ImgDTO;
import dp.fdis.dto.MsgDTO;
import dp.fdis.dto.TourDTO;
import dp.fdis.service.IImgService;
import dp.fdis.service.ITourInfoService;
import dp.fdis.util.CmmUtil;
import dp.fdis.util.DateUtil;
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


@Slf4j
@RequestMapping(value = "/tour")
@RequiredArgsConstructor
@Controller
public class TourController {

    private final ITourInfoService tourInfoService;
    private final IImgService iImgService;
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

        if (rList.isEmpty()) {
            session.setAttribute("SS_EXISTS_YN", "N");
        } else {
            session.setAttribute("SS_EXISTS_YN", "Y");
        }

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
    public String tourNameEdit(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".tourNameEdit Start!");

        String userId =((String) session.getAttribute("SS_USER_ID"));
        String tourSeq =((String) session.getAttribute("SS_TOUR_SEQ"));

        log.info("userId : " + userId);
        log.info("tourSeq : " + tourSeq);

        TourDTO pDTO = new TourDTO();

        pDTO.setUserId(userId);
        pDTO.setTourSeq(tourSeq);

        TourDTO rDTO = Optional.ofNullable(tourInfoService.getTourInfo(pDTO))
                .orElseGet(TourDTO::new);

        log.info("tourName : " + rDTO.getTourName());
        log.info("startTime : " + rDTO.getStartTime());

        model.addAttribute("rDTO", rDTO);

        return "thymeleaf/tour/tourNameEdit";

    }

    /**
     * 회원 가입 전 아이디 중복체크하기(Ajax를 통해 입력한 아이디 정보 받음)
     */
    @ResponseBody
    @PostMapping(value = "getTourNameExists")
    public TourDTO getUserExists(HttpServletRequest request, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".getTourNameExists Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
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
     * 여행 정보 삭제
     **/
    @ResponseBody
    @PostMapping(value = "deleteTourInfo")
    public MsgDTO deleteTourInfo(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".deleteTourInfo Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 여행 번호(PK)
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            log.info("nSeq : " + nSeq);
            log.info("userId : " + userId);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(nSeq);
            pDTO.setUserId(userId);

            ImgDTO iDTO = new ImgDTO();

            iDTO.setTourSeq(nSeq);

            List<ImgDTO> rList = Optional.ofNullable(iImgService.getTourImgAll(iDTO))
                    .orElseGet(ArrayList::new);

            System.out.println(rList);

            for (ImgDTO imgDTO : rList) {

                log.info("이미지 삭제 시작!");

                String imgURL = imgDTO.getImgURL();

                log.info("imgURL : " + imgURL);

                String uploadFileName = imgURL.substring(imgURL.lastIndexOf("/") + 1);

                iDTO.setUploadFileName(uploadFileName);

                log.info("uploadFileName : " + uploadFileName);

                iImgService.deleteCloudImg(iDTO);

                log.info(uploadFileName+" : 삭제 완료");

            }

            iImgService.deleteTourImg(iDTO);


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
/*

    @ResponseBody
    @PostMapping(value = "updateTourEndAdd")
    public MsgDTO updateTourEndAdd(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".updateTourEndAdd Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String nSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ")); // 여행 번호(PK)
            String tourProcess = CmmUtil.nvl(request.getParameter("tourProcess")); // 여행 진행도
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            log.info("nSeq : " + nSeq);
            log.info("userId : " + userId);
            log.info("tourProcess : " + tourProcess);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(nSeq);
            pDTO.setUserId(userId);
            pDTO.setTourProcess(tourProcess);

            tourInfoService.updateTourEndAdd(pDTO);

            msg = "여행 시작일이 수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".updateTourEndAdd End!");

        }

        return dto;
    }
*/

    /**
     * 여행 정보 수정
     * 수정 할 수 있는 정보가 이름 뿐임
     **/
    @ResponseBody
    @PostMapping(value = "updateTourInfo")
    public MsgDTO updateTourInfo(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".updateTourInfo Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ")); // 여행 번호(PK)
            String tourName = CmmUtil.nvl(request.getParameter("tourName")); // 여행 이름(PK)
            String startTime = CmmUtil.nvl(request.getParameter("startTime")); // 여행 이름(PK)
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            log.info("tourSeq : " + tourSeq);
            log.info("userId : " + userId);
            log.info("tourName : " + tourName);
            log.info("startTime : " + startTime);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(tourSeq);
            pDTO.setUserId(userId);
            pDTO.setTourName(tourName);
            pDTO.setStartTime(startTime);

            int n = tourInfoService.countDay(pDTO);

            pDTO.setCount(n-1);

            tourInfoService.updateTourName(pDTO);

            log.info("Day num : " + n);

            for (int i = 0; i < n; i++){

                pDTO.setCount(i);
                pDTO.setTourDay(String.valueOf(i+1));
                tourInfoService.updateTourDays(pDTO);

            }


            msg = "여행 정보 수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".updateTourInfo End!");

        }

        return dto;
    }

    /**
     * 여행 요일 관리 페이지 이동
     **/
    @GetMapping(value = "tourDayList")
    public String tourDayList(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".tourDayList Start!");

        String tourSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 여행 번호(PK)
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); //

        session.setAttribute("SS_TOUR_SEQ", tourSeq);
        session.setAttribute("SS_USER_ID", userId);

        String sstourSeq = (String) session.getAttribute("SS_TOUR_SEQ");

        log.info("tourSeq : " + tourSeq);
        log.info("sstourSeq : " + sstourSeq);
        log.info("userId : " + userId);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        TourDTO pDTO = new TourDTO();

        pDTO.setTourSeq(tourSeq);
        pDTO.setUserId(userId);

        String existsYn = tourInfoService.tourSeqExists(pDTO).getExistsYn();

        log.info("existsYn : " + existsYn);

        if (existsYn.equals("N")) {

            return "/tour/tourInfo";

        } else {

            List<TourDTO> rList = Optional.ofNullable(tourInfoService.getTourDayList(pDTO))
                    .orElseGet(ArrayList::new);

            TourDTO rDTO = Optional.ofNullable(tourInfoService.getTourInfo(pDTO))
                    .orElseGet(TourDTO::new);


            // 조회된 리스트 결과값 넣어주기
            model.addAttribute("rList", rList);


            session.setAttribute("SS_TOUR_NAME", rDTO.getTourName());

            String tourName = (String) session.getAttribute("SS_TOUR_NAME");

            log.info("sstourName : " + tourName);

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
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            log.info("tourSeq : " + tourSeq);
            log.info("userId : " + userId);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(tourSeq);
            pDTO.setUserId(userId);

            if (tourSeq.length() > 0) {

                tourInfoService.addTourDay(pDTO);
                tourInfoService.updateTourEndAdd(pDTO);

                msg = "";

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

        pDTO.setUserId(userId);
        pDTO.setTourSeq(tourSeq);
        pDTO.setTourDay(tourDay);

        List<TourDTO> rList = Optional.ofNullable(tourInfoService.getTourPlace(pDTO))
                .orElseGet(ArrayList::new);

        if (rList.isEmpty()) {
            session.setAttribute("SS_EXISTS_YN", "N");
        } else {
            session.setAttribute("SS_EXISTS_YN", "Y");
        }


        TourDTO rDTO = new TourDTO();

        List<TourDTO> dList = Optional.ofNullable(tourInfoService.getTourDayList(pDTO))
                .orElseGet(ArrayList::new);

        int dayCount = dList.size();

        log.info("Day 수 : " + dayCount);

        rDTO.setCount(dayCount);
        model.addAttribute("rDTO", rDTO);
        model.addAttribute("rList", rList);

        return "thymeleaf/tour/tourDayInfo";

    }

    /**
     * 수정전 여행지 이름 중복체크하기
     */
    @ResponseBody
    @PostMapping(value = "getTourEditExists")
    public TourDTO getTourEditExists(HttpServletRequest request, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".getTourEditExists Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String tourName = CmmUtil.nvl(request.getParameter("tourName")); // 여행명
        String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));

        log.info("tourName : " + tourName);
        log.info("userId : " + userId);
        log.info("tourSeq : " + tourSeq);

        TourDTO pDTO = new TourDTO();
        pDTO.setTourName(tourName);
        pDTO.setUserId(userId);
        pDTO.setTourSeq(tourSeq);

        //회원아이디를 통해 중복된 아이디인지 조회
        TourDTO rDTO = Optional.ofNullable(tourInfoService.getTourEditExists(pDTO)).orElseGet(TourDTO::new);

        log.info(this.getClass().getName() + ".getTourEditExists End!");

        return rDTO;
    }

    /**
     * 여행 정보 삭제
     **/
    @ResponseBody
    @PostMapping(value = "deleteTourDay")
    public MsgDTO deleteTourDay(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".deleteTourDay Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String tourDay = CmmUtil.nvl(request.getParameter("dSeq")); // 여행 일자
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            log.info("tourDay : " + tourDay);
            log.info("tourSeq : " + tourSeq);
            log.info("userId : " + userId);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourDay(tourDay);
            pDTO.setTourSeq(tourSeq);
            pDTO.setUserId(userId);

            ImgDTO iDTO = new ImgDTO();

            iDTO.setTourSeq(tourSeq);
            iDTO.setTourDay(tourDay);

            List<ImgDTO> rList = Optional.ofNullable(iImgService.getTourDayImg(iDTO))
                    .orElseGet(ArrayList::new);

            System.out.println(rList);

            for (ImgDTO imgDTO : rList) {

                log.info("이미지 삭제 시작!");

                String imgURL = imgDTO.getImgURL();

                log.info("imgURL : " + imgURL);

                String uploadFileName = imgURL.substring(imgURL.lastIndexOf("/") + 1);

                iDTO.setUploadFileName(uploadFileName);

                log.info("uploadFileName : " + uploadFileName);

                iImgService.deleteCloudImg(iDTO);

                log.info(uploadFileName+" : 삭제 완료");

            }

            iImgService.deleteDayImg(iDTO);

            // 여행 일자, 해당 장소 삭제하기 DB
            tourInfoService.deleteTourDay(pDTO);
            tourInfoService.deleteDayPlace(pDTO);
            tourInfoService.updateTourEndSub(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".deleteTourDay End!");

        }

        return dto;
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

        String poi = CmmUtil.nvl(request.getParameter("poi"));
        String placeName = CmmUtil.nvl(request.getParameter("placeName"));
        String placeAddr = CmmUtil.nvl(request.getParameter("placeAddr"));
        String lat = CmmUtil.nvl(request.getParameter("lat"));
        String lon = CmmUtil.nvl(request.getParameter("lon"));

        log.info("poi : " + poi);
        log.info("placeName : " + placeName);
        log.info("placeAddr : " + placeAddr);
        log.info("lat : " + lat);
        log.info("lon : " + lon);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        TourDTO pDTO = new TourDTO();


        pDTO.setPoi(poi);
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

        log.info(this.getClass().getName() + ".insertTourInfoReg Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // 로그인 아이디
            String tourName = CmmUtil.nvl(request.getParameter("tourName")); // 제목
            String startTime = CmmUtil.nvl(request.getParameter("startTime")); // 여행시작일

            log.info("session user_id : " + userId);
            log.info("tourName : " + tourName);
            log.info("startTime : " + startTime);


            TourDTO pDTO = new TourDTO();
            pDTO.setUserId(userId);
            pDTO.setTourName(tourName);

            pDTO.setStartTime(startTime);

            if (userId.length() > 0) {
                tourInfoService.insertTourInfo(pDTO);

                TourDTO rDTO = Optional.ofNullable(tourInfoService.getTourSeq(pDTO)).orElseGet(TourDTO::new);

                String tourSeq = rDTO.getTourSeq();
                String rtourName = rDTO.getTourName();
                String tourProcess = rDTO.getTourProcess();
                String rstartTime = rDTO.getStartTime();

                log.info("여행 번호 : " + tourSeq);
                log.info("여행 이름 : " + rtourName);
                log.info("여행 진행도 여부 : " + tourProcess);
                log.info("여행 시작일 : " + rstartTime);

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

            log.info(this.getClass().getName() + ".insertTourInfoReg End!");
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
            String poi = CmmUtil.nvl(request.getParameter("poi"));

            log.info("session tourSeq : " + tourSeq);
            log.info("session daySeq : " + daySeq);
            log.info("placeNick : " + placeNick);
            log.info("placeName : " + placeName);
            log.info("placeAddr : " + placeAddr);
            log.info("memo : " + memo);
            log.info("lat : " + lat);
            log.info("lon : " + lon);
            log.info("poi : " + poi);

            TourDTO pDTO = new TourDTO();

            pDTO.setTourSeq(tourSeq);
            pDTO.setTourDay(daySeq);
            pDTO.setPlaceNick(placeNick);
            pDTO.setPlaceName(placeName);
            pDTO.setPlaceAddr(placeAddr);
            pDTO.setMemo(memo);
            pDTO.setLat(lat);
            pDTO.setLon(lon);
            pDTO.setPoi(poi);

            if (userId.length() > 0) {
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
        String poi = CmmUtil.nvl(request.getParameter("poi"));

        log.info("placeNick : " + placeNick);
        log.info("placeName : " + placeName);
        log.info("placeAddr : " + placeAddr);
        log.info("lat : " + lat);
        log.info("lon : " + lon);
        log.info("memo : " + memo);
        log.info("placeSeq : " + placeSeq);
        log.info("poi : " + poi);

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
        pDTO.setPoi(poi);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("pDTO", pDTO);

        log.info(this.getClass().getName() + ".tourPlaceEditForm End!");

        return "thymeleaf/tour/tourPlaceEditForm";
    }

    /**
     * 목적지 수정
     **/
    @ResponseBody
    @PostMapping(value = "updateTourPlace")
    public MsgDTO updateTourPlace(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".updateTourPlace Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
            String tourDay = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));
            String placeSeq = CmmUtil.nvl((String) session.getAttribute("SS_PLACE_SEQ"));
            String placeNick = CmmUtil.nvl(request.getParameter("placeNick"));
            String placeName = CmmUtil.nvl(request.getParameter("placeName"));
            String placeAddr = CmmUtil.nvl(request.getParameter("placeAddr"));
            String lat = CmmUtil.nvl(request.getParameter("lat"));
            String lon = CmmUtil.nvl(request.getParameter("lon"));
            String memo = CmmUtil.nvl(request.getParameter("memo"));
            String poi = CmmUtil.nvl(request.getParameter("poi"));

            log.info("tourSeq : " + tourSeq);
            log.info("tourDay : " + tourDay);
            log.info("placeSeq : " + placeSeq);
            log.info("placeNick : " + placeNick);
            log.info("placeName : " + placeName);
            log.info("placeAddr : " + placeAddr);
            log.info("lat : " + lat);
            log.info("lon : " + lon);
            log.info("memo : " + memo);
            log.info("poi : " + poi);

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
            pDTO.setPoi(poi);

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
     * 목적지 정보 삭제
     **/
    @ResponseBody
    @PostMapping(value = "deleteTourPlaceOne")
    public MsgDTO deleteTourPlaceOne(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".deleteTourPlaceOne Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String placeSeq = CmmUtil.nvl(request.getParameter("pSeq"));
            String tourDay = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));

            log.info("placeSeq : " + placeSeq);
            log.info("tourDay : " + tourDay);
            log.info("tourSeq : " + tourSeq);


            if (placeSeq.length() > 0 && tourDay.length() > 0 && tourSeq.length() > 0) {

                TourDTO pDTO = new TourDTO();

                pDTO.setTourSeq(tourSeq);
                pDTO.setTourDay(tourDay);
                pDTO.setPlaceSeq(placeSeq);

                TourDTO rDTO = Optional.ofNullable(tourInfoService.getTourPlaceOne(pDTO))
                        .orElseGet(TourDTO::new);
                String imgURL = rDTO.getImgURL();

                ImgDTO iDTO = new ImgDTO();

                iDTO.setTourSeq(tourSeq);
                iDTO.setTourDay(tourDay);
                iDTO.setPlaceSeq(placeSeq);

                // 목적지 한 개 삭제하기 DB
                tourInfoService.deleteTourPlaceOne(pDTO);

                if (imgURL.length() > 0) {

                    log.info("이미지 삭제 시작!");


                    log.info("imgURL : " + imgURL);

                    String uploadFileName = imgURL.substring(imgURL.lastIndexOf("/") + 1);

                    iDTO.setUploadFileName(uploadFileName);

                    log.info("uploadFileName : " + uploadFileName);

                    iImgService.deleteCloudImg(iDTO);

                    log.info(uploadFileName + " : 삭제 완료");

                    iImgService.deleteImgOne(iDTO);

                    msg = "삭제되었습니다.";

                } else {

                    msg = "등록된 이미지가 없습니다.";
                }

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

    /**
     * ##################################################################################
     *
     *                              Itda 서비스 컨트롤러
     *
     *  ##################################################################################
     */

    /**
     * Itda 페이지 이동
     */

    @GetMapping(value = "goItda")
    public String goItda(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".goItda Start!");

        String tourDay = CmmUtil.nvl(request.getParameter("dSeq"));
        String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String startTimeMn = CmmUtil.nvl((String) session.getAttribute("SS_STARTTIME_MN"));

        log.info("tourDay : " + tourDay);
        log.info("tourSeq : " + tourSeq);
        log.info("userId : " + userId);
        log.info("startTimeMn : " + startTimeMn);

        TourDTO pDTO = new TourDTO();

        pDTO.setTourDay(tourDay);
        pDTO.setTourSeq(tourSeq);
        pDTO.setUserId(userId);

        List<TourDTO> rList = Optional.ofNullable(tourInfoService.getTourPlace(pDTO))
                .orElseGet(ArrayList::new);

        if (rList.isEmpty()) {
            session.setAttribute("SS_EXISTS_YN", "N");
        } else {
            session.setAttribute("SS_EXISTS_YN", "Y");
        }

        List<TourDTO> dList = Optional.ofNullable(tourInfoService.getTourDayList(pDTO))
                .orElseGet(ArrayList::new);

        session.setAttribute("SS_DAY_SEQ", tourDay);

        log.info("rList 존재 여부 : " + (String) session.getAttribute("SS_EXISTS_YN"));

        if (dList.isEmpty()) {
            log.info("dList Empty");
        }

        TourDTO rDTO = Optional.ofNullable(tourInfoService.getTourInfo(pDTO))
                .orElseGet(TourDTO::new);

        TourDTO dDTO = Optional.ofNullable(tourInfoService.getTourDayInfo(pDTO))
                .orElseGet(TourDTO::new);

        log.info("Day 시작일자 : " + dDTO.getStartTime());

        // Day에 해당하는 TOUR_PLACE의 TOUR_YN값 축출 : cDTO(count)
        TourDTO cDTO = Optional.ofNullable(tourInfoService.getTourDayYn(pDTO))
                .orElseGet(TourDTO::new);

        int cTourY = cDTO.getTourY();
        int cTourN = cDTO.getTourN();

        log.info("cDTO : TourY " + cTourY );
        log.info("cDTO : TourN " + cTourN );

        log.info("rDTO.getStartTime() : " + CmmUtil.nvl(dDTO.getStartTime()));
        log.info("DateUtil.getDateTime() : " + DateUtil.getDateTime());

        log.info("테스트1 : " + CmmUtil.nvl(dDTO.getStartTime()).equals(DateUtil.getDateTime()));
        log.info("테스트2 : " + CmmUtil.nvl(startTimeMn).equals("Y"));


        if (CmmUtil.nvl(dDTO.getStartTime()).equals(DateUtil.getDateTime()) || CmmUtil.nvl(startTimeMn).equals("Y")) {

            session.setAttribute("SS_STARTTIME_YN", "Y");

            // 여행 시작 여부에 따라 TourInfo페이지에서 진행도 표시 여부 확인용 체크사항 변경
            // 0이면 여행시작 -> 진행도 표시
            pDTO.setTourProcess("0");
            tourInfoService.updateTourProcess(pDTO);

        } else {

            session.setAttribute("SS_STARTTIME_YN", "N");

            // 여행 시작 여부에 따라 TourInfo페이지에서 진행도 표시 여부 확인용 체크사항 변경
            // -1이면 여행 준비중 -> '여행 준비 중입니다 표시'
            pDTO.setTourProcess("-1");
            tourInfoService.updateTourProcess(pDTO);

        }

        log.info("SS_STARTTIME_YN : " + CmmUtil.nvl((String)session.getAttribute("SS_STARTTIME_YN")));

        model.addAttribute("rList", rList);
        model.addAttribute("dList", dList);
        model.addAttribute("rDTO", rDTO);
        model.addAttribute("dDTO", dDTO);
        model.addAttribute("cDTO", cDTO);

        return "thymeleaf/tour/goItda";

    }

    /**
     * 여행 시작
     **/
    @ResponseBody
    @PostMapping(value = "updateTourStart")
    public MsgDTO updateTourStart(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".updateTourStart Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String tourDay = CmmUtil.nvl(request.getParameter("dSeq"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ")); // 여행 번호(PK)
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            log.info("tourSeq : " + tourSeq);
            log.info("userId : " + userId);
            log.info("tourDay : " + tourDay);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(tourSeq);
            pDTO.setUserId(userId);
            pDTO.setTourDay(tourDay);

                msg = "여행을 시작합니다.";


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
     * 여행 시작 취소
     **/
    @ResponseBody
    @PostMapping(value = "resetTourStart")
    public MsgDTO resetTourStart(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".resetTourStart Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String tourDay = CmmUtil.nvl(request.getParameter("dSeq"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ")); // 여행 번호(PK)
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            log.info("tourSeq : " + tourSeq);
            log.info("userId : " + userId);
            log.info("tourDay : " + tourDay);

            TourDTO pDTO = new TourDTO();
            pDTO.setTourSeq(tourSeq);
            pDTO.setUserId(userId);
            pDTO.setTourDay(tourDay);

            if (tourDay.equals("1")) {

                tourInfoService.resetTourStart(pDTO);
                tourInfoService.resetTourDaySt(pDTO);

                msg = "여행이 취소되었습니다.";

            } else {

                tourInfoService.resetTourDaySt(pDTO);
                msg = "여행이 취소되었습니다.";

            }

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".resetTourStart End!");

        }

        return dto;
    }


    /**
     * 여행 시작
     **/
    @ResponseBody
    @PostMapping(value = "updatePlaceEnd")
    public MsgDTO updatePlaceEnd(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".updatePlaceEnd Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String placeName =CmmUtil.nvl(request.getParameter("placeName"));
            String placeNIck = CmmUtil.nvl(request.getParameter("placeNick"));
            String placeSeq = CmmUtil.nvl(request.getParameter("placeSeq"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
            String tourDay = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));

            log.info("placeSeq : " + placeSeq);
            log.info("tourSeq : " + tourSeq);
            log.info("tourDay : " + tourDay);

            TourDTO pDTO = new TourDTO();
            pDTO.setPlaceSeq(placeSeq);
            pDTO.setTourSeq(tourSeq);
            pDTO.setTourDay(tourDay);

            tourInfoService.updatePlaceEnd(pDTO);

            msg = placeName + "(" + placeNIck + ")" + " 방문을 완료하셨습니다!";


        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".updatePlaceEnd End!");

        }

        return dto;
    }

    /**
     * 여행 시작 취소
     **/
    @ResponseBody
    @PostMapping(value = "resetPlaceEnd")
    public MsgDTO resetPlaceEnd(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".resetPlaceEnd Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String placeSeq = CmmUtil.nvl(request.getParameter("placeSeq"));
            String placeName =CmmUtil.nvl(request.getParameter("placeName"));
            String placeNIck = CmmUtil.nvl(request.getParameter("placeNick"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ")); // 여행 번호(PK)
            String tourDay = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));

            log.info("placeSeq : " + placeSeq);
            log.info("tourSeq : " + tourSeq);
            log.info("tourDay : " + tourDay);

            TourDTO pDTO = new TourDTO();
            pDTO.setPlaceSeq(placeSeq);
            pDTO.setTourSeq(tourSeq);
            pDTO.setTourDay(tourDay);

            tourInfoService.resetPlaceEnd(pDTO);

            msg = placeName + "(" + placeNIck + ")" + " 방문 완료를 취소하셨습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".resetPlaceEnd End!");

        }

        return dto;
    }

    @ResponseBody
    @PostMapping(value="setSessionMn")
    MsgDTO setSessionMn(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".setSessionMn Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        TourDTO pDTO = new TourDTO();

        String tourSeq = CmmUtil.nvl((String)session.getAttribute("SS_TOUR_SEQ"));
        String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

        log.info("tourSeq : " + tourSeq);
        log.info("userId : " + userId);

        pDTO.setTourSeq(tourSeq);
        pDTO.setUserId(userId);

        try {

            String startMn = CmmUtil.nvl(request.getParameter("startMn"));

            log.info("startMn : " + startMn);

            if (startMn.equals("Y")) {

                msg = "여행 안내를 수동으로 시작하셨습니다.";


            } else if (startMn.equals("N")) {

                msg = "여행 수동 안내를 취소하셨습니다.";

            } else {

                msg = "알 수 없는 문제가 발생했습니다. 로그아웃 후 다시 실행해주세요.";
            }

            session.setAttribute("SS_STARTTIME_MN", startMn);

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".setSessionMn End!");

        }

        return dto;
    }


}