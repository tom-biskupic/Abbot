package com.runcible.abbot.controllers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.runcible.abbot.model.User;
import com.runcible.abbot.service.UserService;
import com.runcible.abbot.service.exceptions.DuplicateUserException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/java/com/runcible/abbot/controllers/TestApplicationContext.xml")
public class UserControllerTest extends MvcTestWithJSON 
{
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        setupMockMVC();
        reset(userService);
        
        User[] users = new User[1];
        users[0] = user;
        testPage = new PageImpl<User>(Arrays.asList(users));
    }

    @Test 
    public void getUser() throws Exception
    {
    	when(userService.findByID(1)).thenReturn(user);
    	mockMvc.perform(get("/user.json/1"))
    		.andExpect(status().isOk())
    		.andExpect(content().contentType(contentType))
    		.andExpect(jsonPath("$.email",is(EMAIL)))
			.andExpect(jsonPath("$.firstName",is(FIRST_NAME)))
			.andExpect(jsonPath("$.lastName",is(LAST_NAME)))
			.andExpect(jsonPath("$.organisation",is(ORGANISATION)))
			.andExpect(jsonPath("$.administrator",is(false)));
    }
    
    @Test
    public void addUser() throws IOException, Exception
    {
    	ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    	mockMvc.perform(post("/user.json")
    			.content(convertObjectToJsonBytes(user))
    			.contentType(contentType))
    			.andExpect(status().isOk());

    	verify(userService).addUser(userCaptor.capture());
   	
    	checkUser(userCaptor.getValue());
    }

    @Test
    public void updateUser() throws IOException, Exception
    {
    	ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    	mockMvc.perform(post("/user.json")
    			.content(convertObjectToJsonBytes(userWithId))
    			.contentType(contentType))
    			.andExpect(status().isOk());

    	verify(userService).updateUser(userCaptor.capture());
   	
    	checkUser(userCaptor.getValue());
    }

    @Test
    public void duplicateUser() throws IOException, Exception
    {
    	doThrow(new DuplicateUserException()).when(userService).addUser(any(User.class));
    	
    	mockMvc.perform(post("/user.json")
    			.content(convertObjectToJsonBytes(user))
    			.contentType(contentType))
    			.andExpect(status().isOk())
        		.andExpect(content().contentType(contentType))
        		.andExpect(jsonPath("$.status",is("FAIL")))
        		.andExpect(jsonPath("$.errorMessageList[0].field",is("email")))
        		.andExpect(jsonPath("$.errorMessageList[0].defaultMessage",is("The email address specified is already registered")));
    }

    @Test
    public void testValidationEmail() throws IOException, Exception
    {
    	User user = new User("",PASSWORD,FIRST_NAME,LAST_NAME,ORGANISATION,IS_ADMIN);
    	testValidation(user, "email", "Email must be provided.");
    }
    
    @Test
    public void testValidationFirstName() throws IOException, Exception
    {
    	User user = new User(EMAIL,PASSWORD,"",LAST_NAME,ORGANISATION,IS_ADMIN);
    	testValidation(user, "firstName", "First Name must be provided.");
    }

    @Test
    public void testValidationLastName() throws IOException, Exception
    {
    	User user = new User(EMAIL,PASSWORD,FIRST_NAME,"",ORGANISATION,IS_ADMIN);
    	testValidation(user, "lastName", "Last Name must be provided.");
    }

    @Test
    public void testValidationPassword() throws IOException, Exception
    {
    	User user = new User(EMAIL,"",FIRST_NAME,LAST_NAME,ORGANISATION,IS_ADMIN);
    	testValidation(user, "password", "Password must be provided.");
    }
    
    @Test
    public void testGetPageableList() throws IOException, Exception
    {
    	when(userService.findAll(any(Pageable.class))).thenReturn(testPage);
    	mockMvc.perform(get("/userlist.json?page=1&size=3")
    			.contentType(contentType))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.totalPages",is(1)))
    			.andExpect(jsonPath("$.number",is(0)))
    			.andExpect(jsonPath("$.content[0].email",is(EMAIL)));
    }
    
	private void testValidation(User user, String fieldName, String message)
			throws Exception, IOException {
		mockMvc.perform(post("/user.json")
    			.content(convertObjectToJsonBytes(user))
    			.contentType(contentType))
    			.andExpect(status().isOk())
        		.andExpect(content().contentType(contentType))
        		.andExpect(jsonPath("$.status",is("FAIL")))
        		.andExpect(jsonPath("$.errorMessageList[0].field",is(fieldName)))
        		.andExpect(jsonPath("$.errorMessageList[0].defaultMessage",is(message)));
	}
    
	private void checkUser(User createdUser) 
	{
		assertEquals(EMAIL,createdUser.getEmail());
    	assertEquals(FIRST_NAME,createdUser.getFirstName());
    	assertEquals(LAST_NAME,createdUser.getLastName());
    	assertEquals(ORGANISATION,createdUser.getOrganisation());
    	assertEquals(IS_ADMIN,createdUser.isAdministrator());
	}

	@Autowired
    private UserService userService;
    
    private static final Integer ID=1234;
    private static final String FIRST_NAME="Fred";
    private static final String LAST_NAME="Bloggs";
    private static final String EMAIL="fred@nowhere.com";
    private static final String PASSWORD="Password01!";
    private static final String ORGANISATION="Nowhere Inc";
    private static final boolean IS_ADMIN=false;
    
    private User user = new User(EMAIL,PASSWORD,FIRST_NAME,LAST_NAME,ORGANISATION,IS_ADMIN);
    private User userWithId = new User(ID,EMAIL,PASSWORD,FIRST_NAME,LAST_NAME,ORGANISATION,IS_ADMIN);
    
    private Page<User> testPage;

}
