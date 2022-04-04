package jee.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
//sql has user table so we need to give User a table name thats different
@Table(name = "TodoUser")
@NamedQuery(name = User.FIND_ALL_USERS, query = "select u from User u order by u.fullName")
@NamedQuery(name = User.FIND_USER_BY_EMAIL, query = "select u from User u where u.email = :email")
@NamedQuery(name = User.FIND_USER_BY_PASSWORD, query = "select u from User u where u.password = :password")
public class User extends AbstractEntity{
	
	public static final String FIND_ALL_USERS = "User.findAllUsers";
	public static final String FIND_USER_BY_EMAIL = "User.findByEmail";
	public static final String FIND_USER_BY_PASSWORD = "User.findUserByPassword";
	
	//@NotNull(message = "Full name must be set.")
//Todo	@Pattern(regexp = "", message = "Full name must be in letters")	
	private String fullName;
	@NotNull(message = "Email must be set")
	@Email(message = "Email must be in the form user@provider.com")
	private String email;
	@NotNull(message = "Password cannot be empty")
	@Size(min = 8, message = "password must be at least 8 characters")
//Todo	
//	@Pattern(regexp = "", message = "Password must contain symbols uppercase letters lowercase and numbers.")
	private String password; //$%*Ll
	
	private String salt;
	
	
	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	//this is a valid way but not the most perofmant
	//its more efficient to just use the manyToOne relation so query from the owning side to the owned side
//	@OneToMany
//	private final Collection<Todo> todos = new ArrayList<Todo>();
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
