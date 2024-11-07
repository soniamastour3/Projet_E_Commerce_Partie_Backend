package com.securityModel.modele;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securityModel.models.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(	name = "users",
		uniqueConstraints = {
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email")
		})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	private String image;



	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}



	@OneToOne(mappedBy= "user")
	@JsonIgnoreProperties("user")
	private WishList wishList;


	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	private boolean confirm=false;
	@Column(length = 64)
	private String confirmationToken;

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public String getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(String passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	private String passwordResetToken;

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public void setTokenExpiration(Instant tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}

	public Instant getTokenExpiration() {
		return tokenExpiration;
	}

	@Column
	private Instant tokenExpiration;

	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public WishList getWishList() {
		return wishList;
	}

	public void setWishList(WishList wishList) {
		this.wishList = wishList;
	}


	public User(Long id, String username, String email, String password, WishList wishList, List<Order> orders, Set<Role> roles, boolean confirm, String confirmationToken, String passwordResetToken, Instant tokenExpiration) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.wishList = wishList;

		this.roles = roles;
		this.confirm = confirm;
		this.confirmationToken = confirmationToken;
		this.passwordResetToken = passwordResetToken;
		this.tokenExpiration = tokenExpiration;
	}
}
