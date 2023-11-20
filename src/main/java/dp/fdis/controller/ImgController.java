package dp.fdis.controller;

import dp.fdis.dto.ImgDTO;
import dp.fdis.service.IImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ImgController {

    private final IImgService iImgService;

    @GetMapping("/test")
    public String getUpload(){

        log.info(this.getClass().getName() + "./test Start!");

        return "thymeleaf/test/test";
    }

    @PostMapping("/upload")
    @ResponseBody
    public void uploadFilesSample(
        @RequestPart(value = "file") MultipartFile multipartFile) throws Exception {

            log.info(this.getClass().getName() + ".uploadFilesSample Start!");

                ImgDTO pDTO = new ImgDTO();

                iImgService.upLoadImg(multipartFile, pDTO);

            log.info(this.getClass().getName() + ".uploadFilesSample End!");
    }





}
