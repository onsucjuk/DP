package dp.fdis.service.impl;

import dp.fdis.dto.MyAddrDTO;
import dp.fdis.persistance.mapper.IMyAddrMapper;
import dp.fdis.service.IMyAddrSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyAddrService implements IMyAddrSerivce {

    private final IMyAddrMapper addrMapper;

    @Override
    public List<MyAddrDTO> getMyAddrList(MyAddrDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getAddrList start!");

        return addrMapper.getMyAddrList(pDTO);
    }

    @Override
    public MyAddrDTO getMyAddr(MyAddrDTO pDTO, boolean type) throws Exception {

        log.info(this.getClass().getName() + ".getMyAddr start!");

        return addrMapper.getMyAddr(pDTO);
    }

    @Override
    public void insertMyAddr(MyAddrDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertMyAddr start!");

        addrMapper.insertMyAddr(pDTO);

    }

    @Override
    public void updateMyAddr(MyAddrDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateMyAddr start!");

        addrMapper.updateMyAddr(pDTO);

    }

    @Override
    public void deleteMyAddr(MyAddrDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteMyAddr start!");

        addrMapper.deleteMyAddr(pDTO);

    }
}
