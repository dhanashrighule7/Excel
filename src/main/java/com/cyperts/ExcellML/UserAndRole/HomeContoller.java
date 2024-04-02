package com.cyperts.ExcellML.UserAndRole;

import java.util.List;
import java.util.Optional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyperts.ExcellML.MailIntegration.MailIntegration;
import com.cyperts.ExcellML.MailIntegration.MailRequest;
import com.cyperts.ExcellML.MailIntegration.MailRequestRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
public class HomeContoller {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	static MailIntegration mailIntegration;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MailRequestRepository mailRequestRepository;

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
		try {

			User user = userRepository.findByUsername(loginDto.getUsername());
			String dbPassword = user.getPassword();
			String logInPassword = loginDto.getPassword();

			if (user != null && passwordEncoder.matches(logInPassword, dbPassword)
					&& user.getStatus().equalsIgnoreCase("Active")) {
				System.out.println("check point 1");
				return new ResponseEntity<>(true, HttpStatus.OK);
			} else {
				if (user.getStatus().equalsIgnoreCase("InActive")) {
					return new ResponseEntity<>("User profile is inactive", HttpStatus.UNAUTHORIZED);
				}
				return new ResponseEntity<>("Invalid username and password", HttpStatus.UNAUTHORIZED);
			}

		}

