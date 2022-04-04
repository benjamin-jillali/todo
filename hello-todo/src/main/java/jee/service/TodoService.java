package jee.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import jee.entity.Todo;
import jee.entity.User;
//@transactional means each method will have a transaction for it
//@Transactional
//converted todoService tostateless ejb
@Stateless
public class TodoService {
	
	//created a context producer field
	@Inject
	private EntityManager entityManager;
	
	@Inject
	private QueryService queryService;
	
	@Inject
	private SecurityUtil securityUtil;
	//we need the SecurityContext to validate and get manage the user email
	@Context
	private SecurityContext securityContext;
	
	//persist user but we need to salt and hash the password
	public User saveUser(User user) {
		//we want to verify that the email the user is using is unique and doesnt already exist so we get the results from the email
		//checker query
		Integer count = (Integer) queryService.countUserByEmail(user.getEmail()).get(0);
		//if id doesnt exist and the number of emails that match the user input email is 0 we procede to save the user otherwise we do not
		if(user.getId() == null && count == 0) {
			//takers user plain passsword and creates hashed password as well as salt then puts it in a map
			Map<String, String> credMap = securityUtil.hashPassword(user.getPassword());
			//gets the hashed password from our map and puts it in user using the password map key
			user.setPassword(credMap.get(SecurityUtil.HASHED_PASSWORD_KEY));
			//same as the setPasseword except with the salt and salt key		
			user.setSalt(credMap.get(SecurityUtil.SALT_KEY));
			//now we persist the user to the database
			entityManager.persist(user);
			//clear the map
			credMap.clear();			
		}
		return user;
	}
	
	public Todo createTodo(Todo todo) {
		//Persist into db
		//we compare email to connect the user by email we get the email from the security context
		User userByEmail = queryService.findUserByEmail(securityContext.getUserPrincipal().getName());
		if(userByEmail != null) {
			todo.setTodoOwner(userByEmail);
			entityManager.persist(todo);
		}		
		return todo;
	}
	
	public Todo updateTodo(Todo todo) {
		entityManager.merge(todo);
		return todo;
	}
	public Todo findTodoById(Long id) {
		return queryService.findTodoById(id);
	}
	public List<Todo> getTodos(){ 
		return queryService.getAllTodos(); 
	}
}
