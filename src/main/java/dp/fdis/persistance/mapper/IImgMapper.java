package dp.fdis.persistance.mapper;

import dp.fdis.dto.ImgDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IImgMapper {

    ImgDTO getTourImgOne(ImgDTO pDTO) throws Exception;
    List<ImgDTO> getTourDayImg(ImgDTO pDTO) throws Exception;
    List<ImgDTO> getTourImgAll(ImgDTO pDTO) throws Exception;
    List<ImgDTO> getImgAll() throws Exception;
    void insertTourImg(ImgDTO pDTO) throws Exception;
    void updatePlaceImg(ImgDTO pDTO) throws Exception;
    void updateTourImg(ImgDTO pDTO) throws Exception;
    void updateTourImgInfo(ImgDTO pDTO) throws Exception;
    void deleteImgOne(ImgDTO pDTO) throws Exception;
    void deleteDayImg(ImgDTO pDTO) throws Exception;
    void deleteTourImg(ImgDTO pDTO) throws Exception;
    void initialTourImgInfo(ImgDTO pDTO) throws Exception;

    /* 이미지 등록되어있는지 확인 */



}
