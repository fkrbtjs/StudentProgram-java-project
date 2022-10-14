package mire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, UPDATE = 2, DELETE = 3, SEARCH = 4, OUTPUT = 5, SORT = 6, STATS = 7, EXIT = 8;

	public static void main(String[] args) {

		DBConnection dbc = new DBConnection();

		// Database connection
		dbc.connect();

		Map<String, Student> map = new HashMap<String, Student>();
		// �޴�����
		boolean flag = false;

		while (!flag) {

			int num = displayMenu();

			switch (num) {
			case INPUT:
				studentInputData();
				break;
			case UPDATE:
				UpdateStudentData();
				break;
			case DELETE:
				deleteStudentData();
				break;
			case SEARCH:
				searchStudentData();
				break;
			case OUTPUT:
				studentOutput();
				break;
			case SORT:
				sortStudentData();
				break;
			case STATS:
				statictStudentData();
				break;
			case EXIT:
				flag = true;
				break;
			default:
				System.out.println("1~7���߿� �������ּ���.");
				break;
			}

		} // end of while

		System.out.println("�ý��� ����");

	}

	private static void statictStudentData() {

		List<Student> list = new ArrayList<Student>();

		try {
			System.out.print("�ְ����� : 1 , �������� : 2 >> ");
			int type = sc.nextInt();

			boolean value = checkInputPattern(String.valueOf(type), 5);
			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			
			list = dbc.selectMaxMin(type);
			
			if (list.size() <= 0) {
				System.out.println("�˻��� �л������� �����ϴ�." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ�. ���Է¿�û" + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� �л���� ����" + e.getMessage());
		}

	}

	// ����
	private static void sortStudentData() {
		List<Student> list = new ArrayList<Student>();

		try {
			DBConnection dbc = new DBConnection();
			dbc.connect();

			// ������ �л� ��ȣ �Է�
			System.out.print("���Ĺ�ļ���(1.no 2.name 3.total) >> ");
			int type = sc.nextInt();

			// ��ȣ ���ϰ˻�
			boolean value = checkInputPattern(String.valueOf(type), 4);
			if (!value)
				return;

			list = dbc.selectOrderBy(type);

			if (list.size() <= 0) {
				System.out.println("������ list�� �����ϴ�." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (Exception e) {
			System.out.println("�����ͺ��̽� ���� ����" + e.getMessage());
		}
		return;

	}

	// ����
	public static void UpdateStudentData() {
		List<Student> list = new ArrayList<Student>();
		try {
			// ������ �л� ��ȣ �Ԥ���
			System.out.print("�л� ��ȣ �Է� >> ");
			String no = sc.nextLine();
			// ��ȣ ���ϰ˻�
			boolean value = checkInputPattern(no, 1);
			if (!value)
				return;

			// ��ȣ�� �˻��ؼ� �ҷ����ߵ�.
			DBConnection dbc = new DBConnection();
			// Database connection
			dbc.connect();
			// Entering article table data
			list = dbc.selectSearch(no, 1);

			if (list.size() <= 0) {
				System.out.println("�Էµ� ������ �����ϴ�.");
			}

			// ����Ʈ ������ �����ش�.
			for (Student student : list) {
				System.out.println(student);
			}

			// ������ ����Ʈ�� ������� �ȴ�.
			Student imsiStudent = list.get(0);
			System.out.print("���� ���� �Է� >>");
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 3);
			if (!value)
				return;
			imsiStudent.setKor(kor);

			System.out.print("���� ���� �Է� >>");
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 3);
			if (!value)
				return;
			imsiStudent.setEng(eng);

			System.out.print("���� ���� �Է� >>");
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 3);
			if (!value)
				return;
			imsiStudent.setMath(math);

			imsiStudent.calTotal();
			imsiStudent.calAvr();
			imsiStudent.calGrade();

			// �����ͺ��̽� ������ �κ��� update ����
			int returnUpdateValue = dbc.update(imsiStudent);
			if (returnUpdateValue == -1) {
				System.out.println("�л� ���� ���� ����");
				return;
			}
			System.out.println("�л� ���� �Ϸ��Ͽ����ϴ�.");

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("�Է� Ÿ�� ���� �ʾ�. �ٽ� �Է���");
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� ���� ���� . �ٽ� �Է���");
			return;
		}
	}

	// �˻�
	private static void searchStudentData() {
		List<Student> list = new ArrayList<Student>();

		try {
			System.out.print("�˻��� �л� �̸��� �Է��ϼ��� : ");
			String name = sc.nextLine();

			boolean value = checkInputPattern(name, 2);
			if (!value) {
				return;
			}

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.selectSearch(name, 2);
			if (list.size() <= 0) {
				System.out.println("������ list�� �����ϴ�." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ�. ���Է¿�û" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� �˻� ����" + e.getStackTrace());
		}
	}

	// ���
	private static void studentOutput() {
		List<Student> list = new ArrayList<Student>();
		try {

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.select();
			if (list.size() <= 0) {
				System.out.println("������ list�� �����ϴ�." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (Exception e) {
			System.out.println("�����ͺ��̽� ��� ����" + e.getMessage());
		}
		return;
	}

	// ����
	private static void deleteStudentData() {
		try {
			System.out.print("������ ��ȣ�� �Է����ּ��� : ");
			String no = sc.nextLine();

			boolean value = checkInputPattern(no, 1);

			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			int insertReturnValue = dbc.delete(no);
			if (insertReturnValue == -1) {
				System.out.println("���������Դϴ�." + insertReturnValue);
			}
			if (insertReturnValue == 0) {
				System.out.println("������ ��ȣ�� �������� �ʽ��ϴ�." + insertReturnValue);
			} else {
				System.out.println("���������Դϴ�. ���ϰ� = " + insertReturnValue);
			}

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ�. ���Է¿�û" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� ���� ����" + e.getStackTrace());
		}
	}

	// �Է�
	private static void studentInputData() {

		String pattern = null;
		boolean regex = false;

		try {

			System.out.print("�г�(01,02,03)��(01~09)��ȣ(01~60) : ");
			String no = sc.nextLine();
			boolean value = checkInputPattern(no, 1);
			if (!value)
				return;

			System.out.print("�̸��Է� : ");
			String name = sc.nextLine();
			value = checkInputPattern(name, 2);
			if (!value)
				return;

			System.out.print("kor�Է� : ");
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 3);
			if (!value)
				return;

			System.out.print("eng�Է� : ");
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 3);
			if (!value)
				return;

			System.out.print("math�Է� : ");
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 3);
			if (!value)
				return;

			// �����ͺ��̽� �Է�
			Student student = new Student(no, name, kor, eng, math);
			student.calTotal();
			student.calAvr();
			student.calGrade();

			DBConnection dbc = new DBConnection();

			dbc.connect();

			int insertReturnValue = dbc.insert(student);

			if (insertReturnValue == -1) {
				System.out.println("���Խ����Դϴ�.");
			} else {
				System.out.println("���Լ����Դϴ�. ���ϰ�=" + insertReturnValue);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("�Է�Ÿ���� ���� �ʽ��ϴ�. ���Է¿�û" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� �Է� ����" + e.getStackTrace());
		} finally {
			sc.nextLine();
		}

	}

	// �޴�����
	public static int displayMenu() {
		int num = -1;

		try {
			System.out.print("1.�Է� 2.���� 3.���� 4.�˻� 5.��� 6.���� 7.��� 8.���� : ");
			num = sc.nextInt();

			String pattern = "^[1-8]$";
			boolean regex = Pattern.matches(pattern, String.valueOf(num));
		} catch (InputMismatchException e) {
			System.out.println();
			System.out.println("���ڷ� �Է����ּ���");
			num = -1;
		} finally {
			sc.nextLine();
		}
		return num;
	}

	private static boolean checkInputPattern(String data, int patternType) {
		String pattern = null;
		boolean regex = false;
		String message = null;
		switch (patternType) {
		case 1:
			pattern = "^0[1-3]0[1-9][0-6][0-9]$";
			message = "no ���Է¿��";
			break;
		case 2:
			pattern = "^[��-�R]{2,4}$";
			message = "name ���Է¿��";
			break;
		case 3:
			pattern = "^[0-9]{1,3}$";
			message = "score ���Է¿��";

			break;
		case 4:
			pattern = "^[1-3]$";
			message = "����Ÿ�� ���Է¿��";
			break;
		case 5:
			pattern = "^[1-2]$";
			message = "���Ÿ�� ���Է¿��";
			break;
		}

		regex = Pattern.matches(pattern, data);
		if (patternType == 3) {
			if (Integer.parseInt(data) < 0 || Integer.parseInt(data) > 100) {
				System.out.println(message);
				return false;
			}
		} else {
			if (!regex) {
				System.out.println(message);
				return false;
			}
		}
		return regex;
	}

}
