package dp.fdis.test;

import dp.fdis.service.ITourInfoService;
import dp.fdis.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;


public class test {

    public static void main(String[] args) {


        String today = DateUtil.getDateTime();

        System.out.println("today : " + today);


    }
}
