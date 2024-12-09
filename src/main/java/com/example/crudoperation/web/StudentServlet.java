package com.example.crudoperation.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.crudoperation.bean.Student;
import com.example.crudoperation.dao.StudentDao;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       private StudentDao studentDao;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		studentDao = new StudentDao(); 
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String action = request.getServletPath();
		 switch(action)
		 {
		 case "/new":
			 showNewForm(request, response);
			 break;

	     case "/insert":
	    	 try {
				insertStudent(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     break;
	 
	     case "/delete":
	    	 try {
				deleteStudent(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         break;
	        
	     case "/edit":
	    	 try {
				showEditForm(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     break;
		     
	     case "/update":
	    	 try {
				updateStudent(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     break;
		     
		     default:
			try {
				listStudent(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     break;
 }
}
		 
		 private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		 {
			 RequestDispatcher dispatcher = request.getRequestDispatcher("student-form.jsp");
			 dispatcher.forward(request, response);
		 }
		 
		 //insert student
		 private void insertStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException
		 {
			 String name = request.getParameter("name");
			 String email = request.getParameter("email");
			 String department = request.getParameter("department");
			 Student newStudent = new Student(name, email, department);
			 studentDao.insertStudent(newStudent);
			 response.sendRedirect("list");
		 }
		 
		 //delete student
		 private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException
		 {
			 int id = Integer.parseInt(request.getParameter("id"));
			 try {
				 studentDao.deleteStudent(id);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
			 response.sendRedirect("list");
		 }
		 
		 //edit student
		 private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException
		 {
			 int id = Integer.parseInt(request.getParameter("id"));
			 Student existingStudent;
			 try {
				 existingStudent = studentDao.selectStudent(id);
				 RequestDispatcher dispatcher = request.getRequestDispatcher("student-form.jsp");
				 request.setAttribute("student", existingStudent);
				 dispatcher.forward(request, response);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }
		 
		 //update student
		 private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException
		 {
			 int id = Integer.parseInt(request.getParameter("id"));
			 String name = request.getParameter("name");
			 String email = request.getParameter("email");
			 String department = request.getParameter("department");
			 
			 Student student = new Student(id, name, email, department);
			 studentDao.updateStudent(student);
			 response.sendRedirect("list");
		 }
		 
		 //default
		 private void listStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException
		 {
			try {
				List<Student> listStudent = studentDao.selectAllStudents();
				request.setAttribute("listStudent", listStudent);
				RequestDispatcher dispatcher = request.getRequestDispatcher("student-list.jsp");
				dispatcher.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
			}
		 }
}
