package dp.fdis.service.impl;

import dp.fdis.dto.TourDTO;
import dp.fdis.dto.UserInfoDTO;
import dp.fdis.persistance.mapper.ITourMapper;
import dp.fdis.service.ITourInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TourInfoService implements ITourInfoService {

    private final ITourMapper tourMapper;

    @Transactional
    @Override
    public List<TourDTO> getTourList(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourList start!");


        return tourMapper.getTourList(pDTO);
    }

    @Override
    public TourDTO getTourNameExists(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + "getTourNameExists Start!");

        TourDTO rDTO = tourMapper.getTourNameExists(pDTO);

        log.info(this.getClass().getName() + "getTourNameExists End!");

        return rDTO;
    }

    @Override
    public TourDTO getTourSeq(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourList start!");


        return tourMapper.getTourSeq(pDTO);
    }

    @Transactional
    @Override
    public TourDTO getTourInfo(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourInfo start!");


        return tourMapper.getTourInfo(pDTO);
    }

    @Transactional
    @Override
    public void insertTourInfo(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".InsertTourInfo start!");

        tourMapper.insertTourInfo(pDTO);

        log.info(this.getClass().getName() + ".InsertTourInfo End!");

    }

    @Transactional
    @Override
    public void updateTourName(TourDTO pDTO) throws Exception {


        log.info(this.getClass().getName() + ".updateTourName start!");

        tourMapper.updateTourName(pDTO);

        log.info(this.getClass().getName() + ".updateTourName End!");

    }

    @Transactional
    @Override
    public void updateTourStart(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateTourStart start!");

        tourMapper.updateTourStart(pDTO);

        log.info(this.getClass().getName() + ".updateTourStart End!");

    }

    @Transactional
    @Override
    public void updateTourEnd(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateTourEnd start!");

        tourMapper.updateTourEnd(pDTO);

        log.info(this.getClass().getName() + ".updateTourEnd End!");

    }

    @Transactional
    @Override
    public void updateTourProcess(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateTourProcess start!");

        tourMapper.updateTourProcess(pDTO);

        log.info(this.getClass().getName() + ".updateTourProcess End!");

    }

    @Transactional
    @Override
    public void deleteTourInfo(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteTourInfo start!");

        tourMapper.deleteTourInfo(pDTO);

        log.info(this.getClass().getName() + ".deleteTourInfo End!");

    }


    /**
     *  여기서부터 TOUR_DAY SERVICE
     */
    @Transactional
    @Override
    public List<TourDTO> getTourYn(TourDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getTourYn start!");

        return tourMapper.getTourYn(pDTO);
    }

    @Transactional
    @Override
    public List<TourDTO> getTourDay(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourDay start!");

        return tourMapper.getTourDay(pDTO);
    }

    @Transactional
    @Override
    public TourDTO getTourDayInfo(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourDayInfo start!");

        return tourMapper.getTourDayInfo(pDTO);
    }

    @Transactional
    @Override
    public void addTourDay(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".addTourDay start!");

        tourMapper.addTourDay(pDTO);

        log.info(this.getClass().getName() + ".addTourDay End!");

    }

    @Transactional
    @Override
    public void insertTourPlace(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertTourPlace start!");

        tourMapper.insertTourPlace(pDTO);

        log.info(this.getClass().getName() + ".insertTourPlace End!");

    }

    @Override
    public void insertTourPlaceMany(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertTourPlaceMany start!");

        tourMapper.insertTourPlaceMany(pDTO);

        log.info(this.getClass().getName() + ".insertTourPlaceMany End!");

    }

    @Transactional
    @Override
    public void updateTourPlace(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateTourPlace start!");

        tourMapper.updateTourPlace(pDTO);

        log.info(this.getClass().getName() + ".updateTourPlace End!");

    }

    @Transactional
    @Override
    public void updateTourDayMemo(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateTourDayMemo start!");

        tourMapper.updateTourDayMemo(pDTO);

        log.info(this.getClass().getName() + ".updateTourDayMemo End!");

    }

    @Transactional
    @Override
    public void updateTourDayYN(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateTourDayYN start!");

        tourMapper.updateTourDayYN(pDTO);

        log.info(this.getClass().getName() + ".updateTourDayYN End!");

    }

    @Transactional
    @Override
    public void updateTourDaySt(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateTourDaySt start!");

        tourMapper.updateTourDaySt(pDTO);

        log.info(this.getClass().getName() + ".updateTourDaySt End!");

    }

    @Transactional
    @Override
    public void deleteTourDayAll(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteTourDayAll start!");

        tourMapper.deleteTourDayAll(pDTO);

        log.info(this.getClass().getName() + ".deleteTourDayAll End!");

    }

    @Transactional
    @Override
    public void deleteTourDay(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteTourDay start!");

        tourMapper.deleteTourDay(pDTO);

        log.info(this.getClass().getName() + ".deleteTourDay End!");

    }


    /**
     *  여기서부터 TOUR_PLACE Service
     */

    @Transactional
    @Override
    public void deleteTourPlace(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteTourPlace start!");

        tourMapper.deleteTourPlace(pDTO);

        log.info(this.getClass().getName() + ".deleteTourPlace End!");

    }
}
