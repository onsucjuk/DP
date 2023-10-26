package dp.fdis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping(value = "/test")
@RequiredArgsConstructor
@Controller
public class testController {

    @GetMapping(value = "test")
    public String myAddrReg() {

        log.info(this.getClass().getName() + ".test Start!");

        log.info(this.getClass().getName() + ".test End!");

        return "thymeleaf/notice/tables";
    }
}