		catch (Exception ex) {
			return new ResponseEntity<>("Invalid username and password", HttpStatus.UNAUTHORIZED);
		}
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUpDto) throws MessagingException {
		try {
			System.out.println("check point 1");
			List<User> findAll = userRepository.findAll();

			for (User user : findAll) {
				if (signUpDto.getUsername().equals(user.getUsername())) {
					System.out.println("check point 2");
					return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
				} else if (signUpDto.getEmail().equals(user.getEmail())) {
					System.out.println("check point 4");
					return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
				}
			}
			User user = new User();
			System.out.println("check point 5");
			user.setFirstName(signUpDto.getFirstName());
			user.setUsername(signUpDto.getUsername());
			user.setEmail(signUpDto.getEmail());
			user.setMobileNo(signUpDto.getMobileNo());
			user.setLastName(signUpDto.getLastName());
			user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
			user.setRole(signUpDto.getRole());
			user.setStatus("InActive");
			User newUser = userRepository.save(user);
			if (userRepository.existsById(newUser.getId())) {
				sendRegistrationMailToAdmin(newUser, signUpDto.getPassword());
			}
			System.out.println("check point 8");
			return new ResponseEntity<>(signUpDto.getRole() + " is registered successfully!", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<>("Contact Number already exist!", HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PostMapping("/internalSignup")
	public ResponseEntity<?> registerUserInternally(@RequestBody SignUpDTO signUpDto) throws MessagingException {
		System.out.println("check point 1");
		if (userRepository.existsByUsername(signUpDto.getUsername()) == null) {
			System.out.println("check point 2");
			return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
		}
		System.out.println("check point 3");
		if (userRepository.existsByEmail(signUpDto.getEmail()) == null) {
			System.out.println("check point 4");
			return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		System.out.println("check point 5");
		user.setFirstName(signUpDto.getFirstName());
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setMobileNo(signUpDto.getMobileNo());
		user.setLastName(signUpDto.getLastName());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
		user.setRole("Admin");
		user.setStatus("InActive");
		User newUser = userRepository.save(user);
		if (userRepository.existsById(newUser.getId())) {
			sendRegistrationMailToAdmin(newUser, signUpDto.getPassword());
		}

		System.out.println("check point 8");
		return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
	}

	private void sendRegistrationMailToAdmin(User newUser, String password) throws MessagingException {
		MailIntegration mail = new MailIntegration();
		Session session = mail.getMailSessionforUser();
		MimeMessage msg = new MimeMessage(session);
		String username = newUser.getUsername();
		String email = newUser.getEmail();
		String role = newUser.getRole();
		System.out.println("check point 1" + password);
		msg.setFrom("info@beautyblush.com");
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(newUser.getEmail()));
		msg.setSubject("Welcome in Beautyblush!!!");
		String content = "<!DOCTYPE html><html><head>\r\n"
				+ "				 <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
				+ "				 <style>      body { margin: 0; overflow: hidden; }\r\n"
				+ "				     .mail-container { position: relative; float: left; width: 55%; height: 100vh; margin: 0 22.5%; margin-top: 3vh; }\r\n"
				+ "				     .mail-msg-box { position: relative; float: left; width: 70%; margin: 0 15%; height: auto; background-color: white; border: 1px solid gray; }\r\n"
				+ "				     .heading { position: relative; float: left; width: 100%; height: auto; color: white; font-size: 1.5rem; font-family: Georgia; background-color: #eb8d00; padding: 5px 0; }\r\n"
				+ "				     .welcome { font-size: 1.15em; }\r\n"
				+ "				     .body { position: relative; float: left; width: 100%; padding: 2rem; box-sizing: border-box; }\r\n"
				+ "				     .body>span { color: black; }\r\n"
				+ "				     .salutation-row { position: relative; float: left; width: 100%; font-size: 1rem; }\r\n"
				+ "				     .congrts { font-size: 1.25em; }\r\n"
				+ "			      .regi-msg { font-size: 1.15em; color: black; }\r\n"
				+ "					    .user-name { font-weight: bold; color: darkgray; font-size: 1rem; text-align: center; }\r\n"
				+ "				     .default-password, .email-otp { position: relative; float: left; width: 100%; color: #60686f; margin-top: 3vh; }\r\n"
				+ "				     .default-password span, .email-otp span { color: black; font-weight: bold; margin-left: 3px; }\r\n"
				+ "			      .footer { position: relative; float: left; width: 100%; height: auto; margin: 3vh 0 5vh; padding: 5px 0; text-align: center; }\r\n"
				+ "			      @media only screen and (max-width: 480px) { .body { padding: 2rem 0.5em; } .mail-container { width: 90%; margin: 0 5%; } .mail-msg-box { width: 95%; margin: 0 2.5%; } }\r\n"
				+ "			  </style></head><body>    <div class=\"mail-container\">\r\n"
//				+ "			      <div class=\"mail-msg-box\\\">            <div class=\"heading\">\r\n"
////				+ "			              <div class=\"welcome\" style=\"text-align: center; margin-top: 5px;\">Welcome! To Excel</span></div>\r\n"
//				+ "			          </div>            <div class=\"body\">\r\n"
				+ "			              <div class=\"salutation-row\">\r\n"
				+ "			                  <div class=\"congrts\" style=\"color: green; text-align: center;\">You are registered successfully</div>\r\n"
				+ "							  <div class=\"congrts\" style=\"color: red; text-align: center;\">Use below username and password to login</div>\r\n"
				+ "							  <div style=\"text-align: center;\">User Name:<span>$$username$$</span></div>\r\n"
				+ "			                  <div style=\"text-align: center;\">Role:<span>$$role$$</span></div>\r\n"
				+ "			                  <div style=\"text-align: center;\">Email:<span>$$email$$</span></div>\r\n"
				+ "							  <div style=\"text-align: center;\">Password:<span>$$password$$</span></div>\r\n"
				+ "<br>" + "							 \r\n" + "\r\n"
				+ "				<div style=\"text-align: center;\"><span style=\"color:orange;\">Beauty Blush</span></div>\r\n"
				+ "			</div>\r\n" + "\r\n"
				+ "	                </div>           </div>        </div>   </div>\r\n" + "	</body></html>";

		content = content.replace("$$username$$", username);
		content = content.replace("$$email$$", email);
		content = content.replace("$$password$$", password);
		content = content.replace("$$role$$", role);
		msg.setContent(content, "text/html");
		Transport.send(msg);

	}

	private void sendRegistrationMailToSuperAdmin(User newUser) throws MessagingException {
		// Session session = mailIntegration.getMailSessionforUser();
		MailIntegration mail = new MailIntegration();
		Session session = mail.getMailSessionforUser();
		MimeMessage msg = new MimeMessage(session);
		String username = newUser.getUsername();
		String email = newUser.getEmail();
		String status = newUser.getStatus();
		System.out.println("check point 1");
		msg.setFrom("info@beautyblush.com");
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(newUser.getEmail()));
		msg.setSubject("Approval for Registration!!!");
		String content = "<!DOCTYPE html><html><head>\r\n"
				+ "				 <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
				+ "				 <style>      body { margin: 0; overflow: hidden; }\r\n"
				+ "				     .mail-container { position: relative; float: left; width: 55%; height: 100vh; margin: 0 22.5%; margin-top: 3vh; }\r\n"
				+ "				     .mail-msg-box { position: relative; float: left; width: 70%; margin: 0 15%; height: auto; background-color: white; border: 1px solid gray; }\r\n"
				+ "				     .heading { position: relative; float: left; width: 100%; height: auto; color: white; font-size: 1.5rem; font-family: Georgia; background-color: #eb8d00; padding: 5px 0; }\r\n"
				+ "				     .welcome { font-size: 1.15em; }\r\n"
				+ "				     .body { position: relative; float: left; width: 100%; padding: 2rem; box-sizing: border-box; }\r\n"
				+ "				     .body>span { color: black; }\r\n"
				+ "				     .salutation-row { position: relative; float: left; width: 100%; font-size: 1rem; }\r\n"
				+ "				     .congrts { font-size: 1.25em; }\r\n"
				+ "			      .regi-msg { font-size: 1.15em; color: black; }\r\n"
				+ "					    .user-name { font-weight: bold; color: darkgray; font-size: 1rem; text-align: center; }\r\n"
				+ "				     .default-password, .email-otp { position: relative; float: left; width: 100%; color: #60686f; margin-top: 3vh; }\r\n"
				+ "				     .default-password span, .email-otp span { color: black; font-weight: bold; margin-left: 3px; }\r\n"
				+ "			      .footer { position: relative; float: left; width: 100%; height: auto; margin: 3vh 0 5vh; padding: 5px 0; text-align: center; }\r\n"
				+ "			      @media only screen and (max-width: 480px) { .body { padding: 2rem 0.5em; } .mail-container { width: 90%; margin: 0 5%; } .mail-msg-box { width: 95%; margin: 0 2.5%; } }\r\n"
				+ "			  </style></head><body>    <div class=\"mail-container\">\r\n"
				+ "			      <div class=\"mail-msg-box\\\">            <div class=\"heading\">\r\n"
				+ "			              <div class=\"welcome\" style=\"text-align: center; margin-top: 5px;\">Welcome! To Excel</span></div>\r\n"
				+ "			          </div>            <div class=\"body\">\r\n"
				+ "			              <div class=\"salutation-row\">\r\n"
				+ "			                  <div class=\"congrts\" style=\"color: green; text-align: center;\">New User Registerd Successfully</div>\r\n"
				+ "							  <div class=\"congrts\" style=\"color: red; text-align: center;\">Please Activate the User</div>\r\n"
				+ "							  <div style=\"text-align: center;\">User Name:<span>$$username$$</span></div>\r\n"
				+ "			                  <div style=\"text-align: center;\">Email:<span>$$email$$</span></div>\r\n"
				+ "							  <div style=\"text-align: center;\">Status:<span>$$status$$</span></div>\r\n"
				+ "			              </div>               <div class=\"default-password\\\">\r\n"
				+ "			      \r\n" + "	                </div>           </div>        </div>   </div>\r\n"
				+ "	</body></html>";

		content = content.replace("$$username$$", username);
		content = content.replace("$$email$$", email);
		content = content.replace("$$status$$", status);
		msg.setContent(content, "text/html");
		Transport.send(msg);

	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/getUserData/{username}")
	public ResponseEntity<?> getUserData(@PathVariable String username) {
		User findByUsername = userRepository.findByUsername(username);
		if (findByUsername != null) {
			return ResponseEntity.ok(findByUsername);

		} else {
			return ResponseEntity.ok("User not found");
		}
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/user/{email}")
	public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
		System.out.println("Data fetched before fetching from DB:: ");
		User user = userRepository.findUserByEmail(email);
		System.out.println("Data fetched after :: ");
		if (user != null) {
			System.out.println("Inside IF block:: ");
			return ResponseEntity.ok(user);
		} else {
			System.out.println("Inside else block:: ");
			return ResponseEntity.ok("User not found");
		}
	}

//	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000") })
//	@PutMapping("/forgetpassword/{emailTo}/{otp}")
//	public String checkOTP(@PathVariable String emailTo, @PathVariable String otp, @RequestBody User user) {
//		System.out.println("Check0::  ");
//		User userByEmail = userRepository.findUserByEmail(emailTo);
//		System.out.println("Check1::  ");
//		System.out.println(userByEmail.toString());
//		System.out.println("Check2::  ");
//
//		String otpFromUser = otp;
//		System.out.println("Check3:  ");
//
//		System.out.println("OTP by user input::: " + otpFromUser);
//		System.out.println("Check email" + emailTo);
//		MailRequest byemailTo = mailRequestRepository.getByemailTo(emailTo);
//		System.out.println("Check Size::" + byemailTo.getOtp());
//		System.out.println("Check4::  ");
//
//		String otpFromDB = byemailTo.getOtp();
//		System.out.println("Check5::  ");
//		System.out.println("Check Otp::" + otpFromDB + "==" + otpFromUser);
//		if (otpFromDB.equals(otpFromUser)) {
//			System.out.println("In for block ::: ");
//			// user.setId(userByEmail.getId());
//			user.setEmail(userByEmail.getEmail());
//			user.setFirstName(userByEmail.getFirstName());
//			user.setLastName(userByEmail.getLastName());
//			user.setUsername(userByEmail.getUsername());
//			user.setMobileNo(userByEmail.getMobileNo());
//			// user.setRoles(userByEmail.getRoles());
//			user.setPassword(passwordEncoder.encode(user.getPassword()));
//			System.out.println("Password::" + user.getPassword());
//			userRepository.save(user);
//			System.out.println("::::: User Updated ::::::: ");
//		}
//		return "Password updated successfully!";
//	}

//	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
//	@PutMapping("/forgetpassword/{email}")
//	public String forgetpassword(@PathVariable String email, @RequestBody User user) {
//		System.out.println("Check0::  ");
//		User userByEmail = userRepository.findUserByEmail(email);
//		System.out.println("Check1::  ");
//		System.out.println(userByEmail.toString());
//		System.out.println("Check2::  ");
//
//		System.out.println("In for block ::: ");
//		user.setId(userByEmail.getId());
//		user.setEmail(userByEmail.getEmail());
//		user.setFirstName(userByEmail.getFirstName());
//		user.setLastName(userByEmail.getLastName());
//		user.setUsername(userByEmail.getUsername());
//		user.setMobileNo(userByEmail.getMobileNo());
//		user.setRole(userByEmail.getRole());
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		System.out.println("Password::" + user.getPassword());
//		userRepository.save(user);
//		System.out.println("::::: User Updated ::::::: ");
//
//		return "Password updated successfully!";
//	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PutMapping("/forgetpassword/{email}")
	public ResponseEntity<?> forgetpassword(@PathVariable String email, @RequestBody User user) {

		try {
			User userByEmail = userRepository.findUserByEmail(email);
			System.out.println(userByEmail.toString());

			System.out.println("In for block ::: ");
			if (userRepository.existsById(userByEmail.getId())) {
				user.setId(userByEmail.getId());
				user.setEmail(userByEmail.getEmail());
				user.setFirstName(userByEmail.getFirstName());
				user.setLastName(userByEmail.getLastName());
				user.setUsername(userByEmail.getUsername());
				user.setMobileNo(userByEmail.getMobileNo());
				user.setRole(userByEmail.getRole());
				System.out.println("Check Password::" + user.getPassword());
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setStatus(userByEmail.getStatus());
				System.out.println("Password::" + user.getPassword());
				user.setEditedOn(System.currentTimeMillis());
				userRepository.save(user);
				System.out.println("::::: User Updated ::::::: ");
				return new ResponseEntity<>("Password updated successfully!", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid E-mail, User not found", HttpStatus.UNAUTHORIZED);
		}
		return null;
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PutMapping("/update/{email}")
	public User updateUser(@PathVariable String email, @RequestBody User user) {
		System.out.println("check point0::: ");

		User userByEmail = userRepository.findUserByEmail(email);
		System.out.println("check point1::: ");
		if (userByEmail != null) {
			System.out.println("check point2::: ");

			user.setId(userByEmail.getId());
			user.setEmail(userByEmail.getEmail());
			user.setFirstName(user.getFirstName());
			user.setLastName(user.getLastName());
			user.setUsername(user.getUsername());
			user.setMobileNo(userByEmail.getMobileNo());
			// user.setRoles(userByEmail.getRoles());
			user.setPassword(userByEmail.getPassword());
			System.out.println("check point3::: ");

			return userRepository.save(user);
		}
		return null;
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PutMapping("/updateUserStatus/{id}")
	public User updateUserToSetStatus(@PathVariable long id, @RequestBody User user) {
		System.out.println("check point0::: ");

		Optional<User> userByEmail = userRepository.findById(id);
		User user2 = userByEmail.get();
		System.out.println("check point1::: ");
		System.out.println("check point2::: ");
		user.setId(user2.getId());
		user.setEmail(user2.getEmail());
		user.setFirstName(user2.getFirstName());
		user.setLastName(user2.getLastName());
		user.setUsername(user2.getUsername());
		user.setMobileNo(user2.getMobileNo());
		user.setRole(user2.getRole());
		user.setCreatedOn(user2.getCreatedOn());
		user.setEditedOn(System.currentTimeMillis());
		user.setPassword(user2.getPassword());
		user.setStatus(user.getStatus());
		System.out.println("check point3::: ");
		return userRepository.save(user);
	}
}