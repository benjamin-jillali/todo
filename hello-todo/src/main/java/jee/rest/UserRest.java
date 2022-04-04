package jee.rest;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jee.entity.User;
import jee.service.SecurityUtil;
import jee.service.TodoService;

@Path("user")

public class UserRest {
	
	@Inject
	SecurityUtil securityUtil;
	//we inject the todop peristence service
	@Inject
	private TodoService todoService;
	
	
	//we need an interface about our application to get the uri of the application so we inject with @Context
	@Context
	private UriInfo uriInfo;
	
	//created web servuce that consumes form object that is passed email and password set to those values
	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@NotNull @FormParam("email") String email, @NotNull @FormParam("password") String password) {
		//authenticate user
		//generate token if succeds
		//return token to the client
		//checks if a user is authenticated true or false with the authenticateUser method in securityutils class
		//by giving it the password and email retrieved from a form
		boolean authenticated = securityUtil.authenticateUser(email, password);
		//if user isnt authenticated throw exception
		if(!authenticated) {
			throw new SecurityException("Email or password not valid");
		}
		//if user is authenticated we generate a token
		String token = generateToken(email);
		//when we pass the token to the client we want the header then the word "Bearer" then space then the token
		return Response.ok().header(HttpHeaders.AUTHORIZATION, securityUtil.BEARER + " " + token).build();		
	}
	
	private String generateToken(String email) {
		//we get the security key the same one that was used to sign in securityFilter that was generated in securityUtil init()
		Key securityKey = securityUtil.getSecurityKey();
		//we set the email of the currenty authenticated user the email is called in the SecurityFilter.filter method in the Override
		//we set the subject we set the issuer we set the audience the expiration to 15 minutes and set the signwith with the same algorithm we use 
		//to sing the key and the key
		return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setIssuer(uriInfo.getBaseUri().toString())
		.setAudience(uriInfo.getAbsolutePath().toString()).setExpiration(securityUtil
				.toDate(LocalDateTime.now().plusMinutes(15)))
		.signWith(SignatureAlgorithm.HS512, securityKey).compact();
	}
	//to save user puts the path at create and recieves a user then produces a json response after calling the persistence saveUer method
	@POST
	@Path("create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveUser(@NotNull User user) {
		todoService.saveUser(user);
		return Response.ok(user).build();
	}
	
}






