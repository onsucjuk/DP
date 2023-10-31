package dp.fdis.controller;

import dp.fdis.dto.UserInfoDTO;
import dp.fdis.service.IUserInfoService;
import dp.fdis.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/index")
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final IUserInfoService userInfoService;
    @GetMapping(value = "index")
    @PostMapping (value = "index")
    public String index(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".index/index Start!");

        UserInfoDTO pDTO = new UserInfoDTO();

        String ssUserId = (String) session.getAttribute("SS_USER_ID");

        pDTO.setUserId(ssUserId);

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                        .orElseGet(UserInfoDTO::new);

        if(CmmUtil.nvl(rDTO.getUserId()).length() > 0) { // 로그인 성공

             rDTO.setUserId(CmmUtil.nvl(ssUserId));
             rDTO.setUserName(CmmUtil.nvl(rDTO.getUserName()));

        }

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".index/index End!");

        return "thymeleaf/index/index";
    }

    @GetMapping(value = "404")
    public String userRegForm() {

        log.info(this.getClass().getName() + ".index/404");

        return "thymeleaf/index/404";
    }

}
