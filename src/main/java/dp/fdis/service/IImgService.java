package dp.fdis.service;

import dp.fdis.dto.ImgDTO;

public interface IImgService {

    String endPoint  = "https://kr.object.ncloudstorage.com";
    String regionName = "kr-standard";

    void upLoadImg(ImgDTO pDTO) throws Exception;

    void deleteImg(ImgDTO pDTO) throws Exception;

}
