package dp.fdis.controller;

import dp.fdis.dto.MsgDTO;
import dp.fdis.dto.MyAddrDTO;
import dp.fdis.service.IMyAddrSerivce;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value="/addr")
@Controller

public class MyAddrController {

    private final IMyAddrSerivce myAddrSerivce;

    @GetMapping(value = "myAddrList")
    public String myAddrList(ModelMap model)
            throws Exception {

        log.info(this.getClass().getName() + ".myAddrList Start!");

        List<MyAddrDTO> rList = Optional.ofNullable(myAddrSerivce.getMyAddrList())
                .orElseGet(ArrayList::new);


        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".myAddrList End!");

        return "addr/myAddrList";

    }

    @GetMapping(value = "myAddrReg")
    public String myAddrReg() {

        log.info(this.getClass().getName() + ".myAddrReg Start!");

        log.info(this.getClass().getName() + ".myAddrReg End!");

        return "addr/myAddrReg";
    }

    @ResponseBody
    @PostMapping(value = "myAddrInsert")
    public MsgDTO noticeInsert(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".myAddrInsert Start!");

        String msg = "";

        MsgDTO dto = null;

        try {
            // 로그인된 사용자 아이디를 가져오기
            // 로그인을 아직 구현하지 않았기에 공지사항 리스트에서 로그인 한 것처럼 Session 값을 저장함
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String addr = CmmUtil.nvl(request.getParameter("addr")); // 제목
            String memo = CmmUtil.nvl(request.getParameter("memo")); // 내용

            log.info("session user_id : " + userId);
            log.info("addr : " + addr);
            log.info("memo : " + memo);

            MyAddrDTO pDTO = new MyAddrDTO();
            pDTO.setUserId(userId);
            pDTO.setAddr(addr);
            pDTO.setMemo(memo);

            if(userId.length() > 0) {
                myAddrSerivce.insertMyAddr(pDTO);

                msg = "등록되었습니다.";
            } else {
                msg = "로그인 해주세요.";
            }

        } catch (Exception e) {

            // 저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".myAddrInsert End!");
        }

        return dto;
    }


    /**
     * 즐겨찾기 상세보기
     */
    @GetMapping(value = "myAddrInfo")
    public String myAddrInfo(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".myAddrInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("nSeq : " + nSeq);
        log.info("userId : " + userId);

        MyAddrDTO pDTO = new MyAddrDTO();
        pDTO.setSeq(nSeq);
        pDTO.setUserId(userId);

        MyAddrDTO rDTO = Optional.ofNullable(myAddrSerivce.getMyAddr(pDTO, true))
                .orElseGet(MyAddrDTO::new);

        log.info("rDTO 확인");
        log.info("userId : " + rDTO.getUserId());


        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".myAddrInfo End!");

        return "addr/myAddrInfo";
    }

    /**
     * 즐겨찾기 수정 보기
     */
    @GetMapping(value = "myAddrEdit")
    public String myAddrEdit(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".myAddrEdit Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("nSeq : " + nSeq);
        log.info("userId : " + userId);

        MyAddrDTO pDTO = new MyAddrDTO();
        pDTO.setSeq(nSeq);
        pDTO.setUserId(userId);

        MyAddrDTO rDTO = Optional.ofNullable(myAddrSerivce.getMyAddr(pDTO, true))
                .orElseGet(MyAddrDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".myAddrEdit End!");

        return "addr/myAddrEdit";
    }

    /**
     * 즐겨찾기 글 수정
     */
    @ResponseBody
    @PostMapping(value = "myAddrUpdate")
    public MsgDTO myAddrUpdate(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".myAddrUpdate Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // 아이디
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 주소번호(PK)
            String addr = CmmUtil.nvl(request.getParameter("addr")); // 제목
            String memo = CmmUtil.nvl(request.getParameter("memo")); // 공지글 여부

            log.info("userId : " + userId);
            log.info("nSeq : " + nSeq);
            log.info("addr : " + addr);
            log.info("memo : " + memo);

            MyAddrDTO pDTO = new MyAddrDTO();
            pDTO.setUserId(userId);
            pDTO.setSeq(nSeq);
            pDTO.setAddr(addr);
            pDTO.setMemo(memo);

            myAddrSerivce.updateMyAddr(pDTO);

            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".myAddrUpdate End!");

        }

        return dto;
    }

    /**
     * 즐겨찾기 글 삭제
     */
    @ResponseBody
    @PostMapping(value = "myAddrDelete")
    public MsgDTO myAddrDelete(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".myAddrDelete Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));

            log.info("nSeq : " + nSeq);

            MyAddrDTO pDTO = new MyAddrDTO();

            pDTO.setUserId(userId);
            pDTO.setSeq(nSeq);

            myAddrSerivce.deleteMyAddr(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".myAddrDelete End!");
        }

        return dto;
    }

}
