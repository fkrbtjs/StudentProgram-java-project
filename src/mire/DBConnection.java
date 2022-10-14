package mire;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet rs = null;
	private int id = -1;

//	connection 
	public void connect() {

		Properties properties = new Properties();

		try {
			FileInputStream fis = new FileInputStream(
					"C:\\Users\\PC\\Desktop\\java_test\\studentProjectGit\\src\\mire\\db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException" + e.getStackTrace());
		} catch (IOException e) {
			System.out.println("Fproperties.load error" + e.getStackTrace());
		}

		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("Class.forname load error" + e.getStackTrace());
		} catch (SQLException e) {
			System.out.println("connection error" + e.getStackTrace());
		}
	}

//	insert statement 
	public int insert(Student student) {

		PreparedStatement ps = null;
		int insertReturnValue = -1;
		String insertQuery = "INSERT INTO student(no,name,kor,eng,math,total,avr,grade) " + "VALUES(?,?,?,?,?,?,?,?);";

		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, student.getNo());
			ps.setString(2, student.getName());
			ps.setInt(3, student.getKor());
			ps.setInt(4, student.getEng());
			ps.setInt(5, student.getMath());
			ps.setInt(6, student.getTotal());
			ps.setDouble(7, student.getAvr());
			ps.setString(8, student.getGrade());

			insertReturnValue = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println("insert 오류발생" + e.getStackTrace());
		}

		try {
			if (ps != null)
				ps.close();
		} catch (SQLException e) {
			System.out.println("PrepareStatement error" + e.getStackTrace());
		}

		return insertReturnValue;
	}

//	delete statement
	public int delete(String no) {
		PreparedStatement ps = null;
		int deleteReturnValue = -1;
		String deleteQuery = "DELETE FROM student WHERE no = ?";

		try {
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, no);
			deleteReturnValue = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println("delete 오류발생" + e.getStackTrace());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PrepareStatement error" + e.getStackTrace());
			}
		}
		return deleteReturnValue;
	}

//  select statement
	public List<Student> select() {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectQuery = "select * from student";

		try {
			ps = connection.prepareStatement(selectQuery);
			rs = ps.executeQuery(selectQuery);

			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			// rs.next() : 현재 커서에 있는 레코드 위치로 간다.
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");

				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PrepareStatement error" + e.getMessage());
			}
		}
		return list;
	}

//	search statement
	public List<Student> selectSearch(String data, int type) {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectSearchQuery = "select * from student where ";

		try {
			switch (type) {
			case 1:
				selectSearchQuery += "no like ? ";
				break;
			case 2:
				selectSearchQuery += "name like ? ";
				break;
			default:
				System.out.println("잘못된 입력타입 ");
				return list;
			}
			ps = connection.prepareStatement(selectSearchQuery);
			String namePattern = "%" + data + "%";
			ps.setString(1, namePattern);
			rs = ps.executeQuery();

			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			// rs.next() : 현재 커서에 있는 레코드 위치로 간다.
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");

				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}
		} catch (Exception e) {
			System.out.println("selectSearch 오류발생" + e.getMessage());
		}

		try {
			if (ps != null)
				ps.close();
		} catch (SQLException e) {
			System.out.println("PrepareStatement error" + e.getStackTrace());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PrepareStatement error" + e.getMessage());
			}
		}
		return list;
	}

//  update statement
	public int update(Student student) {

		PreparedStatement ps = null;
		int updateReturnValue = -1;
		String updateQuery = "UPDATE student SET kor = ?,eng =?,math = ?,total = ?,avr = ?,grade = ? WHERE no = ?";

		try {
			ps = connection.prepareStatement(updateQuery);

			ps.setInt(1, student.getKor());
			ps.setInt(2, student.getEng());
			ps.setInt(3, student.getMath());
			ps.setInt(4, student.getTotal());
			ps.setDouble(5, student.getAvr());
			ps.setString(6, student.getGrade());
			ps.setString(7, student.getNo());

			updateReturnValue = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println("update 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PrepareStatement error" + e.getMessage());
			}
		}
		return updateReturnValue;
	}

//	select order by statement
	public List<Student> selectOrderBy(int type) {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectOrderByQuery = "select * from student order by ";

		try {

			switch (type) {
			case 1:
				selectOrderByQuery += "no desc ";
				break;
			case 2:
				selectOrderByQuery += "name desc ";
				break;
			case 3:
				selectOrderByQuery += "total desc ";
				break;
			default:
				System.out.println("정렬타입 오류");
				return list;

			}

			ps = connection.prepareStatement(selectOrderByQuery);
			rs = ps.executeQuery();

			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			
			// rs.next() : 현재 커서에 있는 레코드 위치로 간다.
			int rank =0;
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				
				if(type ==3) {
					rate = ++rank;
				}
				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}
		} catch (Exception e) {
			System.out.println("select 정렬 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PrepareStatement error" + e.getMessage());
			}
		}
		return list;
	}

	//  select max min statement
	public List<Student> selectMaxMin(int type) {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectMaxMinQuery = "select * from student where total = ";

		try {

			switch (type) {
			case 1:
				selectMaxMinQuery += "(select max(total) from student)";
				break;
			case 2:
				selectMaxMinQuery += "(select min(total) from student)";
				break;
			default:
				System.out.println("통계타입 오류");
				return list;

			}

			ps = connection.prepareStatement(selectMaxMinQuery);
			rs = ps.executeQuery();

			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			
			// rs.next() : 현재 커서에 있는 레코드 위치로 간다.
			
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				
				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}
		} catch (Exception e) {
			System.out.println("타입이 맞지 않습니다." + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PrepareStatement error" + e.getMessage());
			}
		}
		return list;
	}
	
	// close statement
	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Connection close error" + e.getStackTrace());
		}
	}

	

}
