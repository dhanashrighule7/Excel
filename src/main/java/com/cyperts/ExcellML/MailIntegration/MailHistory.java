package com.cyperts.ExcellML.MailIntegration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class MailHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String productName;
	private String upc;
	private String quantity;
	private String firstName;
	private String organisationName;
	private String email;
	private String price;
	@Column(columnDefinition = "bigint default 0")
	private long createdOn;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@PrePersist
	protected void prePersistFunction() {
		this.createdOn = System.currentTimeMillis();
	}

	public MailHistory(long id, String productName, String upc, String quantity, String firstName,
			String organisationName, String email, long createdOn) {
		super();
		this.id = id;
		this.productName = productName;
		this.upc = upc;
		this.quantity = quantity;
		this.firstName = firstName;
		this.organisationName = organisationName;
		this.email = email;
		this.createdOn = createdOn;
	}

//	public MailHistory(String productName, String upc, String quantity, String firstName, String organisationName,
//			String email) {
//		this.productName = productName;
//		this.upc = upc;
//		this.quantity = quantity;
//		this.firstName = firstName;
//		this.organisationName = organisationName;
//		this.email = email;
//	}

	public MailHistory(String productName, String upc, String quantity, String firstName, String organisationName,
			String email, String price) {
		this.productName = productName;
		this.upc = upc;
		this.quantity = quantity;
		this.firstName = firstName;
		this.organisationName = organisationName;
		this.email = email;
		this.price = price;
	}

	@Override
	public String toString() {
		return "MailHistory [id=" + id + ", productName=" + productName + ", upc=" + upc + ", quantity=" + quantity
				+ ", firstName=" + firstName + ", organisationName=" + organisationName + ", email=" + email
				+ ", createdOn=" + createdOn + "]";
	}

	public MailHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

}
