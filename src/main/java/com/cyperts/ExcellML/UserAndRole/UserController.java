package com.cyperts.ExcellML.UserAndRole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
public class UserController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PostMapping("/addSupplier")
	public ResponseEntity<?> toAddUser(@RequestBody User user) {
		try {
			user.setCreatedOn(System.currentTimeMillis());
			user.setRole("Supplier");
			List<User> findAll = userRepo.findAll();
			for (User user2 : findAll) {
				String email = user2.getEmail();
				if (user.getEmail().equals(email)) {
					return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
				}
			}

			User supplier = userRepo.save(user);
			return ResponseEntity.ok(supplier);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<>("Contact Number already exist", HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PostMapping("/addAdmin")
	public User toAddAdmin(@RequestBody User user) {
		user.setCreatedOn(System.currentTimeMillis());
		user.setRole("Admin");
		User admin = userRepo.save(user);
		return admin;
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/getSupplierList")
	public List<User> getListOfSuppliers() {
		List<User> listOfSuppliers = userRepo.getDataByRole("Supplier");
		return listOfSuppliers;
	}

//get supplierList pagewise
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/getSupplierList/{pageNumber}")
	public List<User> getSupplierList(@PathVariable int pageNumber) {
		int pageSize = 20;
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		Page<User> listOfSuppliers = userRepo.getAllSuppliersByRole("Supplier", pageable);
		List<User> suppliersList = listOfSuppliers.getContent();
		return suppliersList;
	}

	@CrossOrigin(origins = { "http://72.167.32.189", "http://localhost:3000", "https://beautyblush.ca" })
	@GetMapping("/getAllList")
	public ResponseEntity<List<User>> getListOfAdmin() {

		List<User> allData = userRepo.getAllUsersByRole("Admin");
		return ResponseEntity.ok(allData);
	}

	// to get all data pagewise for admin
	@CrossOrigin(origins = { "http://72.167.32.189", "http://localhost:3000", "https://beautyblush.ca" })
	@GetMapping("/getAllList/{pageNumber}")
	public List<User> getListOfAdmin(@PathVariable int pageNumber) {
		int pageSize = 20;
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		Page<User> userList = userRepo.findByRoleIn(pageable, Arrays.asList("admin", "manager"));
		List<User> allData = userList.getContent();
		return allData;
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/getDataByUserId/{id}")
	public ResponseEntity<User> getListById(@PathVariable long id) {
		System.out.println("User data found1::: " + id);

		User user = userRepo.getDataById(id);
		System.out.println("User data found2::: " + user.toString());
		return ResponseEntity.ok(user);

	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PutMapping("/updateUser/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
		try {
			System.out.println("Check point true1");
			Optional<User> findById = userRepo.findById(id);
			System.out.println("Check point true2");
			User exsistingUser = findById.get();
			System.out.println("Check point true3");
			if (user.getRole().equals("Supplier")) {
//				System.out.println("Check point true4");
				if (findById.isPresent()) {
//					System.out.println("Check point true5");
//					List<User> findAll = userRepo.findAll();
//					List<String> exsistingEmails = new ArrayList<String>();
//					for (User allUser : findAll) {
//						exsistingEmails.add(allUser.getEmail());
//						exsistingEmails.remove(user.getEmail());
//					}
//					boolean remove = exsistingEmails.remove(user.getEmail());
//					System.out.println("check remove"+remove);
//					if (!exsistingEmails.contains(user.getEmail())) {
					System.out.println("Check point true6");
					user.setEmail(user.getEmail());
					user.setFirstName(user.getFirstName());
					user.setLastName(user.getLastName());
					user.setMobileNo(user.getMobileNo());
					user.setCreatedOn(exsistingUser.getCreatedOn());
					user.setOrganisationName(user.getOrganisationName());
					user.setAddress(user.getAddress());
					user.setEditedOn(System.currentTimeMillis());
					user.setRole(user.getRole());
					user.setId(exsistingUser.getId());
					userRepo.save(user);
					return new ResponseEntity<>("Supplier is updated sucessfully!", HttpStatus.OK);
//					} else {
//						return new ResponseEntity<>("Email already exsist!", HttpStatus.BAD_REQUEST);
//					}
				}

			} else if (user.getRole().equals("Admin") || user.getRole().equals("Manager")
					|| user.getRole().equals("SuperAdmin")) {
//					System.out.println("Check point true8");
//					List<User> findAll = userRepo.findAll();
//					System.out.println("Check point true9");
//					List<String> exsistingEmails = new ArrayList<String>();
//					for (User allUser : findAll) {
//						System.out.println("Check point true0");
//						exsistingEmails.add(allUser.getEmail());
//						exsistingEmails.remove(user.getEmail());
//					}
//					if (!exsistingEmails.contains(user.getEmail())) {
				if (findById.isPresent()) {
					System.out.println("Check point true");
					user.setEmail(user.getEmail());
					user.setFirstName(user.getFirstName());
					user.setLastName(user.getLastName());
					user.setMobileNo(user.getMobileNo());
					user.setEditedOn(System.currentTimeMillis());
					user.setStatus(user.getStatus());
					user.setCreatedOn(exsistingUser.getCreatedOn());
					user.setPassword(exsistingUser.getPassword());
					user.setRole(user.getRole());
					user.setId(exsistingUser.getId());
					userRepo.save(user);

					return new ResponseEntity<>(user.getRole() + " updated sucessfully", HttpStatus.OK);
//					} else {
//						return new ResponseEntity<>("Email already exsist!", HttpStatus.BAD_REQUEST);
//					}
				}
			}

		} catch (Exception e) {
			return new ResponseEntity<>("Email or Contact already exist!", HttpStatus.CONFLICT);
		}
		return null;

	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUser(String searchTerm) {
		String searchedData = searchTerm.trim();
		List<User> userList = userRepo.findBySearchTerm(searchedData);
		if (userList != null && !userList.isEmpty()) {
			return new ResponseEntity<>(userList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}

	}

	@CrossOrigin(origins = { "http://72.167.32.189", "http://localhost:3000", "https://beautyblush.ca" })
	@GetMapping("/listofallUsersOfAdmin/{mapAdminId}/{pageNumber}")
	public ResponseEntity<List<User>> ListOfSupplierAndMangerByAdminId(@PathVariable("mapAdminId") Long mapAdminId,
			@PathVariable int pageNumber) {
		int pageSize = 20;
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		Page<User> allData = userRepo.findBymapAdminId(mapAdminId, pageable);
		List<User> content = allData.getContent();
		return ResponseEntity.ok(content);
	}

	@CrossOrigin(origins = { "http://72.167.32.189", "http://localhost:3000", "https://beautyblush.ca" })
	@GetMapping("/listofSupplier/{mapAdminId}/{pageNumber}")
	public ResponseEntity<List<User>> ListOfSupplierByAdminId(@PathVariable("mapAdminId") Long mapAdminId,
			@PathVariable int pageNumber) {
		int pageSize = 20;
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		Page<User> allData = userRepo.findBymapAdminId(mapAdminId, pageable);
		List<User> list = allData.stream().filter(user -> "Supplier".equals(user.getRole()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(list);

	}

	// to count whole rows for pagination
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/count")
	public ResponseEntity<Integer> getNumberOfRowsForAdmin() {
		try {
			List<User> sortedDataOfUser = userRepo.findByRole("Admin");
			int rowCount = sortedDataOfUser.size();
			return ResponseEntity.ok(rowCount);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(0);
		}
	}

	// to count whole rows for pagination
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/countRowsForSupplier")
	public ResponseEntity<Integer> getNumberOfRowsForSupplier() {
		try {
			List<User> sortedDataOfUser = userRepo.findByRole("Supplier");
			int rowCount = sortedDataOfUser.size();
			return ResponseEntity.ok(rowCount);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(0);
		}
	}

	@CrossOrigin(origins = { "http://72.167.32.189", "http://localhost:3000", "https://beautyblush.ca" })
	@DeleteMapping("/deleteSupplierAndAdmin/{id}")
	public ResponseEntity<Long> deleteSupplierAndAdminById(@PathVariable Long id) {
		try {
			Optional<User> optionalSupplierAndAdmin = userRepo.findById(id);
			if (optionalSupplierAndAdmin.isPresent()) {
				userRepo.deleteById(id);
				return ResponseEntity.ok(id);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}

//	@CrossOrigin(origins = { "http://72.167.32.189", "http://localhost:3000", "https://beautyblush.ca" })
//	@DeleteMapping("/deleteUser/{id}")
//	public ResponseEntity<?> deleteUserById(@PathVariable long id) {
//		long uid = id;
//		if (!userRepo.existsById(id)) {
//			System.out.println("HIII");
//			return ResponseEntity.ok("User not found");
//		} else {
//			userRepo.deleteById(id);
//			return ResponseEntity.ok("Deleted   " + uid);
//		}
//
//	}

//@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
//@PutMapping("/update/{id}")
//public ResponseEntity<User> updateAll(@PathVariable("id") Long id, @RequestBody User newUser) {
//	
//	String firstName = newUser.getFirstName();
//	String lastName = newUser.getLastName();
//	String organisationName = newUser.getOrganisationName();
//	String username = newUser.getUsername();
//	
//	Optional<User> dataFromDb = userRepo.findById(id);
//	
//	User user1 = dataFromDb.get();
//	user1.setId(user1.getId());
//	user1.setEmail(user1.getEmail());
//	user1.setMobileNo(user1.getMobileNo());
//	String encode = passwordEncoder.encode(user1.getPassword());
//	user1.setPassword(encode);
//	newUser.setFirstName(firstName);
//	newUser.setUsername(username);
//	newUser.setLastName(lastName);
//	newUser.setPassword(newUser.getPassword());
//	newUser.setOrganisationName(organisationName);
//	newUser.setEditedOn(System.currentTimeMillis());
//	newUser.setRole(newUser.getRole());
//	newUser.setAddress(newUser.getAddress());
//	newUser.setFileId(newUser.getFileId());
//	User updateAll = userRepo.save(newUser);
//	return ResponseEntity.ok(updateAll);
//	
//}
