package com.studenttracker.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;
	public StudentDbUtil(DataSource theDataSource){
		dataSource = theDataSource;
	}
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myState = null;
		ResultSet myRes = null;
		
		try{
			//get connection
			myConn = dataSource.getConnection();
		
			//create sql query
			String sql = "select * from student order by last_name";
			
			myState = myConn.createStatement();
			
		
			//execute query
			myRes = myState.executeQuery(sql);
			
			//process result set
			while(myRes.next()){
				//retrieve data from result set row
				int id = myRes.getInt("id");
				String firstName = myRes.getString("first_name");
				String lastName = myRes.getString("last_name");
				String email = myRes.getString("email");
				
				//create new student object
				Student stu = new Student(id,firstName,lastName,email);
				
				//add it to student list
				students.add(stu);
			}
		
			//close JDBC
			
			return students;
		}
		finally{
			close(myConn, myState, myRes);
		}
	}
	private void close(Connection myConn, Statement myState, ResultSet myRes) {
		try{
			if(myRes != null){
				myRes.close();
			}
			if(myState != null){
				myState.close();
			}
			if(myConn != null){
				myConn.close();
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void addStudent(Student stu) throws Exception{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try{
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql insert
			String sql = "insert into student " +
						 " (first_name, last_name, email) " +
						 " value (?,?,?) ";
			
			myStmt = myConn.prepareStatement(sql);
			
			// set the param value for student
			myStmt.setString(1, stu.getFirstName());
			myStmt.setString(2, stu.getLastName());
			myStmt.setString(3, stu.getEmail());
			
			
			//execute sql insert
			myStmt.execute();
		}
		finally{
			//clean jdbc
			close(myConn, myStmt, null);
		}
	}
	public Student getStudents(String studentId) throws Exception{
		
		Student stu = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRes = null;
		
		int stuId;
		
		try{
			//convert student id to int
			stuId = Integer.parseInt(studentId);
			
			//get coonection tp db
			myConn = dataSource.getConnection();
			
			//create sql to get stu
			String sql = "select * from student where id=?";
			
			//create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, stuId);
			
			
			//execute statement
			myRes = myStmt.executeQuery();
			
			//retrieve result from result set
			if(myRes.next()){
				String firstName = myRes.getString("first_name");
				String lastName = myRes.getString("last_name");
				String email = myRes.getString("email");
				
				//use student id in constructor
				stu = new Student(stuId,firstName, lastName, email);
			}else{
				throw new Exception("Cannot find the student with student id: "+studentId);
			}
			
			return stu;
		}
		finally{
			close(myConn, myStmt, myRes);
		}
	}
	public void updateStudent(Student stu) throws Exception{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try{
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql  update stmt
			String sql = "update student "+
						 "set first_name=?, last_name=?, email=? "+
						 "where id=?";
		
			//prepare statement
			myStmt = myConn.prepareStatement(sql);
		
			//set params
			myStmt.setString(1, stu.getFirstName());
			myStmt.setString(2, stu.getLastName());
			myStmt.setString(3, stu.getEmail());
			myStmt.setInt(4, stu.getId());
		
			//execute sql stmt
			myStmt.execute();
		}
		finally{
			close(myConn, myStmt, null);
		}
	}
	public void deleteStudent(String stuId) throws Exception{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try{
			//convert student id to int
			int studentId = Integer.parseInt(stuId);
			
			//connection to db
			myConn = dataSource.getConnection();
			
			//sql to delete
			String sql = "delete from student where id=?";
			
			//prepare the stmt
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute sql
			myStmt.execute();
		}
		finally{
			close(myConn, myStmt, null);
		}
	}
}
