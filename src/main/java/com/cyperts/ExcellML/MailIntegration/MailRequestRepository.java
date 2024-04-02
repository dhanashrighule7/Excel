package com.cyperts.ExcellML.MailIntegration;

import javax.mail.internet.MimeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRequestRepository extends JpaRepository<MailRequest, Long> {

	void save(MimeMessage m);

	MailRequest getByemailTo(String emailTo);

//	void deleteByUserId(long id);

}
