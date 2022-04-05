package jee.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import jee.entity.Todo;
import jee.entity.User;
//doesnt want to be put on github :(
@Stateless
public class QueryService {
	@Inject
	EntityManager entityManager;
	@Inject
	SecurityUtil securityUtil;
	//we inject the security context that we override in SecurityFilter class
	//in the override method we sent the name to the email of the currently executing user gotten from the jwt
	//the email was set by the currently authenticated user
	@Context
	private SecurityContext securityContext;
	
	public User findUserByEmail(String email) {
		//we use email to get specific user
		//one way to find user from the context otherwise
		//return entityManager.find(User.class, email);
		//created query service tp find currently executing user to us
		
		//to avoid errors with empty lists with the get(0) we assign user list to a variable and check if its empty 
		//and if not return the user in the list otherwise return null
		List<User> users = entityManager.createNamedQuery(User.FIND_USER_BY_EMAIL, User.class).setParameter("email", email)
				.getResultList();
		if(!users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}
	
	public List countUserByEmail(String email) {
		//native query is a sql query so has different format
		//here we create a query to check if there are duplicate emails in the database that match the email paramter sent
		//then get each one and add it to a list thats returned
		return entityManager.createNativeQuery("select count (id) from TodoUser where exists (select id from TodoUser where email = ?)")
		.setParameter(1, email).getResultList();
		
	}
	
//	public boolean authenticateUser(String email, String password) {
//		User user = findUserByEmail(email);
//		if(user == null) {
//			return false;
//		}
//		//uses the security util passwordMatch method to hash the input password and check if it matches the users input password after being hashed
//		//the method takes the stored hashed password the salt and the newly input password 
//		return securityUtil.passwordMatch(user.getPassword(), user.getSalt(), password);
//		
//	}
	
	public Todo findTodoById(Long id) {
		//we are using getresultlist instead of getSingleResult to avoid the noResultEexception so that the jpa runtime doenst throw a no result exception
		//when there arnt results we simply get an empty list
		//here we set the paramaters for the query and check if it isnt empty we return the result
		//we are using the email that is set in the security context that is authorized
		List<Todo> resultList = entityManager.createNamedQuery(Todo.FIND_TODO_BY_ID, Todo.class)
								.setParameter("id", id)
								.setParameter("email", securityContext.getUserPrincipal().getName()).getResultList();
		if(!resultList.isEmpty()) {
			return resultList.get(0);
		}
		return null;
	}
	//query to get all the todos from a users email
	public List<Todo> getAllTodos(){
		return entityManager.createNamedQuery(Todo.FIND_ALL_TODOS_BY_USER, Todo.class)
				.setParameter("email", securityContext.getUserPrincipal().getName()).getResultList();

	}
	//get all the todoTasks for a user by email
	//the seach text could be in any part of a task text body so we use the % simbol before and after the input search text
	//to tell it that it should just search the head but anywhere in the todo task text body
	public List<Todo> getAllTodosByTask(String taskText){
		return entityManager.createNamedQuery(Todo.FIND_TODO_BY_TASK, Todo.class)
				.setParameter("email", securityContext.getUserPrincipal().getName())
				//this might take a task text so % the text can be within 
				.setParameter("task", "%" + taskText + "%").getResultList();
	}
}







