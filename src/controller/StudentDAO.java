package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import model.Student;

public class StudentDAO {

	public ArrayList<Student> getTotalLoadList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Student> arrayList = null;

		// mysql 드라이버 로드
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.totalLoadList : DB 연결 성공");
			} else {
				System.out.println("RootController.totalLoadList : DB 연결 실패");
			}
			// con 객체를 가지고 쿼리문 실행 가능(select, insert, update, delete)
			String query = "select * from gradeTBL";
			// 쿼리문 실행 위한 준비 = mysql에서 파일에서 쿼리문 쓴 것과 같음
			pstmt = con.prepareStatement(query);// 나 쿼리문 받았다, 준비하고 있어~
			// 쿼리문을 실행한다. = 번개 버튼과 같음
			rs = pstmt.executeQuery(); // workbench에서 실행했을 때 표모양으로 레코드가 나왔던 것이 나옴 => 레코드셋으로 옴 배열구조로옴
			// resultset 값을 한개씩 가져와서 arrayList에 저장한다.
			arrayList = new ArrayList<Student>();
			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), // 레코드의 값이 맨 왼쪽이 1부터 시작해서 차례로
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15));
				arrayList.add(student);
			} // end of while

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("totalList 점검요망");
			alert.setHeaderText("totalList 문제 발생");
			alert.setContentText("문제" + e.getMessage());
			alert.showAndWait();
		} finally {
			// 자원 반납
			try {
				if (rs != null)// 반납도 순서가 있음, 먼저 들어온 것은 나중에 나가야함
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				System.out.println("RootController.totalLoadList : " + e.getMessage());
			}
		}
		return arrayList;
	}

	// 찾기 기능, resultSet으로 값 리턴
	public ArrayList<Student> getStudentFind(String name) {
		// DB를 실행
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Student> arrayList = null;

		// mysql 드라이버 로드
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.totalLoadList : DB 연결 성공");
			} else {
				System.out.println("RootController.totalLoadList : DB 연결 실패");
			}

			// con 객체를 가지고 쿼리문 실행 가능(select, insert, update, delete)
			String query = "select * from gradeTBL where name like ? ";

			// 쿼리문 실행 위한 준비 = mysql에서 파일에서 쿼리문 쓴 것과 같음
			pstmt = con.prepareStatement(query);// 나 쿼리문 받았다, 준비하고 있어~
			pstmt.setString(1, "%" + name + "%");

			// 쿼리문을 실행한다. = 번개 버튼과 같음
			rs = pstmt.executeQuery(); // workbench에서 실행했을 때 표모양으로 레코드가 나왔던 것이 나옴 => 레코드셋으로 옴 배열구조로옴

			// resultset 값을 한개씩 가져와서 arrayList에 저장한다.
			arrayList = new ArrayList<Student>();
			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), // 레코드의 값이 맨 왼쪽이 1부터 시작해서 차례로
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), // student객체에
																												// 넣음
						rs.getString(11), rs.getString(12), rs.getString(13));
				arrayList.add(student);
			} // end of while

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("totalList 점검요망");
			alert.setHeaderText("totalList 문제 발생");
			alert.setContentText("문제" + e.getMessage());
			alert.showAndWait();
		} finally {

			// 자원 반납
			try {
				if (rs != null)// 반납도 순서가 있음, 먼저 들어온 것은 나중에 나가야함
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				System.out.println("RootController.BtnSearchAction : " + e.getMessage());
			}
		}
		return arrayList;
	}

	//등록 기능
	public int getStudentRegistry(Student s) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		// mysql 드라이버 로드
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnOkAction : DB 연결 성공");
			} else {
				System.out.println("RootController.BtnOkAction : DB 연결 실패");
			}
			// con 객체를 가지고 쿼리문 실행 가능(select, insert, update, delete)
			String query = "insert into gradeTBL values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
			// 물음표로, 만약에 지우고 싶은 번호가 여러가지로(?가 여러개라면 밑에서 pstmt.setint(?????여기서 해줘야함));
			// 쿼리문 실행 위한 준비 = mysql에서 파일에서 쿼리문 쓴 것과 같음
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, s.getName()); 
			pstmt.setString(2, s.getLevel());
			pstmt.setString(3, s.getBan());
			pstmt.setString(4, s.getGender());
			pstmt.setString(5, s.getKorean());
			pstmt.setString(6, s.getEnglish());
			pstmt.setString(7, s.getMath());
			pstmt.setString(8, s.getSic());
			pstmt.setString(9, s.getSoc());
			pstmt.setString(10, s.getMusic());
			pstmt.setString(11, s.getTotal());
			pstmt.setString(12, s.getAvg());
			pstmt.setString(13, s.getRegister());
			pstmt.setString(14, s.getFileName());

			// 쿼리문을 실행한다. = 번개 버튼과 같음 가져오지 않고 쿼리문만 실행할 때(결과값은 안가져오고)는 pstmt.executeQuery()
			// 이거 쓰지 말고 executeUpdate(); 사용하면 됨
			returnValue = pstmt.executeUpdate(); // 실행문 수만큼 int 값으로 돌려줌 0이면 error

			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
				alert.setTitle("삽입 성공");
				alert.setHeaderText(s.getName() + "님 삽입 성공 ");
				alert.setContentText(s.getName() + "님 반가워요~");
				alert.showAndWait();
			} else {
				throw new Exception("삽입에 문제 있음");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("삽입 점검요망");
			alert.setHeaderText("삽입 문제 발생 \n BtnOkAction");
			alert.setContentText("문제" + e.getMessage());
			alert.showAndWait();
		} finally {
			// 자원 반납
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("RootController.BtnOkAction : " + e.getMessage());
			}
		}
		return returnValue;
	}

	//삭제 기능
	public int getStudentDelete(int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		// mysql 드라이버 로드
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnDeleteAction : DB 연결 성공");
			} else {
				System.out.println("RootController.BtnDeleteAction : DB 연결 실패");
			}
			// con 객체를 가지고 쿼리문 실행 가능(select, insert, update, delete)
			String query = "delete from gradeTBL where no = ?";
															
			// 쿼리문 실행 위한 준비 = mysql에서 파일에서 쿼리문 쓴 것과 같음
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, no);

			returnValue = pstmt.executeUpdate(); // 실행문 수만큼 int 값으로 돌려줌 0이면 error
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
				alert.setTitle("삭제 성공");
				alert.setHeaderText(no + " 번 번호 삭제 성공 ");
				alert.setContentText(no + "번 님 안녕히가세요.");
				alert.showAndWait();
			} else {
				throw new Exception("삭제 문제 있음");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("삭제 점검 요망");
			alert.setHeaderText("삭제 문제 발생 \n getStudentDelete");
			alert.setContentText("문제" + e.getMessage());
			alert.showAndWait();
		} finally {
			// 자원 반납
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("RootController.BtnDeleteAction : " + e.getMessage());
			}
		}
		return returnValue;
	}

	//수정 기능
	public int getStudentUpdate(Student student) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		// mysql 드라이버 로드
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnEditAction.btnFormAdd : DB 연결 성공");
			} else {
				System.out.println("RootController.BtnEditAction.btnFormAdd : DB 연결 실패");
			}
			// con 객체를 가지고 쿼리문 실행 가능(select, insert, update, delete)
			String query = "update gradetbl set korean = ?, english = ?, math = ?, sic = ?, soc = ?, "
					+ "music = ?, total = ?, average = ? where no = ?"; 
			
			// 쿼리문 실행 위한 준비 = mysql에서 파일에서 쿼리문 쓴 것과 같음
			pstmt = con.prepareStatement(query);
			
			// ?에 무엇을 지워야할지 해당된 쿼리문에 no 번호를 연결.
			pstmt.setString(1, student.getKorean());
			pstmt.setString(2, student.getEnglish());
			pstmt.setString(3, student.getMath());
			pstmt.setString(4, student.getSic());
			pstmt.setString(5, student.getSoc());
			pstmt.setString(6, student.getMusic());
			pstmt.setString(7, student.getTotal());
			pstmt.setString(8, student.getAvg());
			pstmt.setInt(9, student.getNo());

			returnValue = pstmt.executeUpdate(); // 실행문 수만큼 int 값으로 돌려줌 0이면 error
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
				alert.setTitle("수정 성공");
				alert.setHeaderText(student.getNo() + "번호 수정 성공 ");
				alert.setContentText(student.getName() + "님 안녕~");
				alert.showAndWait();
			} else {
				throw new Exception("수정 문제 있음");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("수정 점검요망");
			alert.setHeaderText("수정 문제 발생 \n RootController.BtnEditAction.btnFormAdd");
			alert.setContentText("문제" + e.getMessage());
			alert.showAndWait();
		} finally {
			// 자원 반납
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e2) {
				System.out.println("RootController.BtnEditAction.btnFormAdd : " + e2.getMessage());
			}
		}
		return returnValue;
	}
}
