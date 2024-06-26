package dp.fdis.controller;

import dp.fdis.dto.MsgDTO;
import dp.fdis.dto.TourDTO;
import dp.fdis.dto.UserInfoDTO;
import dp.fdis.service.ITourInfoService;
import dp.fdis.service.IUserInfoService;
import dp.fdis.util.CmmUtil;
import dp.fdis.util.DateUtil;
import dp.fdis.util.EncryptUtil;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Controller
public class UserInfoController {

    private final IUserInfoService userInfoService;
    private final ITourInfoService tourInfoService;

    /**
     * 회원 가입 화면으로 이동
     */

    @GetMapping(value = "userRegForm")
    public String userRegForm() {

        log.info(this.getClass().getName() + ".user/userRegForm");

        return "thymeleaf/user/userRegForm";
    }

    /**
     * 회원 가입 전 아이디 중복체크하기(Ajax를 통해 입력한 아이디 정보 받음)
     */
    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId")); // 회원 아이디

        log.info("userId : " + userId);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        //회원아이디를 통해 중복된 아이디인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }

    /**
     * 회원 가입 전 이메일 중복체크하기(Ajax를 통해 입력한 아이디 정보 받음)
     * 유효한 이메일인지 확인하기 위해 입력된 이메일에 인증번호 포함하여 메일 발송
     */
    @ResponseBody
    @PostMapping(value = "getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getEmailExists Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        //이메일을 통해 중복된 이메일인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".getEmailExists End!");

        return rDTO;
    }

