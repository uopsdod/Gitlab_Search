//package com.amazonaws.lambda.demo.search;
//
//import java.io.IOException;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import com.amazonaws.lambda.demo.search.SearchService;
//
///**
// * test login 
// */
//@RunWith(MockitoJUnitRunner.class)
//public class LoginTest {
//
//    @Before
//    public void setUp() throws IOException {
//    	
//    }
//
//    @Test
//    public void loginSuccess() {
//        String username = System.getProperty("username");
//        String password = System.getProperty("password");
//        SearchService autoLoginService = new SearchService();
//    	boolean isLogined = autoLoginService.login(username, password);
//    	
//    	Assert.assertEquals(isLogined, true);
//    }
//    
//    @Test
//    public void loginFailure() {
//        String username = System.getProperty("username");
//        String password = "WrongPassword";
//        SearchService autoLoginService = new SearchService();
//    	boolean isLogined = autoLoginService.login(username, password);
//    	
//    	Assert.assertEquals(isLogined, false);
//    }
//    
//}
