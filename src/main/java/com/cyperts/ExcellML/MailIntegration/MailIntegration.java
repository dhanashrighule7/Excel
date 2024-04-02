package com.cyperts.ExcellML.MailIntegration;

import java.util.Optional;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cyperts.ExcellML.FileOperations.FileOperationRepository;
import com.cyperts.ExcellML.FileOperations.FileOperations;
import com.cyperts.ExcellML.UserAndRole.User;
import com.cyperts.ExcellML.UserAndRole.UserRepository;

@Component
public class MailIntegration {
	private static Session session = null;
	@Autowired
	MailRequest mailRequest;

	@Autowired
	FileOperationRepository fileOperationRepository;

	@Autowired
	UserRepository userRepository;
	@Autowired
	MailRequestRepository mailRequestRepository;
	@Autowired
	MailHistoryReposistory mailHistoryRepository;

	public void sendEmailToSupplier(Long dataId, Long userId) {
		try {

			User user = userRepository.getDataById(userId);
			FileOperations orderData = fileOperationRepository.getFileoperationByDataIdAndUserId(dataId, userId);
			Session mailSession = getMailSession();
			MimeMessage msg = new MimeMessage(mailSession);
			String orderQty = orderData.getOrderQty();
			String firstName = user.getFirstName();
			String price = orderData.getPrice();
			String organisationName = user.getOrganisationName();
			String productName = orderData.getProductName();
			long upc = orderData.getUpc();
			String mUpc = Long.toString(upc);
			System.out.println("check point 1");
			msg.setFrom("info@beautyblush.com");
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			msg.setSubject("Order Data");

			String content = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "\r\n" + "<head>\r\n"
					+ "    <meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1\\\">\r\n"
					+ "    <style>\r\n" + "        body {\r\n" + "            margin: 0;\r\n"
					+ "            overflow: hidden;\r\n" + "        }\r\n" + "\r\n" + "        .mail-container {\r\n"
					+ "            position: relative;\r\n" + "            float: left;\r\n"
					+ "            width: 55%;\r\n" + "            height: 100vh;\r\n"
					+ "            margin: 0 22.5%;\r\n" + "            margin-top: 3vh;\r\n" + "        }\r\n" + "\r\n"
					+ "        .mail-msg-box {\r\n" + "            position: relative;\r\n"
					+ "            float: left;\r\n" + "            width: 70%;\r\n" + "            margin: 0 15%;\r\n"
					+ "            height: auto;\r\n" + "            background-color: white;\r\n"
					+ "            border: 1px solid gray;\r\n" + "        }\r\n" + "\r\n" + "        .heading {\r\n"
					+ "            position: relative;\r\n" + "            float: left;\r\n"
					+ "            width: 100%;\r\n" + "            height: auto;\r\n" + "            color: white;\r\n"
					+ "            font-size: 1.5rem;\r\n" + "            font-family: Georgia;\r\n"
					+ "            background-color: #eb8d00;\r\n" + "            padding: 5px 0;\r\n" + "        }\r\n"
					+ "\r\n" + "        .welcome {\r\n" + "            font-size: 1.15em;\r\n" + "        }\r\n"
					+ "\r\n" + "        .body {\r\n" + "            position: relative;\r\n"
					+ "            float: left;\r\n" + "            width: 100%;\r\n" + "            padding: 2rem;\r\n"
					+ "            box-sizing: border-box;\r\n" + "        }\r\n" + "\r\n" + "        .body>span {\r\n"
					+ "            color: black;\r\n" + "        }\r\n" + "\r\n" + "        .salutation-row {\r\n"
					+ "            position: relative;\r\n" + "            float: left;\r\n"
					+ "            width: 100%;\r\n" + "            font-size: 1rem;\r\n" + "        }\r\n" + "\r\n"
					+ "        .congrts {\r\n" + "            font-size: 1.25em;\r\n" + "        }\r\n" + "\r\n"
					+ "        .regi-msg {\r\n" + "            font-size: 1.15em;\r\n" + "            color: black;\r\n"
					+ "        }\r\n" + "\r\n" + "        .user-name {\r\n" + "            font-weight: bold;\r\n"
					+ "            color: darkgray;\r\n" + "            font-size: 1rem;\r\n"
					+ "            text-align: center;\r\n" + "        }\r\n" + "\r\n"
					+ "        .default-password,\r\n" + "        .email-otp {\r\n"
					+ "            position: relative;\r\n" + "            float: left;\r\n"
					+ "            width: 100%;\r\n" + "            color: #60686f;\r\n"
					+ "            margin-top: 3vh;\r\n" + "        }\r\n" + "\r\n"
					+ "        .default-password span,\r\n" + "        .email-otp span {\r\n"
					+ "            color: black;\r\n" + "            font-weight: bold;\r\n"
					+ "            margin-left: 3px;\r\n" + "        }\r\n" + "\r\n" + "        .footer {\r\n"
					+ "            position: relative;\r\n" + "            float: left;\r\n"
					+ "            width: 100%;\r\n" + "            height: auto;\r\n"
					+ "            margin: 3vh 0 5vh;\r\n" + "            padding: 5px 0;\r\n"
					+ "            text-align: center;\r\n" + "        }\r\n" + "\r\n"
					+ "        @media only screen and (max-width: 480px) {\r\n" + "            .body {\r\n"
					+ "                padding: 2rem 0.5em;\r\n" + "            }\r\n" + "\r\n"
					+ "            .mail-container {\r\n" + "                width: 90%;\r\n"
					+ "                margin: 0 5%;\r\n" + "            }\r\n" + "\r\n"
					+ "            .mail-msg-box {\r\n" + "                width: 95%;\r\n"
					+ "                margin: 0 2.5%;\r\n" + "            }\r\n" + "\r\n"
					+ "            .heading {\r\n" + "                font-size: 0.9rem;\r\n" + "            }\r\n"
					+ "\r\n" + "            .welcome {\r\n" + "                font-size: 1em;\r\n"
					+ "            }\r\n" + "\r\n" + "            .salutation-row {\r\n"
					+ "                font-size: 0.9rem;\r\n" + "            }\r\n" + "\r\n"
					+ "            .congrts {\r\n" + "                font-size: 1.1em;\r\n" + "            }\r\n"
					+ "\r\n" + "            .regi-msg {\r\n" + "                font-size: 1.1em;\r\n"
					+ "                width: 100%;\r\n" + "            }\r\n" + "\r\n"
					+ "            .default-password,\r\n" + "            .email-otp {\r\n"
					+ "                font-size: 0.85em;\r\n" + "            }\r\n" + "        }\r\n"
					+ "    </style>\r\n" + "</head>\r\n" + "\r\n" + "<body>\r\n"
					+ "    <div class=\"mail-container\">\r\n" + "        <div class=\"mail-msg-box\">\r\n"
					+ "            <div class=\"heading\">\r\n"
					+ "                <div class=\"welcome\" style=\"text-align: center; margin-top: 5px;\\\">Welcome! To Excel-Project</span></div>\r\n"
					+ "                \r\n" + "            </div>\r\n" + "            <div class=\"body\">\r\n"
					+ "                <div class=\"salutation-row\">\r\n"
					+ "                    <div class=\"congrts\" style=\"color: black; text-align: center;\">Hello, <span>$$firstName$$</span></div>\r\n"
//				+ "                    <div class=\"user-name\">$$UserName$$</div>\r\n"
					+ "                    <div class=\"regi-msg\" style=\"text-align: center;  margin-top: 5px; color: green; \">I want to buy this product</div>\r\n"
					+ "                </div>\r\n" + "                <div class=\"default-password\">\r\n"
					+ "                    <div style=\"text-align: center;\">Product name:<span>$$productName$$</span>\r\n"
					+ "                    </div>\r\n" + "                </div>\r\n"
					+ "                <div class=\"email-otp\">\r\n"
					+ "                    <div style=\"text-align: center;\">UPC:<span>$$mUpc$$</span> </div>\r\n"
					+ "                </div>\r\n" + "                <div class=\"email-otp\">\r\n"
					+ "                    <div style=\"text-align: center;\">Price:<span>$$price$$</span></div>\r\n"
					+ "                </div>\r\n" + "                <div class=\"email-otp\">\r\n"
					+ "                    <div style=\"text-align: center;\">Order Quantity:<span>$$orderQty$$</span></div>\r\n"
					+ "                </div>\r\n" + "            </div>\r\n"
					+ "                <div class=\\\\\\\"footer\\\\\\\">\r\n"
					+ "             <div  style=\"text-align: center;\"><span>$$organisationName$$</span></div>\r\n"
					+ "        </div>\r\n" + "            </div>\r\n" + "    </div>\r\n" + "</body>\r\n" + "\r\n"
					+ "</html>";

			content = content.replace("$$firstName$$", firstName);
			if (productName == null) {
				content = content.replace("$$productName$$", "NA");
			} else {
				content = content.replace("$$productName$$", productName);
			}
			if (mUpc == null) {
				content = content.replace("$$mUpc$$", "NA");
			} else {
				content = content.replace("$$mUpc$$", mUpc);
			}
			if (price == null) {
				content = content.replace("$$price$$", "NA");
			} else {
				content = content.replace("$$price$$", price);
			}
			if (orderQty == null) {
				content = content.replace("$$orderQty$$", "NA");
			} else {
				content = content.replace("$$Qty$$", orderQty);
			}
			if (organisationName == null) {
				content = content.replace("$$organisationName$$", "NA");
			} else {
				content = content.replace("$$organisationName$$", organisationName);
			}

			msg.setContent(content, "text/html");
			Transport.send(msg);
			System.out.println("check point 2");

			MailHistory mm = new MailHistory(productName, mUpc, orderQty, firstName, organisationName, user.getEmail(),
					price);
			mailHistoryRepository.save(mm);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void mailToSupplier(String userId, String upc, String price, String quantity, String productName) {
		try {
			System.out.println("Check userID:" + userId);
			Optional<User> dataFromUser = userRepository.findById(Long.parseLong(userId));
			String email = dataFromUser.get().getEmail();
			String firstName = dataFromUser.get().getFirstName();
			String lastName = dataFromUser.get().getLastName();
			String organisationName = dataFromUser.get().getOrganisationName();
			Session mailSession = getMailSession();
			MimeMessage msg = new MimeMessage(mailSession);

			System.out.println("check point 1");
			msg.setFrom("info@beautyblush.com");
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject("Order Data");

			System.out.println("Check Order Qty" + quantity);

			String content = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "\r\n" + "<head>\r\n"
					+ "	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n" + "	<style>\r\n"
					+ "		body {\r\n" + "			margin: 0;\r\n" + "			overflow: hidden;\r\n" + "\r\n"
					+ "		}\r\n" + "\r\n" + "		.mail-container {\r\n" + "			position: relative;\r\n"
					+ "			float: left;\r\n" + "\r\n" + "			width: 55%;\r\n"
					+ "			height: 100vh;\r\n" + "\r\n" + "			margin: 0 22.5%;\r\n"
					+ "			margin-top: 3vh;\r\n" + "\r\n" + "		}\r\n" + "\r\n" + "\r\n"
					+ "		.mail-msg-box {\r\n" + "			position: relative;\r\n" + "\r\n"
					+ "			float: left;\r\n" + "			width: 70%;\r\n" + "			margin: 0 15%;\r\n"
					+ "\r\n" + "			height: auto;\r\n" + "			background-color: white;\r\n" + "\r\n"
					+ "			border: 1px solid gray;\r\n" + "\r\n" + "		}\r\n" + "\r\n" + "		.heading {\r\n"
					+ "\r\n" + "			position: relative;\r\n" + "			float: left;\r\n" + "\r\n"
					+ "			width: 100%;\r\n" + "			height: auto;\r\n" + "			color: white;\r\n"
					+ "			font-size: 1.5rem;\r\n" + "			font-family: Georgia;\r\n"
					+ "			background-color: #eb8d00;\r\n" + "			padding: 5px 0;\r\n" + "\r\n"
					+ "		}\r\n" + "\r\n" + "		.welcome {\r\n" + "			font-size: 1.15em;\r\n" + "\r\n"
					+ "		}\r\n" + "\r\n" + "		.body {\r\n" + "			position: relative;\r\n" + "\r\n"
					+ "			float: left;\r\n" + "			width: 100%;\r\n" + "			padding: 2rem;\r\n"
					+ "\r\n" + "			box-sizing: border-box;\r\n" + "\r\n" + "		}\r\n" + "\r\n"
					+ "		.body>span {\r\n" + "\r\n" + "			color: black;\r\n" + "\r\n" + "		}\r\n" + "\r\n"
					+ "		.salutation-row {\r\n" + "\r\n" + "			position: relative;\r\n"
					+ "			float: left;\r\n" + "\r\n" + "			width: 100%;\r\n"
					+ "			font-size: 1rem;\r\n" + "\r\n" + "		}\r\n" + "\r\n" + "\r\n"
					+ "		.congrts {\r\n" + "			font-size: 1.25em;\r\n" + "\r\n" + "		}\r\n" + "\r\n"
					+ "\r\n" + "		.regi-msg {\r\n" + "			font-size: 1.15em;\r\n"
					+ "			color: black;\r\n" + "\r\n" + "\r\n" + "		}\r\n" + "\r\n"
					+ "		.user-name {\r\n" + "			font-weight: bold;\r\n" + "\r\n"
					+ "			color: darkgray;\r\n" + "			font-size: 1rem;\r\n" + "\r\n"
					+ "			text-align: center;\r\n" + "\r\n" + "		}\r\n" + "\r\n" + "\r\n"
					+ "		.default-password,\r\n" + "		.email-otp {\r\n" + "\r\n"
					+ "			position: relative;\r\n" + "			float: left;\r\n" + "\r\n"
					+ "			width: 100%;\r\n" + "			color: #60686f;\r\n" + "\r\n"
					+ "			margin-top: 3vh;\r\n" + "\r\n" + "		}\r\n" + "\r\n" + "\r\n"
					+ "		.default-password span,\r\n" + "		.email-otp span {\r\n" + "\r\n"
					+ "			color: black;\r\n" + "			font-weight: bold;\r\n" + "\r\n"
					+ "			margin-left: 3px;\r\n" + "\r\n" + "		}\r\n" + "\r\n" + "		.footer {\r\n" + "\r\n"
					+ "			position: relative;\r\n" + "			float: left;\r\n"
					+ "			width: 100%;\r\n" + "			height: auto;\r\n" + "\r\n"
					+ "			margin: 3vh 0 5vh;\r\n" + "			padding: 5px 0;\r\n" + "\r\n"
					+ "			text-align: center;\r\n" + "\r\n" + "		}\r\n" + "\r\n" + "\r\n"
					+ "		@media only screen and (max-width: 480px) {\r\n" + "			.body {\r\n" + "\r\n"
					+ "				padding: 2rem 0.5em;\r\n" + "\r\n" + "			}\r\n" + "\r\n" + "\r\n"
					+ "			.mail-container {\r\n" + "				width: 90%;\r\n" + "\r\n"
					+ "				margin: 0 5%;\r\n" + "\r\n" + "			}\r\n" + "\r\n" + "\r\n"
					+ "			.mail-msg-box {\r\n" + "				width: 95%;\r\n" + "\r\n"
					+ "				margin: 0 2.5%;\r\n" + "\r\n" + "			}\r\n" + "\r\n" + "\r\n"
					+ "			.heading {\r\n" + "				font-size: 0.9rem;\r\n" + "\r\n" + "			}\r\n"
					+ "\r\n" + "\r\n" + "			.welcome {\r\n" + "				font-size: 1em;\r\n" + "\r\n"
					+ "\r\n" + "			}\r\n" + "\r\n" + "			.salutation-row {\r\n" + "\r\n"
					+ "				font-size: 0.9rem;\r\n" + "\r\n" + "			}\r\n" + "\r\n" + "\r\n"
					+ "			.congrts {\r\n" + "				font-size: 1.1em;\r\n" + "\r\n" + "			}\r\n"
					+ "\r\n" + "\r\n" + "			.regi-msg {\r\n" + "				font-size: 1.1em;\r\n" + "\r\n"
					+ "				width: 100%;\r\n" + "\r\n" + "			}\r\n" + "\r\n" + "\r\n"
					+ "			.default-password,\r\n" + "			.email-otp {\r\n" + "\r\n"
					+ "				font-size: 0.85em;\r\n" + "\r\n" + "			}\r\n" + "\r\n" + "\r\n"
					+ "		}\r\n" + "	</style>\r\n" + "</head>\r\n" + "\r\n" + "<body>\r\n"
					+ "	<div class=\"mail-container\">\r\n" + "		<div class=\"mail-msg-box\">\r\n"
					+ "			<div class=\"heading\">\r\n"
					+ "				<div class=\"welcome\" style=\"text-align: center; margin-top: 5px;\"></span>\r\n"
					+ "				</div>\r\n" + "\r\n" + "			</div>\r\n" + "\r\n"
					+ "			<div class=\"congrts\" style=\"color: black; text-align: center;\">Hello, <span>$$firstName$$</span>\r\n"
					+ "				<span>$$lastName$$</span><br> <span>$$organisationName$$</span></div>\r\n" + "\r\n"
					+ "\r\n" + "			<div class=\"default-password\">\r\n"
					+ "				<div class=\"regi-msg\" style=\"text-align: center; margin-top: 5px; color: green; \">Can you please give us\r\n"
					+ "					your best price for below quantity and product\r\n"
					+ "				</div><br>\r\n"
					+ "				<div style=\"text-align: center;\">Product name:<span>$$productName$$</span>\r\n"
					+ "				</div>\r\n" + "			</div>\r\n" + "			<div class=\"email-otp\">\r\n"
					+ "				<div style=\"text-align: center;\">UPC:<span>$$upc$$</span> </div>\r\n"
					+ "			</div>\r\n" + "			<div class=\"email-otp\">\r\n"
					+ "				<div style=\"text-align: center;\">Price:<span>$$price$$</span></div>\r\n"
					+ "			</div>\r\n" + "			<div class=\"email-otp\">\r\n"
					+ "				<div style=\"text-align: center;\">Order Quntity:<span>$$quantity$$</span></div><br>\r\n"
					+ "				<div style=\"text-align: center;\"><span style=\"color:orange;\">Beauty Blush</span></div>\r\n"
					+ "			</div>\r\n" + "\r\n" + "		</div>\r\n" + "		<!-- <div class=\"footer\">\r\n"
					+ "				<div style=\"text-align: center;\"><span>beautyblush</span></div>\r\n"
					+ "			</div> -->\r\n" + "	</div>\r\n" + "	</div>\r\n" + "</body>\r\n" + "\r\n" + "</html>";

//		User user = userRepository.getDataById(userId);
//		FileOperations orderData = fileOperationRepository.getFileoperationByDataIdAndUserId(id,userId);
//		
//		long upc = orderData.getUpc();

			System.out.println("Check Product::" + productName);

			content = content.replace("$$firstName$$", firstName);
			if (productName == null) {
				content = content.replace("$$productName$$", "NA");
			} else {
				content = content.replace("$$productName$$", productName);
			}

			if (upc == null) {
				content = content.replace("$$upc$$", "NA");
			} else {
				content = content.replace("$$upc$$", upc);
			}
			if (price == null) {
				content = content.replace("$$price$$", "NA");
			} else {
				content = content.replace("$$price$$", price);
			}
			if (quantity == null) {
				content = content.replace("$$quantity$$", "NA");
			} else {
				content = content.replace("$$quantity$$", quantity);
			}
			if (organisationName == null) {
				content = content.replace("$$organisationName$$", "NA");
			} else {
				content = content.replace("$$organisationName$$", organisationName);
			}
			if (lastName == null) {
				content = content.replace("$$lastName$$", "NA");
			} else {
				content = content.replace("$$lastName$$", lastName);
			}

			msg.setContent(content, "text/html");

			Transport.send(msg);
			System.out.println("check point 2");
			MailHistory mm = new MailHistory(productName, upc, quantity, firstName, organisationName, email, price);

			mailHistoryRepository.save(mm);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static Session getMailSession() {
		Properties properties = System.getProperties();
		System.out.println("Properties :" + properties);
		properties.put("mail.smtp.host", "smtp.office365.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.port", "587");
		properties.put("mail.smtp.starttls.enable", true);
//		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		// properties.put("mail.smtp.socketFactory.fallback", "false");
		session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("info@beautyblush.com", "Blushbeauty2023");
			}
		});
		session.setDebug(true);

		return session;

	}

	public Session getMailSessionforUser() {
		Properties properties = System.getProperties();
		System.out.println("Properties :" + properties);
		properties.put("mail.smtp.host", "smtp.office365.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.port", "587");
		properties.put("mail.smtp.starttls.enable", true);
//		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		// properties.put("mail.smtp.socketFactory.fallback", "false");
		session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("info@beautyblush.com", "Blushbeauty2023");
			}
		});
		session.setDebug(true);

		return session;

	}

}