    /**
     * 회원가입 로직 처리
     */

    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) throws  Exception {

        log.info(this.getClass().getName() + ".insertUserInfo start!");

        int res = 0; //회원가입 결과
        String msg = ""; //회원가입 결과에 대한 메세지를 전달할 변수
        MsgDTO dto = null; // 결과 메시지 구조

        //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
        UserInfoDTO pDTO = null;

        try {

            /*
             * ##################################################################
             *       웹(회원정보 입력화면)에서 받는 정보를 Stirng 변수에 저장 시작!
             *
             *  무조건 웹으로 받은 정보는 DTOP에 저장하기 위해 임시로 String 변수에 저장함
             * ##################################################################
             */

            String userId = CmmUtil.nvl(request.getParameter("userId")); //아이디
            String userName = CmmUtil.nvl(request.getParameter("userName")); //이름
            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호
            String email = CmmUtil.nvl(request.getParameter("email")); // 이메일
            String addr1 = CmmUtil.nvl(request.getParameter("addr1")); // 주소
            String addr2 = CmmUtil.nvl(request.getParameter("addr2")); // 상세주소

            /*
             * ##################################################################
             *      웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 끝!!
             *
             * ##################################################################
             */

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            /*
             * ##################################################################
             *
             *      웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 시작!!
             *
             * ##################################################################
             */

            pDTO = new UserInfoDTO();


            pDTO.setUserId(userId);
            pDTO.setUserName(userName);
            //비밀번호는 복호화 X 해시 알고리즘으로 암호화
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));
            //민감 정보인 이메일은 AES128-CBC로 암호화[복호화 가능]
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            /*
             * ##################################################################
             *
             *      웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 끝!!
             *
             * ##################################################################
             */

            /*
             * 회원가입
             */

            res = userInfoService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";
            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";
            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메세지
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            //결과 메세지 전달하기
            dto = new MsgDTO();
            dto.setResult(1);
            dto.setMsg(msg);

            log.info("dto.Msg : " + dto.getMsg());
            log.info("dto.Result : ", dto.getResult());

            log.info(this.getClass().getName() + ".insertUserInfo End!");
        }

        // Controller에서 JSP로 객체 전달 model 객체에 값을 넣는다.
        // msg와 pDTO(dto) 전달
        return dto;
    }

    /**
     * 유저 리스트 보여주기
     * <p>
     * GetMapping(value = "user/userList") =>  GET방식을 통해 접속되는 URL이 mail/mailList 경우 아래 함수를 실행함
     */
    @GetMapping(value = "userList")
    public String noticeList(HttpSession session, ModelMap model)
            throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".userList Start!");



        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<UserInfoDTO> rList = Optional.ofNullable(userInfoService.getUserList())
                .orElseGet(ArrayList::new);


        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".userList End!");

        // 함수 처리가 끝나고 보여줄 JSP 파일명

        return "user/userList";

    }

    @GetMapping(value = "userInfo")
    public String userInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".userInfo Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId")); // USER_ID(PK)

        /*
         * ####################################################################################
         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
         * ####################################################################################
         */
        log.info("userId : " + userId);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        // 공지사항 상세정보 가져오기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                .orElseGet(UserInfoDTO::new);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".userInfo End!");

        // 함수 처리가 끝나고 보여줄 JSP 파일명
        // webapp/WEB-INF/views/notice/noticeInfo.jsp
        return "user/userInfo";
    }

    @GetMapping(value = "login")
    public String login() {
        log.info(this.getClass().getName() + ".user/login Start!");

        log.info(this.getClass().getName() + ".user/login End!");

        return "thymeleaf/user/login";
    }

    @PostMapping(value = "login")
    public String postLogin() {
        log.info(this.getClass().getName() + ".user/login Start!");

        log.info(this.getClass().getName() + ".user/login End!");

        return "thymeleaf/user/login";
    }

    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".loginProc Start!");

        int res = 0; // 로그인 처리 결과를 저장할 변수 (로그인 성공 : 1, 아이디, 비밀번호 불일치로 인한 실패 : 0, 시스템 에러 2)
        String msg = ""; // 로그인 결과에 대한 메세지 전달 변수
        MsgDTO dto = null; // 결과 메세지 구조

        //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
        UserInfoDTO pDTO = null;

        try {
            String userId = CmmUtil.nvl(request.getParameter("userId")); // 아이디
            String password = CmmUtil.nvl(request.getParameter("password")); // 비밀번호

            log.info("userId : " + userId);
            log.info("password : " + password);

            //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수를 메모리에 올리기
            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);

            //비밀번호는 절대 복호화되지 않도록 해시 알고리즘으로 암호화함
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기 위한 userInfoService 호출하기

            UserInfoDTO rDTO = userInfoService.getLogin(pDTO);

            if(CmmUtil.nvl(rDTO.getUserId()).length() > 0) { // 로그인 성공
                res = 1;

                msg = "로그인이 성공했습니다.";

                session.setAttribute("SS_USER_ID", CmmUtil.nvl(userId));
                session.setAttribute("SS_USER_NAME", CmmUtil.nvl(rDTO.getUserName()));
                session.setAttribute("SS_EMAIL", EncryptUtil.decAES128CBC(CmmUtil.nvl(rDTO.getEmail())));
                session.setAttribute("SS_ADDR", CmmUtil.nvl(rDTO.getAddr1()) + " " + CmmUtil.nvl(rDTO.getAddr2()));

            } else {
                msg = "아이디와 비빌번호가 올바르지 않습니다.";
            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메세지
            msg = "시스템 문제로 로그인이 실패했습니다.";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메세지 전달하기
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);
            log.info("result : " + dto.getResult());
            log.info("msg : " + dto.getMsg());
            log.info(this.getClass().getName() + ".loginProc End!");
        }
        return dto;
    }

    @GetMapping(value = "loginResult")
    public String loginResult() {
        log.info(this.getClass().getName() + ".user/loginResult Start!");

        log.info(this.getClass().getName() + ".user/loginResult End!");

        return "user/loginResult";
    }

    @GetMapping(value = "myPage")
    public String myPage(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".user/myPage Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        log.info("userId : " + userId);


        if (userId.length() > 0) {

            UserInfoDTO pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);

            UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                        .orElseGet(UserInfoDTO::new);

            // 날짜 포매터 정의 (yyyy-MM-dd 형식)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String[] regDt = rDTO.getRegDt().split(" ");

            log.info("DateUtil.getDateTime() : " + DateUtil.getDateTime());
            log.info("regDt : " + regDt[0]);

            // 문자열을 LocalDate으로 변환
            LocalDate date1 = LocalDate.parse(DateUtil.getDateTime(), formatter);
            LocalDate date2 = LocalDate.parse(regDt[0], formatter);

            // 두 날짜 간의 차이 계산 (일(day) 단위)
            int daysDifference = (int) java.time.temporal.ChronoUnit.DAYS.between(date2, date1);

            System.out.println("Days Difference: " + daysDifference);

            rDTO.setDayCnt(daysDifference);

            String email = EncryptUtil.decAES128CBC(CmmUtil.nvl(rDTO.getEmail()));

            log.info("dec email : " + email);

            rDTO.setEmail(email);



            // 여기서부터 여행 정보

            TourDTO tDTO = new TourDTO();
            tDTO.setUserId(userId);

            List<TourDTO> rList = Optional.ofNullable(tourInfoService.getTourList(tDTO))
                    .orElseGet(ArrayList::new);

            int beforeStart = rList.size();

            rList = Optional.ofNullable(tourInfoService.getTourEndList(tDTO))
                    .orElseGet(ArrayList::new);

            int afterEnd = rList.size();

            log.info("beforeStart : " + beforeStart);
            log.info("afterEnd : " + afterEnd);

            rDTO.setBeforeStart(beforeStart);
            rDTO.setAfterEnd(afterEnd);

            model.addAttribute("rDTO", rDTO);

        } else {

            return "/user/login";

        }


        log.info(this.getClass().getName() + ".user/myPage End!");

        return "thymeleaf/user/myPage";
    }

    @GetMapping(value = "myPageEdit")
    public String myPageEdit(HttpSession session, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".user/myPageEdit Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        log.info("userId : " + userId);


        if (userId.length() > 0) {

            UserInfoDTO pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);

            UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                    .orElseGet(UserInfoDTO::new);


            String email = EncryptUtil.decAES128CBC(CmmUtil.nvl(rDTO.getEmail()));

            log.info("dec email : " + email);

            rDTO.setEmail(email);

            model.addAttribute("rDTO", rDTO);

        } else {

            return "/user/login";

        }


        log.info(this.getClass().getName() + ".user/myPageEdit End!");

        return "thymeleaf/user/myPageEdit";
    }

    @ResponseBody
    @PostMapping(value = "updateUserInfo")
    public MsgDTO updateUserInfo(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".updateUserInfo Start!");

        String msg = ""; // 로그인 결과에 대한 메세지 전달 변수
        MsgDTO dto = null; // 결과 메세지 구조

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            if (CmmUtil.nvl(userId).length() > 0) {

            String userName = CmmUtil.nvl(request.getParameter("userName"));
            String email = CmmUtil.nvl(request.getParameter("email"));
            String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
            String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            UserInfoDTO pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);
            pDTO.setUserName(userName);
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            userInfoService.updateUserInfo(pDTO);


                msg = "유저 정보 변경하였습니다.";


            } else {

                msg = "로그인해주세요.";

            }

        } catch (Exception e) {

            msg = "시스템 문제로 유저 정보를 변경하지 못 했습니다.";
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메세지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info("msg : " + dto.getMsg());
            log.info(this.getClass().getName() + ".updateUserInfo End!");
        }

        return dto;
    }

    @ResponseBody
    @PostMapping(value = "myPageProc")
    public MsgDTO myPageProc(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".loginProc Start!");

        int res = 0; // 로그인 처리 결과를 저장할 변수 (로그인 성공 : 1, 아이디, 비밀번호 불일치로 인한 실패 : 0, 시스템 에러 2)
        String msg = ""; // 로그인 결과에 대한 메세지 전달 변수
        MsgDTO dto = null; // 결과 메세지 구조


        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            if (CmmUtil.nvl(userId).length() > 0) { // 로그인 성공
                res = 1;

                msg = "로그인이 되어있습니다.";


            } else {
                msg = "로그인해주세요.";
            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메세지
            msg = "시스템 문제로 마이페이지를 불러 올 수 없습니다.";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메세지 전달하기
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info("msg : " + dto.getMsg());
            log.info(this.getClass().getName() + ".loginProc End!");
        }

        return dto;
    }

    /**
     *  아이디 찾기 화면
     */

    @GetMapping(value = "searchUserId")
    public  String searchUserId() {
        log.info(this.getClass().getName() + ".user/searchUserId Start!");

        log.info(this.getClass().getName() + ".user/searchUserId End!");

        return "thymeleaf/user/searchUserId";
    }

    /**
     * 아이디 찾기 로직 수행
     */

    @PostMapping(value = "searchUserIdProc")
    public String searchUserIdProc(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".user/searchUserIdProc Start!");

        String userName = CmmUtil.nvl(request.getParameter("userName")); //이름
        String email = CmmUtil.nvl(request.getParameter("email")); //이메일

        log.info("userName : " + userName);
        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserName(userName);
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserIdOrPasswordPro(pDTO))
                .orElseGet(UserInfoDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".user/searchUserIdProc End!");

        return "thymeleaf/user/searchUserIdResult";

    }

    @GetMapping(value = "searchPassword")
    public String searchPassword(HttpSession session) {
        log.info(this.getClass().getName() + ".user/searchPassword Start!");

        session.setAttribute("NEW_PASSWORD", "");
        session.removeAttribute("NEW_PASSWORD");

        log.info(this.getClass().getName() + ".user/searchPassword End!");

        return "thymeleaf/user/searchPassword";

    }

    /**
     *  비밀번호 찾기 로직 수행
     *  <p>
     *  아이디, 이름, 이메일 일치하면, 비밀번호 재발급 화면 이동
     */

    @PostMapping(value = "searchPasswordProc")
    public String searchPasswordProc(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".user/searchPasswordProc Start!");

        String userId = CmmUtil.nvl((request.getParameter("userId"))); // 아이디
        String userName = CmmUtil.nvl((request.getParameter("userName"))); // 이름
        String email = CmmUtil.nvl((request.getParameter("email"))); // 이메일

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);
        pDTO.setUserName(userName);
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        // 비밀번호 찾기 가능한지 확인하기
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserIdOrPasswordPro(pDTO)).orElseGet(UserInfoDTO::new);

        model.addAttribute("rDTO", rDTO);

        // 비밀번호 재생성 화면은 보안을 위해 반드시 NEW_PASSWORD 세션이 존재해야 접속 가능하도록 구현
        // userId 값을 넣은 이유는 비밀번호 재설정하는 newPasswordProc 함수에서 사용하기 위함

        session.setAttribute("NEW_PASSWORD", userId);

        log.info(this.getClass().getName() + ".user/searchPasswordProc End!");

        return "thymeleaf/user/newPassword";
    }

    /**
     *  비밀번호 찾기 로직 수행
     *  <p>
     *  아이디, 이름, 이메일이 일치하면, 비밀번호 재발급 화면 이동
     */

    @PostMapping(value = "newPasswordProc")
    public String newPasswordProc(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".user/newPasswordProc Strat!");

        String msg = ""; // 웹에 보여줄 메세지

        // 정상적인 접근인지 체크
        String newPassword = CmmUtil.nvl((String) session.getAttribute("NEW_PASSWORD"));

        if(newPassword.length() > 0) { //정상접근
            String password = CmmUtil.nvl(request.getParameter("password")); //신규 비밀번호

            log.info("password : " + password);

            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setUserId(newPassword); // newPassword??
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            userInfoService.newPasswordProc(pDTO);

            // 비밀번호 재생성 화면은 보안을 위해 생성한 NEW_PASSWORD 세션 삭제
            session.setAttribute("NEW_PASSWORD", "");
            session.removeAttribute("NEW_PASSWORD");

            msg = "비밀번호가 재설정되었습니다.";

        } else { //비정상 접근
            msg = "비정상 접근입니다.";
        }

        model.addAttribute("msg", msg);

        log.info("msg : " + model.getAttribute("msg"));

        log.info(this.getClass().getName() + ".user/newPasswordProc End!");

        return "thymeleaf/user/newPasswordResult";
    }

    @ResponseBody
    @PostMapping(value = "sendEmailAuth")
    public UserInfoDTO SendEmailAuth(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".sendEmailAuth Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.sendEmailAuth(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".sendEmailAuth End!");

        return rDTO;
    }


    @GetMapping(value = "userLogOut")
    public String userLogout(HttpSession session) throws Exception {

        session.invalidate();

        return "/index/index";
    }

}
