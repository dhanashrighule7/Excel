package com.cyperts.ExcellML.MailIntegration;

import org.springframework.stereotype.Component;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Component
public class MailRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String msg;
	@NonNull
	private String emailTo;
	private String emailSubject;
	private String emailFrom;
	private String otp;

	public long getId() {
		return id;
	}

	public MailRequest() {}

	public void setId(long id) {
		this.id = id;
	}

	public String getOtp() {
		return otp;
	}

	public String setOtp(String otp) {
		return this.otp = otp;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public MailRequest(long id, String msg, String emailTo, String emailSubject, String emailFrom, String otp) {
		super();
		this.id = id;
		this.msg = msg;
		this.emailTo = emailTo;
		this.emailSubject = emailSubject;
		this.emailFrom = emailFrom;
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "MailRequest [id=" + id + ", msg=" + msg + ", emailTo=" + emailTo + ", emailSubject=" + emailSubject
				+ ", emailFrom=" + emailFrom + ", otp=" + otp + "]";
	}
	
	

}
