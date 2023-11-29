package dp.fdis.controller;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import dp.fdis.dto.ImgDTO;
import dp.fdis.dto.MsgDTO;
import dp.fdis.service.IImgService;
import dp.fdis.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping(value = "/img")
@Controller
@Slf4j
public class ImgController {

    private final IImgService iImgService;

    @GetMapping("/imgReg")
    public String imgReg(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + "./imgReg Start!");

        String placeSeq = CmmUtil.nvl(request.getParameter("placeSeq"));
        String lat = CmmUtil.nvl(request.getParameter("lat"));
        String lon = CmmUtil.nvl(request.getParameter("lon"));
        String placeNick = CmmUtil.nvl(request.getParameter("placeNick"));

        log.info("placeSeq : " + placeSeq);
        log.info("lat : " + lat);
        log.info("lon : " + lon);
        log.info("placeNick : " + placeNick);

        session.setAttribute("SS_PLACE_SEQ", placeSeq);
        session.setAttribute("SS_CHECK_LAT", lat);
        session.setAttribute("SS_CHECK_LON", lon);
        session.setAttribute("SS_PLACE_NICK", placeNick);

        return "thymeleaf/img/imgReg";
    }

    @GetMapping("/imgEdit")
    public String imgEdit(HttpSession session, HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + "./imgEdit Start!");

        String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
        String tourDay = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));
        String placeSeq = CmmUtil.nvl(request.getParameter("placeSeq"));
        String lat = CmmUtil.nvl(request.getParameter("lat"));
        String lon = CmmUtil.nvl(request.getParameter("lon"));
        String placeNick = CmmUtil.nvl(request.getParameter("placeNick"));


        log.info("tourSeq : " + tourSeq);
        log.info("tourDay : " + tourDay);
        log.info("placeSeq : " + placeSeq);
        log.info("lat : " + lat);
        log.info("lon : " + lon);
        log.info("placeNick : " + placeNick);


        session.setAttribute("SS_PLACE_SEQ", placeSeq);
        session.setAttribute("SS_CHECK_LAT", lat);
        session.setAttribute("SS_CHECK_LON", lon);
        session.setAttribute("SS_PLACE_NICK", placeNick);

        ImgDTO pDTO = new ImgDTO();


        pDTO.setTourSeq(tourSeq);
        pDTO.setTourDay(tourDay);
        pDTO.setPlaceSeq(placeSeq);

        ImgDTO rDTO = Optional.ofNullable(iImgService.getTourImgOne(pDTO))
                .orElseGet(ImgDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info("rDTO.contents : " + rDTO.getContents());

        return "thymeleaf/img/imgEdit";
    }


    @ResponseBody
    @PostMapping("/imgGPS")
    public MsgDTO imgGPS(HttpSession session, MultipartHttpServletRequest mRequest) throws Exception {

        log.info(this.getClass().getName() + ".imgGPS Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {

            MultipartFile multipartFile = mRequest.getFile("file");

            String filePath = "src/main/resources/static/upload/";
            String fileName = multipartFile.getOriginalFilename();

            File file = new File(filePath + fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            Metadata metadata = ImageMetadataReader.readMetadata(file);

            GpsDirectory gpsDirectory = Optional.ofNullable(metadata.getFirstDirectoryOfType(GpsDirectory.class))
                    .orElseGet(GpsDirectory::new);

            if (!(gpsDirectory.isEmpty())) {

                double longitude = gpsDirectory.getGeoLocation().getLongitude();
                double latitude = gpsDirectory.getGeoLocation().getLatitude();

                log.info("위도 : " + latitude + ", 경도 : " + longitude);

                double checkLat = Double.parseDouble(CmmUtil.nvl((String) session.getAttribute("SS_CHECK_LAT")));
                double checkLon = Double.parseDouble(CmmUtil.nvl((String) session.getAttribute("SS_CHECK_LON")));

                double minLat = checkLat - 0.003;
                double maxLat = checkLat + 0.003;
                double minLon = checkLon - 0.003;
                double maxLon = checkLon + 0.003;

                log.info("minLat : " + minLat);
                log.info("maxLat : " + maxLat);
                log.info("minLon : " + minLon);
                log.info("maxLon : " + maxLon);


                if (latitude >= minLat && latitude <= maxLat && longitude >= minLon && longitude <= maxLon) {

                    session.setAttribute("SS_IMG_LAT", latitude);
                    session.setAttribute("SS_IMG_LON", longitude);

                    msg = "등록 가능한 사진입니다.";

                } else {

                    msg = "해당 여행지에서 찍은 사진만 업로드 가능합니다.";

                }


            } else if (gpsDirectory.isEmpty()) {

                msg = "위치 정보를 활성화하고 찍은 사진만 업로드 가능합니다.";

            }

            try {

                Files.delete(Paths.get(filePath + fileName));

                log.info(filePath + fileName + "파일 삭제 완료");

            } catch (IOException e) {
                e.printStackTrace();
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
    public MsgDTO uploadFilesSample(HttpSession session,
                                    HttpServletRequest request, MultipartHttpServletRequest mRequest) throws Exception {

        log.info(this.getClass().getName() + ".uploadFilesSample Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {

            ImgDTO pDTO = new ImgDTO();

            MultipartFile multipartFile = mRequest.getFile("file");
            String title = CmmUtil.nvl(request.getParameter("title"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
            String tourDay = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));
            String placeSeq = CmmUtil.nvl((String) session.getAttribute("SS_PLACE_SEQ"));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));


            log.info("multipartFile : " + multipartFile);
            log.info("title : " + title);
            log.info("contents : " + contents);
            log.info("tourSeq : " + tourSeq);
            log.info("tourDay : " + tourDay);
            log.info("placeSeq : " + placeSeq);
            log.info("userId : " + userId);

            pDTO.setTourSeq(tourSeq);
            pDTO.setTourDay(tourDay);
            pDTO.setPlaceSeq(placeSeq);
            pDTO.setUserId(userId);

            ImgDTO rDTO = Optional.ofNullable(iImgService.upLoadImg(multipartFile, pDTO))
                    .orElseGet(ImgDTO::new);

            /* 이미지 업로드 후 이미지 게시글 삽입 */

            double lat = (double) session.getAttribute("SS_IMG_LAT");
            double lon = (double) session.getAttribute("SS_IMG_LON");
            String imgURL = rDTO.getImgURL();

            log.info("lat : " + lat);
            log.info("lon : " + lon);
            log.info("imgURL : " + imgURL);

            pDTO.setTitle(title);
            pDTO.setContents(contents);
            pDTO.setImgLat(lat);
            pDTO.setImgLon(lon);
            pDTO.setImgURL(imgURL);

            iImgService.insertTourImg(pDTO);
            iImgService.updatePlaceImg(pDTO);

            msg = "여행 사진 업로드 성공했습니다.";

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".uploadFilesSample End!");
        }

        session.removeAttribute("SS_PLACE_SEQ");
        session.removeAttribute("SS_IMG_LAT");
        session.removeAttribute("SS_IMG_LON");
        session.removeAttribute("SS_CHECK_LAT");
        session.removeAttribute("SS_CHECK_LON");
        session.removeAttribute("SS_PLACE_NICK");

        return dto;
    }

    @PostMapping("/editImg")
    @ResponseBody
    public MsgDTO editImg(HttpSession session,
                          HttpServletRequest request, MultipartHttpServletRequest mRequest) throws Exception {

        log.info(this.getClass().getName() + ".editImg Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {

            ImgDTO pDTO = new ImgDTO();

            MultipartFile multipartFile = mRequest.getFile("file");
            String title = CmmUtil.nvl(request.getParameter("title"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
            String tourDay = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));
            String placeSeq = CmmUtil.nvl((String) session.getAttribute("SS_PLACE_SEQ"));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));


            log.info("multipartFile : " + multipartFile);
            log.info("title : " + title);
            log.info("contents : " + contents);
            log.info("tourSeq : " + tourSeq);
            log.info("tourDay : " + tourDay);
            log.info("placeSeq : " + placeSeq);
            log.info("userId : " + userId);

            pDTO.setTourSeq(tourSeq);
            pDTO.setTourDay(tourDay);
            pDTO.setPlaceSeq(placeSeq);
            pDTO.setUserId(userId);

            ImgDTO rDTO = Optional.ofNullable(iImgService.upLoadImg(multipartFile, pDTO))
                    .orElseGet(ImgDTO::new);

            /* 이미지 업로드 후 이미지 게시글 삽입 */

            double lat = (double) session.getAttribute("SS_IMG_LAT");
            double lon = (double) session.getAttribute("SS_IMG_LON");
            String imgURL = rDTO.getImgURL();

            log.info("lat : " + lat);
            log.info("lon : " + lon);
            log.info("imgURL : " + imgURL);

            pDTO.setTitle(title);
            pDTO.setContents(contents);
            pDTO.setImgLat(lat);
            pDTO.setImgLon(lon);
            pDTO.setImgURL(imgURL);

            iImgService.updateTourImg(pDTO);
            iImgService.updatePlaceImg(pDTO);


            msg = "여행 사진 수정에 성공했습니다.";

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".editImg End!");
        }

        session.removeAttribute("SS_PLACE_SEQ");
        session.removeAttribute("SS_IMG_LAT");
        session.removeAttribute("SS_IMG_LON");
        session.removeAttribute("SS_CHECK_LAT");
        session.removeAttribute("SS_CHECK_LON");
        session.removeAttribute("SS_PLACE_NICK");

        return dto;
    }


    @PostMapping("/editWithoutImg")
    @ResponseBody
    public MsgDTO editWithoutImg(HttpSession session,
                                 HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".editImg Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {

            ImgDTO pDTO = new ImgDTO();

            String title = CmmUtil.nvl(request.getParameter("title"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
            String tourDay = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));
            String placeSeq = CmmUtil.nvl((String) session.getAttribute("SS_PLACE_SEQ"));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            log.info("title : " + title);
            log.info("contents : " + contents);
            log.info("tourSeq : " + tourSeq);
            log.info("tourDay : " + tourDay);
            log.info("placeSeq : " + placeSeq);
            log.info("userId : " + userId);

            pDTO.setTourSeq(tourSeq);
            pDTO.setTourDay(tourDay);
            pDTO.setPlaceSeq(placeSeq);
            pDTO.setUserId(userId);

            pDTO.setTitle(title);
            pDTO.setContents(contents);

            iImgService.updateTourImgInfo(pDTO);

            msg = "여행 사진 수정에 성공했습니다.";

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".editImg End!");
        }

        session.removeAttribute("SS_PLACE_SEQ");
        session.removeAttribute("SS_IMG_LAT");
        session.removeAttribute("SS_IMG_LON");
        session.removeAttribute("SS_CHECK_LAT");
        session.removeAttribute("SS_CHECK_LON");
        session.removeAttribute("SS_PLACE_NICK");

        return dto;
    }

    @ResponseBody
    @PostMapping(value = "deleteImgOne")
    public MsgDTO deleteImgOne(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".deleteImgOne Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {

            String placeSeq = CmmUtil.nvl((String) session.getAttribute("SS_PLACE_SEQ"));
            String tourDay = CmmUtil.nvl((String) session.getAttribute("SS_DAY_SEQ"));
            String tourSeq = CmmUtil.nvl((String) session.getAttribute("SS_TOUR_SEQ"));
            String imgURL = CmmUtil.nvl(request.getParameter("imgURL"));

            log.info("placeSeq : " + placeSeq);
            log.info("tourDay : " + tourDay);
            log.info("tourSeq : " + tourSeq);
            log.info("imgURL : " + imgURL);

            if (placeSeq.length() > 0 && tourDay.length() > 0 && tourSeq.length() > 0 && imgURL.length() > 0) {

                String uploadFileName = imgURL.substring(imgURL.lastIndexOf("/") + 1);

                ImgDTO pDTO = new ImgDTO();

                pDTO.setTourSeq(tourSeq);
                pDTO.setTourDay(tourDay);
                pDTO.setPlaceSeq(placeSeq);
                pDTO.setUploadFileName(uploadFileName);

                // 사진 정보 한 개 삭제하기 DB
                iImgService.deleteCloudImg(pDTO);
                iImgService.deleteImgOne(pDTO);
                iImgService.initialTourImgInfo(pDTO);

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

            log.info(this.getClass().getName() + ".deleteImgOne End!");

        }

        return dto;
    }


    @ResponseBody
    @PostMapping(value = "checkLike")
    public ImgDTO checkLike(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".checkLike Start!");

        ImgDTO rDTO = new ImgDTO();

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String imgSeq = CmmUtil.nvl(request.getParameter("imgSeq"));

            log.info("userId : " + userId);
            log.info("imgSeq : " + imgSeq);

            if (userId.length() > 0 && imgSeq.length() > 0) {

                ImgDTO pDTO = new ImgDTO();

                pDTO.setUserId(userId);
                pDTO.setImgSeq(imgSeq);

                rDTO = iImgService.checkImg(pDTO);

                log.info("count : " + rDTO.getLikeChk());

            }

        } catch (Exception e) {

            log.info(e.toString());
            e.printStackTrace();

        } finally {

            log.info(this.getClass().getName() + ".checkLike End!");

        }

        return rDTO;
    }


    @ResponseBody
    @PostMapping(value = "likeCheck")
    public MsgDTO likeCheck(HttpSession session, @RequestBody Map<String, Object> requestBody) {

        log.info(this.getClass().getName() + ".likeCheck Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조
        ImgDTO rDTO = new ImgDTO();
        int likeCount = 0;

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String imgSeq = CmmUtil.nvl(String.valueOf(requestBody.get("imgSeq")));

            log.info("userId : " + userId);
            log.info("imgURL : " + imgSeq);

            if (!(userId.length() > 0)) {

                msg = "로그인 해주세요.";

            } else if(!(imgSeq.length() > 0)) {

                msg = "이미지 정보가 옳지 않습니다.";

            }

            else if (userId.length() > 0 && imgSeq.length() > 0) {


                ImgDTO pDTO = new ImgDTO();

                pDTO.setUserId(userId);
                pDTO.setImgSeq(imgSeq);

                rDTO = iImgService.checkImg(pDTO);
                int likeChk = rDTO.getLikeChk();

                log.info("likeChk : " + likeChk);

                if (likeChk == 0) {

                    // Like 추가하기
                    iImgService.insertLike(pDTO);
                    iImgService.addImgLike(pDTO);

                    ImgDTO iDTO = Optional.ofNullable(iImgService.getTourImgOne(pDTO))
                            .orElseGet(ImgDTO::new);

                    likeCount = iDTO.getLikeCnt();

                    msg = "좋아요 했습니다.";

                } else if(likeChk == 1) {

                    msg = "이미 좋아요를 하셨습니다.";

                } else {

                    msg = "알 수 없는 문제가 발상했습니다.";

                }

            }

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);
            dto.setLikeCount(likeCount);

            log.info("msg : " + msg);
            log.info("likeCount : " + likeCount);
            log.info(this.getClass().getName() + ".likeCheck End!");

        }

        return dto;
    }


    @ResponseBody
    @PostMapping(value = "likeDel")
    public MsgDTO likeDel(HttpSession session, @RequestBody Map<String, Object> requestBody) {

        log.info(this.getClass().getName() + ".likeDel Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조
        ImgDTO rDTO = new ImgDTO();
        int likeCount = 0;

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String imgSeq = CmmUtil.nvl(String.valueOf(requestBody.get("imgSeq")));

            log.info("userId : " + userId);
            log.info("imgSeq : " + imgSeq);

            if (!(userId.length() > 0)) {

                msg = "로그인 해주세요.";

            } else if(!(imgSeq.length() > 0)) {

                msg = "이미지 정보가 옳지 않습니다.";

            }

            else if(userId.length() > 0 && imgSeq.length() > 0) {

                ImgDTO pDTO = new ImgDTO();

                pDTO.setUserId(userId);
                pDTO.setImgSeq(imgSeq);

                rDTO = iImgService.checkImg(pDTO);
                int likeChk = rDTO.getLikeChk();

                log.info("likeChk : " + likeChk);

                if (likeChk == 1) {

                    // Like 취소하기
                    iImgService.deleteLike(pDTO);
                    iImgService.subImgLike(pDTO);

                    ImgDTO iDTO = Optional.ofNullable(iImgService.getTourImgOne(pDTO))
                            .orElseGet(ImgDTO::new);

                    likeCount = iDTO.getLikeCnt();

                    msg = "좋아요를 취소했습니다.";

                } else if(likeChk == 0) {

                    msg = "먼저 좋아요를 해주세요.";

                } else {

                    msg = "알 수 없는 문제가 발상했습니다.";

                }

            }

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setMsg(msg);
            dto.setLikeCount(likeCount);

            log.info("msg : " + msg);
            log.info("likeCount : " + dto.getLikeCount());
            log.info(this.getClass().getName() + ".likeDel End!");

        }

        return dto;
    }
}