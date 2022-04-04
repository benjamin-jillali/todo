package jee.rest;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jee.service.SecurityUtil;
//we will use the jjwt library in the pom to do this
//we want to intercept requests that are annotatted with the @Authz using this implentation 
//we will implement contauer request filter
//filter method intercepts and is called before request is dispatched  to a resource
//bound to Authz annotation
//simply whichever resource we annotate with Authz will cause this filter to run
@Authz
//provider registers containerrequestfilter implementation as a 
//jaxrs componenet the jaxrs runtime at startup will scan all the componenets annotated with @Provider
@Provider
//@priority means we are setting it at a higher priority we want the filte to run before other filters
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
//we will use the filter to implement the json web token
	
	@Inject
	private SecurityUtil securityUtil;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// grab the json web token from the header the AUTHORIZATION constant
		//Throw exception with a message if there is no token
		//Parse the token
		//if parsing succeeds, proceed
		//otherwise throw exception with method
		//here we look for authorization string
		//wrong header if it is null doesnt containg text or doesnt start with bearer
		//we throw not authorized exception
		String authString = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if(authString == null || authString.isEmpty() || !authString.startsWith(securityUtil.BEARER)) {
			//Better format this exception
			throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		//this gets the jwt to get the token
		String token = authString.substring(securityUtil.BEARER.length()).trim();
		//we put in try catch because the library if theres a problem with parsing the method will throw an exception
		try {
			//they key we sign the token is the same key we need when we are passing the token so we need to make the key consistently
			//which we create in the SecurityUtil class since its @Application scoped
			Key key = securityUtil.getSecurityKey(); // request the security key
			//we pass the setsigning key and will give us a claims object that will give us information about the claims
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			//we first grab the original security context interface 
			SecurityContext originalContext = requestContext.getSecurityContext();
			//we override the securitycontext
			//setting security context with the email as the name
			//we create new sec context where we override getuserprinicipal object and need to create new prinipal
			requestContext.setSecurityContext(new SecurityContext() {
				
				@Override
				public Principal getUserPrincipal() {
					// TODO Auto-generated method stub
					return new Principal() {
						//were overiding the principal object
						//setting the name as the email of the currently executing user
						@Override
						public String getName() {
							//we get the past jwt and get the subject
							return claimsJws.getBody().getSubject();
						}
					};
				}
				
				@Override
				public boolean isUserInRole(String role) {
					// TODO Auto-generated method stub
					return originalContext.isUserInRole(role);
					
				}
				
				@Override
				public boolean isSecure() {
					// TODO Auto-generated method stub
					return originalContext.isSecure();
				}				
				
				
				@Override
				public String getAuthenticationScheme() {
					// TODO Auto-generated method stub
					return originalContext.getAuthenticationScheme();
				}
			});
			
		}catch (Exception e) {
			//throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
			//more jaxrs way for exception handling is to abort the request
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		
	}
	

}




