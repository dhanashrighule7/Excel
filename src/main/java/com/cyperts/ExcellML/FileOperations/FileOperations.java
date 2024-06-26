package com.cyperts.ExcellML.FileOperations;

import jakarta.persistence.Column;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class FileOperations {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long dataId;
	private String productName;
	private long upc;
	private String price;
	private String description;
	private String quantity;
	private String orderQty;

	// @JsonIgnore
	private long userId;
	private long availability;
	private long casepack;
	private String type;
	private String brand;
	private String designer;
	private String department;
	private String hyperLink;
	// @JsonIgnore
	@Column(columnDefinition = "bigint default 0")
	private long createdOn;
	// @JsonIgnore
	@Column(columnDefinition = "bigint default 0")
	private long editedOn;

	public long getDataId() {
		return dataId;
	}

	public void setDataId(long dataId) {
		this.dataId = dataId;
	}

	public long getUpc() {
		return upc;
	}

	public void setUpc(long upc) {
		this.upc = upc;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public long getAvailability() {
		return availability;
	}

	public void setAvailability(long availability) {
		this.availability = availability;
	}

	public long getCasepack() {
		return casepack;
	}

	public void setCasepack(long casepack) {
		this.casepack = casepack;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDesigner() {
		return designer;
	}

	public void setDesigner(String designer) {
		this.designer = designer;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHyperLink() {
		return hyperLink;
	}

	public void setHyperLink(String hyperLink) {
		this.hyperLink = hyperLink;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public FileOperations() {
		super();

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

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	@Override
	public String toString() {
		return "FileOperations [dataId=" + dataId + ", productName=" + productName + ", upc=" + upc + ", price=" + price
				+ ", description=" + description + ", quantity=" + quantity + ", orderQty=" + orderQty + ", userId="
				+ userId + ", availability=" + availability + ", casepack=" + casepack + ", type=" + type + ", brand="
				+ brand + ", designer=" + designer + ", department=" + department + ", hyperLink=" + hyperLink
				+ ", createdOn=" + createdOn + ", editedOn=" + editedOn + "]";
	}

	public FileOperations(long dataId, String productName, long upc, String price, String description, String quantity,
			String orderQty, long userId, long availability, long casepack, String type, String brand, String designer,
			String department, String hyperLink, long createdOn, long editedOn) {
		super();
		this.dataId = dataId;
		this.productName = productName;
		this.upc = upc;
		this.price = price;
		this.description = description;
		this.quantity = quantity;
		this.orderQty = orderQty;
		this.userId = userId;
		this.availability = availability;
		this.casepack = casepack;
		this.type = type;
		this.brand = brand;
		this.designer = designer;
		this.department = department;
		this.hyperLink = hyperLink;
		this.createdOn = createdOn;
		this.editedOn = editedOn;
	}

}
