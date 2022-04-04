package jee.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jee.entity.Todo;
import jee.service.QueryService;
import jee.service.TodoService;

@Path("todo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authz
public class TodoRest {
	@Inject
	TodoService todoService;
	@Inject
	QueryService queryService;
	
	@Path("new")
	@POST
	public Response createTodo(Todo todo) {
		//api/v1/todo/new
		todoService.createTodo(todo);
		return Response.ok(todo).build();
	}
	
//	@Path("update")
//	@PUT
//	public Response updateTodo(Todo todo) {
//		todoService.updateTodo(todo);
//		return Response.ok(todo).build();
//	}
	@Path("{id}")
	@GET
	public Todo getTodo(@PathParam("id") Long id) {
		return queryService.findTodoById(id);
	}
	@Path("list")
	@GET
	public List<Todo> getTodos(){
		return queryService.getAllTodos();
	}
	@Path("status")
	@POST
	public Response markAsComplete(@QueryParam("id") long id) {
		Todo todo = todoService.findTodoById(id);
		todo.setCompleted(true);
		todoService.updateTodo(todo);
		return Response.ok(todo).build();
	}
}
