package dp.fdis.service;

import dp.fdis.dto.ImgDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IImgService {

    String endPoint  = "https://kr.object.ncloudstorage.com";
    String regionName = "kr-standard";

    ImgDTO upLoadImg(MultipartFile multipartFile, ImgDTO pDTO) throws Exception;

    void deleteImg(ImgDTO pDTO) throws Exception;

    void insertTourImg(ImgDTO pDTO) throws Exception;
    void updatePlaceImg(ImgDTO pDTO) throws Exception;
    void updateTourImg(ImgDTO pDTO) throws Exception;

    ImgDTO getTourImgOne(ImgDTO pDTO) throws Exception;




}
