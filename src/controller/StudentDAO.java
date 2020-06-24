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

		// mysql ����̹� �ε�
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.totalLoadList : DB ���� ����");
			} else {
				System.out.println("RootController.totalLoadList : DB ���� ����");
			}
			// con ��ü�� ������ ������ ���� ����(select, insert, update, delete)
			String query = "select * from gradeTBL";
			// ������ ���� ���� �غ� = mysql���� ���Ͽ��� ������ �� �Ͱ� ����
			pstmt = con.prepareStatement(query);// �� ������ �޾Ҵ�, �غ��ϰ� �־�~
			// �������� �����Ѵ�. = ���� ��ư�� ����
			rs = pstmt.executeQuery(); // workbench���� �������� �� ǥ������� ���ڵ尡 ���Դ� ���� ���� => ���ڵ������ �� �迭�����ο�
			// resultset ���� �Ѱ��� �����ͼ� arrayList�� �����Ѵ�.
			arrayList = new ArrayList<Student>();
			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), // ���ڵ��� ���� �� ������ 1���� �����ؼ� ���ʷ�
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15));
				arrayList.add(student);
			} // end of while

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("totalList ���˿��");
			alert.setHeaderText("totalList ���� �߻�");
			alert.setContentText("����" + e.getMessage());
			alert.showAndWait();
		} finally {
			// �ڿ� �ݳ�
			try {
				if (rs != null)// �ݳ��� ������ ����, ���� ���� ���� ���߿� ��������
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

	// ã�� ���, resultSet���� �� ����
	public ArrayList<Student> getStudentFind(String name) {
		// DB�� ����
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Student> arrayList = null;

		// mysql ����̹� �ε�
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.totalLoadList : DB ���� ����");
			} else {
				System.out.println("RootController.totalLoadList : DB ���� ����");
			}

			// con ��ü�� ������ ������ ���� ����(select, insert, update, delete)
			String query = "select * from gradeTBL where name like ? ";

			// ������ ���� ���� �غ� = mysql���� ���Ͽ��� ������ �� �Ͱ� ����
			pstmt = con.prepareStatement(query);// �� ������ �޾Ҵ�, �غ��ϰ� �־�~
			pstmt.setString(1, "%" + name + "%");

			// �������� �����Ѵ�. = ���� ��ư�� ����
			rs = pstmt.executeQuery(); // workbench���� �������� �� ǥ������� ���ڵ尡 ���Դ� ���� ���� => ���ڵ������ �� �迭�����ο�

			// resultset ���� �Ѱ��� �����ͼ� arrayList�� �����Ѵ�.
			arrayList = new ArrayList<Student>();
			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), // ���ڵ��� ���� �� ������ 1���� �����ؼ� ���ʷ�
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), // student��ü��
																												// ����
						rs.getString(11), rs.getString(12), rs.getString(13));
				arrayList.add(student);
			} // end of while

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("totalList ���˿��");
			alert.setHeaderText("totalList ���� �߻�");
			alert.setContentText("����" + e.getMessage());
			alert.showAndWait();
		} finally {

			// �ڿ� �ݳ�
			try {
				if (rs != null)// �ݳ��� ������ ����, ���� ���� ���� ���߿� ��������
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

	//��� ���
	public int getStudentRegistry(Student s) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		// mysql ����̹� �ε�
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnOkAction : DB ���� ����");
			} else {
				System.out.println("RootController.BtnOkAction : DB ���� ����");
			}
			// con ��ü�� ������ ������ ���� ����(select, insert, update, delete)
			String query = "insert into gradeTBL values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
			// ����ǥ��, ���࿡ ����� ���� ��ȣ�� ����������(?�� ��������� �ؿ��� pstmt.setint(?????���⼭ �������));
			// ������ ���� ���� �غ� = mysql���� ���Ͽ��� ������ �� �Ͱ� ����
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

			// �������� �����Ѵ�. = ���� ��ư�� ���� �������� �ʰ� �������� ������ ��(������� �Ȱ�������)�� pstmt.executeQuery()
			// �̰� ���� ���� executeUpdate(); ����ϸ� ��
			returnValue = pstmt.executeUpdate(); // ���๮ ����ŭ int ������ ������ 0�̸� error

			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
				alert.setTitle("���� ����");
				alert.setHeaderText(s.getName() + "�� ���� ���� ");
				alert.setContentText(s.getName() + "�� �ݰ�����~");
				alert.showAndWait();
			} else {
				throw new Exception("���Կ� ���� ����");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("���� ���˿��");
			alert.setHeaderText("���� ���� �߻� \n BtnOkAction");
			alert.setContentText("����" + e.getMessage());
			alert.showAndWait();
		} finally {
			// �ڿ� �ݳ�
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

	//���� ���
	public int getStudentDelete(int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		// mysql ����̹� �ε�
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnDeleteAction : DB ���� ����");
			} else {
				System.out.println("RootController.BtnDeleteAction : DB ���� ����");
			}
			// con ��ü�� ������ ������ ���� ����(select, insert, update, delete)
			String query = "delete from gradeTBL where no = ?";
															
			// ������ ���� ���� �غ� = mysql���� ���Ͽ��� ������ �� �Ͱ� ����
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, no);

			returnValue = pstmt.executeUpdate(); // ���๮ ����ŭ int ������ ������ 0�̸� error
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
				alert.setTitle("���� ����");
				alert.setHeaderText(no + " �� ��ȣ ���� ���� ");
				alert.setContentText(no + "�� �� �ȳ���������.");
				alert.showAndWait();
			} else {
				throw new Exception("���� ���� ����");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("���� ���� ���");
			alert.setHeaderText("���� ���� �߻� \n getStudentDelete");
			alert.setContentText("����" + e.getMessage());
			alert.showAndWait();
		} finally {
			// �ڿ� �ݳ�
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

	//���� ���
	public int getStudentUpdate(Student student) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		// mysql ����̹� �ε�
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnEditAction.btnFormAdd : DB ���� ����");
			} else {
				System.out.println("RootController.BtnEditAction.btnFormAdd : DB ���� ����");
			}
			// con ��ü�� ������ ������ ���� ����(select, insert, update, delete)
			String query = "update gradetbl set korean = ?, english = ?, math = ?, sic = ?, soc = ?, "
					+ "music = ?, total = ?, average = ? where no = ?"; 
			
			// ������ ���� ���� �غ� = mysql���� ���Ͽ��� ������ �� �Ͱ� ����
			pstmt = con.prepareStatement(query);
			
			// ?�� ������ ���������� �ش�� �������� no ��ȣ�� ����.
			pstmt.setString(1, student.getKorean());
			pstmt.setString(2, student.getEnglish());
			pstmt.setString(3, student.getMath());
			pstmt.setString(4, student.getSic());
			pstmt.setString(5, student.getSoc());
			pstmt.setString(6, student.getMusic());
			pstmt.setString(7, student.getTotal());
			pstmt.setString(8, student.getAvg());
			pstmt.setInt(9, student.getNo());

			returnValue = pstmt.executeUpdate(); // ���๮ ����ŭ int ������ ������ 0�̸� error
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
				alert.setTitle("���� ����");
				alert.setHeaderText(student.getNo() + "��ȣ ���� ���� ");
				alert.setContentText(student.getName() + "�� �ȳ�~");
				alert.showAndWait();
			} else {
				throw new Exception("���� ���� ����");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("���� ���˿��");
			alert.setHeaderText("���� ���� �߻� \n RootController.BtnEditAction.btnFormAdd");
			alert.setContentText("����" + e.getMessage());
			alert.showAndWait();
		} finally {
			// �ڿ� �ݳ�
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
