package dp.fdis.persistance.mapper;

import dp.fdis.dto.ImgDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IImgMapper {

    void insertTourImg(ImgDTO pDTO) throws Exception;

    /* 이미지 등록되어있는지 확인 */
    ImgDTO imgYn(ImgDTO pDTO) throws Exception;

}
