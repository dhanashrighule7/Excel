package com.cyperts.ExcellML.UserAndRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String firstName;
	private String username;
	private String lastName;

	private Long mobileNo;
	@Column(unique=true) 
	private String email;
	private String password;
	private String role;
	private long fileId;
	private String address;
	private String organisationName;

	private String status;
	@Column(columnDefinition = "bigint default 0")
	private long createdOn;
	@Column(columnDefinition = "bigint default 0")
	private long editedOn;
	
	private long mapAdminId;
	
	
    
	public long getMapAdminId() {
		return mapAdminId;
	}

	public void setMapAdminId(long mapAdminId) {
		this.mapAdminId = mapAdminId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public long getEditedOn() {
		return editedOn;
	}

	public void setEditedOn(long editedOn) {
		this.editedOn = editedOn;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@PrePersist
	protected void prePersistFunction() {
		this.createdOn = System.currentTimeMillis();
		this.editedOn = System.currentTimeMillis();
	}

	@PreUpdate
	protected void preUpdateFunction() {
		this.editedOn = System.currentTimeMillis();
	}

	

	public User(long id, String firstName, String username, String lastName, Long mobileNo, String email,
			String password, String role, long fileId, String address, String organisationName, String status,
			long createdOn, long editedOn, long mapAdminId) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.username = username;
		this.lastName = lastName;
		this.mobileNo = mobileNo;
		this.email = email;
		this.password = password;
		this.role = role;
		this.fileId = fileId;
		this.address = address;
		this.organisationName = organisationName;
		this.status = status;
		this.createdOn = createdOn;
		this.editedOn = editedOn;
		this.mapAdminId = mapAdminId;
	}

	public User() {
		super();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", username=" + username + ", lastName=" + lastName
				+ ", mobileNo=" + mobileNo + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", fileId=" + fileId + ", address=" + address + ", organisationName=" + organisationName + ", status="
				+ status + ", createdOn=" + createdOn + ", editedOn=" + editedOn + ", mapAdminId=" + mapAdminId + "]";
	}

	

}
