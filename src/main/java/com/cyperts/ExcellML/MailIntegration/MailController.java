package com.cyperts.ExcellML.MailIntegration;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cyperts.ExcellML.FileOperations.FileOperations;
import com.cyperts.ExcellML.UserAndRole.User;
import com.cyperts.ExcellML.UserAndRole.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/api/mail")
@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
public class MailController {
	@Autowired
	MailIntegration mailIntegration;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MailHistoryReposistory mailHistoryReposistory;

//	@CrossOrigin(origins = "http://localhost:3000")
//	@PostMapping("/sendEmail")	
//	public String sendEmail(@RequestBody MailRequest mailRequest) {
//
//		String from = mailRequest.getEmailFrom();
//
//		String to = mailRequest.getEmailTo();
//
//		RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', '9').build();
//		String generate = generator.generate(6);
//		String subject = "Welcome to Excel- ML";
//		String msg = "Dear user,\nYour OTP is: " + generate;
//		mailRequest.setOtp(generate);
//		System.out.println("OTP::" + generate);
//		mailIntegration.sendMail(msg, subject, to, from, mailRequest);
//		return "Mail sent successfullyy...!!!";
//	}
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PostMapping("/EmailToSupplier/{id}/{userId}")
	public ResponseEntity<?> name(@PathVariable("id") long id, @PathVariable("userId") long userId) {

		try {
			mailIntegration.sendEmailToSupplier(id, userId);
			Optional<User> user = userRepository.findById(userId);
			System.out.println("Mail Sent successfully to supplier " + user.get().getFirstName());
			return ResponseEntity.ok("Mail Sent successfully to supplier " + user.get().getFirstName());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PostMapping("/mailToSupplier")
	public ResponseEntity<?> sendEmail(@RequestBody String emailBody) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(emailBody);
			String userId = jsonNode.get("userId").asText();
			String upc = jsonNode.get("upc").asText();
			String price = jsonNode.get("price").asText();
			String quantity = jsonNode.get("orderQty").asText();
			String productName = jsonNode.get("productName").asText();
			mailIntegration.mailToSupplier(userId, upc, price, quantity, productName);

			return ResponseEntity.ok("Mail Sent successfully");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/getListOfMailHistory/{pageNumber}")
	public ResponseEntity<List<MailHistory>> getListOfMailHistory(@PathVariable int pageNumber) {
		int pageSize = 20; // Number of items per page
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // Page number is 0-based

		Page<MailHistory> mailHistoryPage = mailHistoryReposistory.findAll(pageable);
		List<MailHistory> listOfMailHistory = mailHistoryPage.getContent();

		return ResponseEntity.ok(listOfMailHistory);

	}

	// to count whole rows for pagination
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/count")
	public ResponseEntity<Integer> getTotalNumberOfRows() {
		try {
			List<MailHistory> sortedMailHistory = mailHistoryReposistory.findAll();
			int rowCount = sortedMailHistory.size();
			return ResponseEntity.ok(rowCount);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(0);
		}
	}
}
