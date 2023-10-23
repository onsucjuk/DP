package dp.fdis.service;

import dp.fdis.dto.MailDTO;

public interface IMailService {

    int doSendMail(MailDTO pDTO);

}
