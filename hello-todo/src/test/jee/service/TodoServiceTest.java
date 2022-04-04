package jee.service;

import jee.entity.Todo;
import jee.entity.User;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;
//runwith tells framwork it will be managed by arquillian framework
@RunWith(Arquillian.class)
public class TodoServiceTest {
	
	@Inject
    private User user;

    @Inject
    TodoService todoService;
    
    Logger logger;
	
	//@Deployment only mandatory method with annotation
	//returns javaarchive arqui uses shrinkwrap api to create archive of war or jar of the project
	//we add the components of project we want to be added
	//arquillian will take package and deploy it for testing in pom we told arquillian to use payara embeded application server 
	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "hello-todo.war")
				//you can add different stuff you can also use .addClasses eg. addClasses(TodoService.class, Todo.class)
				//you can also use .addPackage to adda whole package
				.addPackage(Todo.class.getPackage())
				.addPackage(TodoService.class.getPackage())
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	@Before
	public void setUp() throws Exception {
//		logger = logger.getLogger(TodoServiceTest.class.getName());
//		user.setEmail("boobs@bouncy.com");
//		user.setFullName("JenniferBoink");
//		user.setPassword("FckA$$Nt1ts");
//		todoService.saveUser(user);
	}
	
	@After
	public void tearDown() throws Exception{		
	}
	//@Test allows to tell where to run the tests (the test methods)
//	@Test
//	public void createTodo() {		
//	}
//	
//	@Test
//	public void updateTodo() {		
//	}
//	
//	@Test
//	public void findTodoById() {
//	}
//	
	@Test
	public void saveUser() {
//		assertNotNull(user.getId());	
//		logger.log(Level.INFO, user.getId().toString());
//		
//		assertNotEquals("Test if user password is different from saved. ", "FckA$$Nt1ts", user.getPassword());
//		logger.log(Level.INFO, user.getPassword());
		
	}

}









