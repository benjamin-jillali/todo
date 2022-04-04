package jee.entity;

import java.time.LocalDate;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedQuery(name = Todo.FIND_TODO_BY_TASK, query = "select t from Todo t where t.task like :task and t.todoOwner.email = :email")
@NamedQuery(name = Todo.FIND_ALL_TODOS_BY_USER, query = "select t from Todo t where t.todoOwner.email = :email")
@NamedQuery(name = Todo.FIND_TODO_BY_ID, query = "select t from Todo t where t.id = :id and t.todoOwner.email  = :email")

@Entity
public class Todo extends AbstractEntity{
	
	public static final String FIND_TODO_BY_TASK = "Todo.findByTask";
	public static final String FIND_ALL_TODOS_BY_USER = "Todo.findByUser";
	public static final String FIND_TODO_BY_ID = "Todo.findById";
	
	@NotEmpty(message = "Task must be set.")
	@Size(min = 10, message = "Task should be at least 10 characters.")
	private String task;
	@NotNull(message = "date must be set")
	@FutureOrPresent(message = "this date must be in present or future")
	@JsonbDateFormat(value = "yyyy-MM-dd")
	private LocalDate dueDate;
	private boolean isCompleted;
	private LocalDate dateCompleted;
	private LocalDate dateCreated;
	
	@ManyToOne
	private User todoOwner;
	
	@PrePersist	
	private void init() {
		setDateCreated(LocalDate.now());
	}
	
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public boolean isCompleted() {
		return isCompleted;
	}
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public LocalDate getDateCompleted() {
		return dateCompleted;
	}
	public void setDateCompleted(LocalDate dateCompleted) {
		this.dateCompleted = dateCompleted;
	}
	public LocalDate getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the todoOwner
	 */
	public User getTodoOwner() {
		return todoOwner;
	}

	/**
	 * @param todoOwner the todoOwner to set
	 */
	public void setTodoOwner(User todoOwner) {
		this.todoOwner = todoOwner;
	}
	
	

}
