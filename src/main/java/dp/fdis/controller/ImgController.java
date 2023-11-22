package dp.fdis.controller;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import dp.fdis.dto.ImgDTO;
import dp.fdis.dto.MsgDTO;
import dp.fdis.dto.NoticeDTO;
import dp.fdis.service.IImgService;
import dp.fdis.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;

@RequiredArgsConstructor
@RequestMapping(value = "/img")
@Controller
@Slf4j
public class ImgController {

    private final IImgService iImgService;

    @GetMapping("/imgReg")
    public String getUpload(){

        log.info(this.getClass().getName() + "./imgReg Start!");

        return "thymeleaf/img/imgReg";
    }

    @ResponseBody
    @PostMapping("/imgGPS")
    public MsgDTO imgGPS(HttpSession session, MultipartHttpServletRequest mRequest) throws Exception {

        log.info(this.getClass().getName() + ".imgGPS Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {

            MultipartFile multipartFile = mRequest.getFile("file");

            File file= new File(multipartFile.getOriginalFilename());
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            Metadata metadata = ImageMetadataReader.readMetadata(file);

            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            if(!(gpsDirectory.isEmpty())) {

                double longitude = gpsDirectory.getGeoLocation().getLongitude();
                double latitude = gpsDirectory.getGeoLocation().getLatitude();

                log.info("위도 : " + latitude + ", 경도 : " + longitude);

                session.setAttribute("SS_IMG_LAT", latitude);
                session.setAttribute("SS_IMG_LON", longitude);

                msg = "등록 가능한 사진입니다..";

            } else if (gpsDirectory == null || gpsDirectory.isEmpty()) {

                msg = "위치 정보를 활성화하고 찍은 사진만 업로드 가능합니다.";

            }

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".imgGPS End!");
        }

        return dto;

    }

    /* 나중에 쓸 내용 임시 고정값 줌 */
/*            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
            String placeSeq = CmmUtil.nvl((String) session.getAttribute("SS_PLACE_SEQ"));
            String daySeq = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ")); */


    @PostMapping("/upload")
    @ResponseBody
    public String uploadFilesSample(
            HttpServletRequest request, MultipartHttpServletRequest mRequest) throws Exception {

        log.info(this.getClass().getName() + ".uploadFilesSample Start!");

        ImgDTO pDTO = new ImgDTO();

        MultipartFile multipartFile = mRequest.getFile("file");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        log.info("multipartFile : " + multipartFile);
        log.info("title : " + title);
        log.info("contents : " + contents);

        iImgService.upLoadImg(multipartFile, pDTO);

        log.info(this.getClass().getName() + ".uploadFilesSample End!");

        return "redirect:/test";
    }






}
