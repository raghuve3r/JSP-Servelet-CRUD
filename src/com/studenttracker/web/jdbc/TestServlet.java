package com.studenttracker.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;



/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Define datasource/connection pool
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//setting up print writer
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		
		//connecting to database
		Connection myConn = null;
		Statement myState = null;
		ResultSet myRes = null;
		
		try{
			myConn = dataSource.getConnection();
			//sql statement
			String sql = "select * from student";
			myState = myConn.createStatement();
			
		
			//execute sql statement
			myRes = myState.executeQuery(sql);
		
			//process result
			while(myRes.next()){
				String email = myRes.getString("email");
				out.println(email);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
