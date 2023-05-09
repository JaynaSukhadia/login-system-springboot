package com.aurosoft.loginsystem.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name= "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false)
	private int id;
	
	@Column(name="fname", nullable= false)
	private String fname;
	
	@Column(name="lname", nullable= false)
	private String lname;
	
	@Column(name="email", nullable = false)
	private String email;
	
	@Column(name="password", nullable =  false)
	private String password;
	
	@Column(name="role", nullable =  false)
	private String role;
	
	
	@Column(name="phone", nullable =  false)
	private String phone;
	
	
	@Column(name="vcode", nullable =  false)
	private String vcode;
	
	@Column(name="reg_date",nullable = false)
	@Temporal(TemporalType.DATE)
	private Date reg_date;
	
	
	
	
}
