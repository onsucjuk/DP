package dp.fdis.service;

import dp.fdis.dto.ImgDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImgService {

    String endPoint  = "https://kr.object.ncloudstorage.com";
    String regionName = "kr-standard";

    void upLoadImg(MultipartFile multipartFile, ImgDTO pDTO) throws Exception;

    void deleteImg(ImgDTO pDTO) throws Exception;

}
