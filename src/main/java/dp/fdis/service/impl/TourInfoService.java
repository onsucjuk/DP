package dp.fdis.service.impl;

import dp.fdis.dto.TourDTO;
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



    @Transactional
    @Override
    public TourDTO getTourNameExists(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + "getTourNameExists Start!");

        TourDTO rDTO = tourMapper.getTourNameExists(pDTO);

        log.info(this.getClass().getName() + "getTourNameExists End!");

        return rDTO;
    }

    @Transactional
    @Override
    public TourDTO getTourSeq(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourList start!");


        return tourMapper.getTourSeq(pDTO);
    }

    @Transactional
    @Override
    public TourDTO tourSeqExists(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + "tourSeqExists Start!");

        TourDTO rDTO = tourMapper.tourSeqExists(pDTO);

        log.info(this.getClass().getName() + "tourSeqExists End!");

        return rDTO;
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
    public void resetTourStart(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".resetTourStart start!");

        tourMapper.resetTourStart(pDTO);

        log.info(this.getClass().getName() + ".resetTourStart End!");

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
/*
    @Transactional
    @Override
    public TourDTO getTourYn(TourDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getTourYn start!");

        return tourMapper.getTourYn(pDTO);
    }
*/

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
    public void resetTourDaySt(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".resetTourDaySt start!");

        tourMapper.resetTourDaySt(pDTO);

        log.info(this.getClass().getName() + ".resetTourDaySt End!");

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


    @Transactional
    @Override
    public List<TourDTO> getTourDayList(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".tourDayList start!");


        return tourMapper.getTourDayList(pDTO);
    }

    @Transactional
    @Override
    public TourDTO getTourDayInfo(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourDayInfo start!");


        return tourMapper.getTourDayInfo(pDTO);
    }


    /**
     *  여기서부터 TOUR_PLACE Service
     */

    @Transactional
    @Override
    public TourDTO getTourPlaceOne(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourPlaceOne start!");

        return tourMapper.getTourPlaceOne(pDTO);
    }

    @Transactional
    @Override
    public List<TourDTO> getTourPlace(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourPlace start!");


        return tourMapper.getTourPlace(pDTO);
    }


    @Transactional
    @Override
    public void deleteTourPlace(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteTourPlace start!");

        tourMapper.deleteTourPlace(pDTO);

        log.info(this.getClass().getName() + ".deleteTourPlace End!");

    }

    @Transactional
    @Override
    public void deleteTourPlaceOne(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteTourPlaceOne start!");

        tourMapper.deleteTourPlaceOne(pDTO);

        log.info(this.getClass().getName() + ".deleteTourPlaceOne End!");

    }


    @Transactional
    @Override
    public void updateTourPlace(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateTourPlace start!");

        tourMapper.updateTourPlace(pDTO);

        log.info(this.getClass().getName() + ".updateTourPlace End!");

    }


    // 장소 방문 완료, 취소

    @Transactional
    @Override
    public void updatePlaceEnd(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updatePlaceEnd start!");

        tourMapper.updatePlaceEnd(pDTO);

        log.info(this.getClass().getName() + ".updatePlaceEnd End!");

    }

    @Transactional
    @Override
    public void resetPlaceEnd(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".resetPlaceEnd start!");

        tourMapper.resetPlaceEnd(pDTO);

        log.info(this.getClass().getName() + ".resetPlaceEnd End!");

    }

    @Transactional
    @Override
    public TourDTO getTourDayYn(TourDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTourDayYn start!");

        return tourMapper.getTourDayYn(pDTO);
    }

}
