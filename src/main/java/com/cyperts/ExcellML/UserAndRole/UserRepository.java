package com.cyperts.ExcellML.UserAndRole;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsernameOrEmail(String username, String email);

	Object existsByUsername(String username);

	Object existsByEmail(String email);

	boolean existsById(long userId);

	User findByUsername(String username);

	User findUserByEmail(String email);

	List<User> getDataByRole(String role);

	User getDataById(long id);
//	User updateUser(User user);

	List<User> getAllUsersByRole(String role);

//	User getById(Long id);

	// void getDataByUserIdAndId(long id,long userId);

	@Query("SELECT u FROM User u " + "WHERE " + "CAST(u.id AS string) LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR u.firstName LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR CAST(u.fileId AS string) LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR u.username LIKE CONCAT('%', :searchTerm, '%') OR "
			+ "(DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%Y') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%y') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%d') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%M') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%b') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%M %e') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%e %M') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%b %e') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%e %b') = :searchTerm OR "
			+ "DATE_FORMAT(FROM_UNIXTIME(u.createdOn / 1000), '%b %e %Y') = :searchTerm) "
			+ "OR u.lastName LIKE CONCAT('%', :searchTerm, '%') " + "OR u.email LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR CAST(u.mobileNo AS string) LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR u.password LIKE CONCAT('%', :searchTerm, '%') " + "OR u.status LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR u.organisationName LIKE CONCAT('%', :searchTerm, '%') "
			+ "OR u.role LIKE CONCAT('%', :searchTerm, '%') " + "OR u.address LIKE CONCAT('%', :searchTerm, '%') ")
	List<User> findBySearchTerm(@Param("searchTerm") String searchTerm);

	Page<User> getAllUsersByRole(String role, Pageable pageable);

	Page<User> getAllSuppliersByRole(String role, Pageable pageable);

	Page<User> findBymapAdminId(Long mapAdminId, Pageable pageable);

	List<User> findByRole(String role);
	
	 Page<User> findByRoleIn(Pageable pageable, List<String> roles);

	//Page<User> getAllByRole(Pageable pageable);

}
