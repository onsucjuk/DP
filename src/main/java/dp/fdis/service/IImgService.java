package dp.fdis.service;

import dp.fdis.dto.ImgDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImgService {

    String endPoint  = "https://kr.object.ncloudstorage.com";
    String regionName = "kr-standard";

    ImgDTO upLoadImg(MultipartFile multipartFile, ImgDTO pDTO) throws Exception;

    void deleteCloudImg(ImgDTO pDTO) throws Exception;

    void insertTourImg(ImgDTO pDTO) throws Exception;
    void insertLike(ImgDTO pDTO) throws Exception;
    void updatePlaceImg(ImgDTO pDTO) throws Exception;
    void updateTourImg(ImgDTO pDTO) throws Exception;
    void updateTourImgInfo(ImgDTO pDTO) throws Exception;
    void addImgLike(ImgDTO pDTO) throws Exception;
    void subImgLike(ImgDTO pDTO) throws Exception;
    ImgDTO getTourImgOne(ImgDTO pDTO) throws Exception;
    List<ImgDTO> getTourDayImg(ImgDTO pDTO) throws Exception;
    List<ImgDTO> getTourImgAll(ImgDTO pDTO) throws Exception;
    List<ImgDTO> getImgAll() throws Exception;
    List<ImgDTO> getMyImg(ImgDTO pDTO) throws Exception;

    ImgDTO checkImg(ImgDTO pDTO) throws Exception;

    void deleteImgOne(ImgDTO pDTO) throws Exception;
    void deleteDayImg(ImgDTO pDTO) throws Exception;
    void deleteTourImg(ImgDTO pDTO) throws Exception;
    void deleteLike(ImgDTO pDTO) throws Exception;
    void initialTourImgInfo(ImgDTO pDTO) throws Exception;

}
