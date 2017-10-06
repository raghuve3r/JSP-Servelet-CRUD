package com.studenttracker.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		//create studentDbUtil.. and pass in conn pool/datasource
		
		try{
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch(Exception ex){
			throw new ServletException(ex);
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			//read the "command"
			String theCommand = request.getParameter("command");
			
			//if null then just display
			if(theCommand == null){
				theCommand = "LIST";
			}
			
			//route to appropriate method
			switch(theCommand){
			case "LIST":
				listStudent(request, response);
				break;
			case "ADD":
				addStudent(request,response);
				break;
			case "LOAD":
				loadStudent(request,response);
				break;
			case "UPDATE":
				updateStudent(request,response);
				break;
			case "DELETE":
				deleteStudent(request,response);
				break;
			default:
				listStudent(request,response);
			}
			
		}
		catch(Exception ex){
			throw new ServletException(ex);
		}
	}


	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//read student id from form data
		String stuId = request.getParameter("studentId");
		
		//delete student from database
		studentDbUtil.deleteStudent(stuId);
		
		//send them back to "list students" page
		listStudent(request, response);
	}


	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//read student info from form
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create new student object
		Student stu = new Student(id,firstName,lastName,email);
		
		//perform update on db
		studentDbUtil.updateStudent(stu);
		
		//send them back to the list students page
		listStudent(request, response);
	}


	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//read student id from form
		String studentId = request.getParameter("studentId");
		
		//get student from database
		Student stu = studentDbUtil.getStudents(studentId);
		
		//place student in request attribute
		request.setAttribute("THE_STUDENT", stu);
		
		//send to jsp page: update-student-page.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}


	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		
		//create a new student object
		Student stu = new Student(firstName,lastName,email);
		
		//add the student to the database
		studentDbUtil.addStudent(stu);
		
		//send back to main page
		listStudent(request, response);
	}


	private void listStudent(HttpServletRequest request, HttpServletResponse response) 
	throws Exception{
		//get student from db util
		List<Student> students = studentDbUtil.getStudents();
		
		//add student to the request
		request.setAttribute("STUDENT_LIST", students);
		
		//send to JSP view (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

}
