package com.richasdy.springwebadmin.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Account implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private long id;

	@Column(unique = true, nullable = false)
	@Email // class validator
	@NotEmpty // class validator
	private String email;

	@Column(unique = true, nullable = false)
	@NotEmpty // validator
	private String phone;

	@Column(unique = true, nullable = false)
	@NotEmpty // validator
	private String username;

	@Column(nullable = false)
	@NotEmpty // validator
	private String password;

	private String note;

	private String permissions;

	private Boolean activated;

	private String activationCode;

	@Temporal(TemporalType.TIMESTAMP)
	private Date activatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	private String persistCode;

	private String resetPasswordCode;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;

	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Account(int id, String email, String phone, String username, String password, String note,
			String permissions, Boolean activated, String activationCode, Date activatedAt, Date lastLogin,
			String persistCode, String resetPasswordCode, Date createdAt, Date updatedAt, Date deletedAt) {
		super();
		this.id = id;
		this.email = email;
		this.phone = phone;
		this.username = username;
		this.password = password;
		this.note = note;
		this.permissions = permissions;
		this.activated = activated;
		this.activationCode = activationCode;
		this.activatedAt = activatedAt;
		this.lastLogin = lastLogin;
		this.persistCode = persistCode;
		this.resetPasswordCode = resetPasswordCode;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public Date getActivatedAt() {
		return activatedAt;
	}

	public void setActivatedAt(Date activatedAt) {
		this.activatedAt = activatedAt;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getPersistCode() {
		return persistCode;
	}

	public void setPersistCode(String persistCode) {
		this.persistCode = persistCode;
	}

	public String getResetPasswordCode() {
		return resetPasswordCode;
	}

	public void setResetPasswordCode(String resetPasswordCode) {
		this.resetPasswordCode = resetPasswordCode;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	@Override
	public String toString() {

		return this.getClass().getName() + " [ " + id + ", " + email + ", " + phone + ", " + username + ", " + password
				+ ", " + note + ", " + permissions + ", " + activationCode + ", " + persistCode + ", "
				+ resetPasswordCode + ", " + activated + ", " + activatedAt + ", " + lastLogin + ", " + createdAt + ", "
				+ updatedAt + ", " + deletedAt + " ]";
	}

}
