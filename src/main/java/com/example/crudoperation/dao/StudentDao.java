package com.example.crudoperation.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.crudoperation.bean.Student;

public class StudentDao {
	private String jdbcURL = "jdbc:mysql://localhost:3306/studentdb?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "Sakshi";
	private String jdbcDriver = "com.mysql.jdbc.Driver";

	private static final String INSERT_STUDENTS_SQL = "INSERT INTO students" + " (name, email, department) VALUES " + " (?, ?, ?); ";
	
	private static final String SELECT_STUDENT_BY_ID = "select id,name,email,department from students where id=?";
	private static final String SELECT_ALL_STUDENTS = "select *from students";
	private static final String UPDATE_STUDENTS_SQL = "update students set name = ?,email = ?,department = ? where id = ?;";
	private static final String DELETE_STUDENTS_SQL = "delete from students where id = ?;";
	
	public StudentDao() {
		
	}
	
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	//insert Student
	public void insertStudent(Student student) throws SQLException{
		System.out.println(INSERT_STUDENTS_SQL);
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENTS_SQL)){
			preparedStatement.setString(1, student.getName());
			preparedStatement.setString(2, student.getEmail());
			preparedStatement.setString(3, student.getDepartment());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch(SQLException e) {
			printSQLException(e);
		}
	}
	
	//select student by id
	public Student selectStudent(int id) {
		Student student = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_BY_ID);){
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String department = rs.getString("department");
				student = new Student(id, name, email, department);
			}
		}catch (SQLException e) {
			printSQLException(e);
		}
		return student;
	}
	//select all students
	public List<Student> selectAllStudents(){
		List<Student> students = new ArrayList<>();
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STUDENTS);) {
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String department = rs.getString("department");
				
				students.add(new Student(id, name, email, department));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return students;
	}
	
	//update student
	public boolean updateStudent(Student student) throws SQLException {
		boolean rowUpdated;
		try(Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENTS_SQL);) {
			System.out.println("updated Student:" + statement);
			statement.setString(1, student.getName());
			statement.setString(2, student.getEmail());
			statement.setString(3, student.getDepartment());
			statement.setInt(4,  student.getId());
			
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	
	//delete student
	public boolean deleteStudent(int id) throws SQLException {
		boolean rowDeleted;
		try(Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_STUDENTS_SQL);) {
			statement.setInt(1,  id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	
	private void printSQLException(SQLException ex) {
		for(Throwable e : ex) {
			if(e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState:" + ((SQLException) e).getSQLState());
				System.err.println("Error Code:" + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while(t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
	
}
