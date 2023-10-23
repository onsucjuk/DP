package dp.fdis.service.impl;

import dp.fdis.dto.MailDTO;
import dp.fdis.service.IMailService;
import dp.fdis.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService implements IMailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public int doSendMail(MailDTO pDTO) {

        // 로그 찍기
        log.info(this.getClass().getName() + "doSendMail start!");

        // 메일 발송 성공여부(발송성공 : 1 / 발송실패 : 0)
        int res = 1;

        // 전달 받은 DTO로부터 데이터 가져오기(DTO객체가 메모리에 올라가지 않아 Null이 발생할 수 있기 때문에 에러방지 차원으로 if문 사용

        if (pDTO == null) {
            pDTO = new MailDTO();
        }

        String toMail = CmmUtil.nvl(pDTO.getToMail()); // 받는 사람
        String title = CmmUtil.nvl(pDTO.getTitle()); // 메일 제목
        String contents = CmmUtil.nvl(pDTO.getContents()); // 메일 내용

        log.info("toMail " + toMail);
        log.info("title " + title);
        log.info("contents : " + contents);

        // 메일 발송 메세지 구조(파일 첨부 가능)
        MimeMessage message = mailSender.createMimeMessage();

        // 메일 발송 메세지 구조를 쉽게 생성하게 도와주는 객체

        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");

        try {
            messageHelper.setTo(toMail); // 받는 사람
            messageHelper.setFrom(fromMail); // 보내는 사람
            messageHelper.setSubject(title); // 메일 제목
            messageHelper.setText(contents); // 메일 내용

            mailSender.send(message);

        } catch (Exception e) { //모든 에러 다 잡기
            res = 0 ; // 메일 발송 실패
            log.info("[ERRPR] " + this.getClass().getName() + ".doSendMail : " + e);
        }

        // 로그 찍기
        log.info(this.getClass().getName() + ".doSendMail end!");

        return res;
    }
}
