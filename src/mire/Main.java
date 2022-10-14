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
		// ¸Þ´º¼±ÅÃ
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
				System.out.println("1~7¹øÁß¿¡ ¼±ÅÃÇØÁÖ¼¼¿ä.");
				break;
			}

		} // end of while

		System.out.println("½Ã½ºÅÛ Á¾·á");

	}

	private static void statictStudentData() {

		List<Student> list = new ArrayList<Student>();

		try {
			System.out.print("ÃÖ°íÁ¡¼ö : 1 , ÃÖÀúÁ¡¼ö : 2 >> ");
			int type = sc.nextInt();

			boolean value = checkInputPattern(String.valueOf(type), 5);
			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			
			list = dbc.selectMaxMin(type);
			
			if (list.size() <= 0) {
				System.out.println("°Ë»öÇÑ ÇÐ»ýÁ¤º¸°¡ ¾ø½À´Ï´Ù." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù. ÀçÀÔ·Â¿äÃ»" + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅÍº£ÀÌ½º ÇÐ»ýÅë°è ¿¡·¯" + e.getMessage());
		}

	}

	// Á¤·Ä
	private static void sortStudentData() {
		List<Student> list = new ArrayList<Student>();

		try {
			DBConnection dbc = new DBConnection();
			dbc.connect();

			// ¼öÁ¤ÇÒ ÇÐ»ý ¹øÈ£ ÀÔ·Â
			System.out.print("Á¤·Ä¹æ½Ä¼±ÅÃ(1.no 2.name 3.total) >> ");
			int type = sc.nextInt();

			// ¹øÈ£ ÆÐÅÏ°Ë»ö
			boolean value = checkInputPattern(String.valueOf(type), 4);
			if (!value)
				return;

			list = dbc.selectOrderBy(type);

			if (list.size() <= 0) {
				System.out.println("º¸¿©ÁÙ list°¡ ¾ø½À´Ï´Ù." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅÍº£ÀÌ½º Á¤·Ä ¿¡·¯" + e.getMessage());
		}
		return;

	}

	// ¼öÁ¤
	public static void UpdateStudentData() {
		List<Student> list = new ArrayList<Student>();
		try {
			// ¼öÁ¤ÇÒ ÇÐ»ý ¹øÈ£ ÀÔ¤©·Â
			System.out.print("ÇÐ»ý ¹øÈ£ ÀÔ·Â >> ");
			String no = sc.nextLine();
			// ¹øÈ£ ÆÐÅÏ°Ë»ö
			boolean value = checkInputPattern(no, 1);
			if (!value)
				return;

			// ¹øÈ£·Î °Ë»öÇØ¼­ ºÒ·¯³»¾ßµÊ.
			DBConnection dbc = new DBConnection();
			// Database connection
			dbc.connect();
			// Entering article table data
			list = dbc.selectSearch(no, 1);

			if (list.size() <= 0) {
				System.out.println("ÀÔ·ÂµÈ Á¤º¸°¡ ¾ø½À´Ï´Ù.");
			}

			// ¸®½ºÆ® ³»¿ëÀ» º¸¿©ÁØ´Ù.
			for (Student student : list) {
				System.out.println(student);
			}

			// ¼öÁ¤ÇÒ ¸®½ºÆ®¸¦ º¸¿©Áà¾ß µÈ´Ù.
			Student imsiStudent = list.get(0);
			System.out.print("±¹¾î Á¡¼ö ÀÔ·Â >>");
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 3);
			if (!value)
				return;
			imsiStudent.setKor(kor);

			System.out.print("¿µ¾î Á¡¼ö ÀÔ·Â >>");
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 3);
			if (!value)
				return;
			imsiStudent.setEng(eng);

			System.out.print("¼öÇÐ Á¡¼ö ÀÔ·Â >>");
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 3);
			if (!value)
				return;
			imsiStudent.setMath(math);

			imsiStudent.calTotal();
			imsiStudent.calAvr();
			imsiStudent.calGrade();

			// µ¥ÀÌÅÍº£ÀÌ½º ¼öÁ¤ÇÒ ºÎºÐÀ» update ÁøÇà
			int returnUpdateValue = dbc.update(imsiStudent);
			if (returnUpdateValue == -1) {
				System.out.println("ÇÐ»ý ¼öÁ¤ Á¤º¸ ¾øÀ½");
				return;
			}
			System.out.println("ÇÐ»ý ¼öÁ¤ ¿Ï·áÇÏ¿´½À´Ï´Ù.");

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("ÀÔ·Â Å¸ÀÔ ¸ÂÁö ¾Ê¾î. ´Ù½Ã ÀÔ·ÂÇØ");
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅÍº£ÀÌ½º »èÁ¦ ¿¡·¯ . ´Ù½Ã ÀÔ·ÂÇØ");
			return;
		}
	}

	// °Ë»ö
	private static void searchStudentData() {
		List<Student> list = new ArrayList<Student>();

		try {
			System.out.print("°Ë»öÇÒ ÇÐ»ý ÀÌ¸§À» ÀÔ·ÂÇÏ¼¼¿ä : ");
			String name = sc.nextLine();

			boolean value = checkInputPattern(name, 2);
			if (!value) {
				return;
			}

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.selectSearch(name, 2);
			if (list.size() <= 0) {
				System.out.println("º¸¿©ÁÙ list°¡ ¾ø½À´Ï´Ù." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù. ÀçÀÔ·Â¿äÃ»" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅÍº£ÀÌ½º °Ë»ö ¿¡·¯" + e.getStackTrace());
		}
	}

	// Ãâ·Â
	private static void studentOutput() {
		List<Student> list = new ArrayList<Student>();
		try {

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.select();
			if (list.size() <= 0) {
				System.out.println("º¸¿©ÁÙ list°¡ ¾ø½À´Ï´Ù." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅÍº£ÀÌ½º Ãâ·Â ¿¡·¯" + e.getMessage());
		}
		return;
	}

	// »èÁ¦
	private static void deleteStudentData() {
		try {
			System.out.print("»èÁ¦ÇÒ ¹øÈ£¸¦ ÀÔ·ÂÇØÁÖ¼¼¿ä : ");
			String no = sc.nextLine();

			boolean value = checkInputPattern(no, 1);

			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			int insertReturnValue = dbc.delete(no);
			if (insertReturnValue == -1) {
				System.out.println("»èÁ¦½ÇÆÐÀÔ´Ï´Ù." + insertReturnValue);
			}
			if (insertReturnValue == 0) {
				System.out.println("»èÁ¦ÇÒ ¹øÈ£°¡ Á¸ÀçÇÏÁö ¾Ê½À´Ï´Ù." + insertReturnValue);
			} else {
				System.out.println("»èÁ¦¼º°øÀÔ´Ï´Ù. ¸®ÅÏ°ª = " + insertReturnValue);
			}

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù. ÀçÀÔ·Â¿äÃ»" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅÍº£ÀÌ½º »èÁ¦ ¿¡·¯" + e.getStackTrace());
		}
	}

	// ÀÔ·Â
	private static void studentInputData() {

		String pattern = null;
		boolean regex = false;

		try {

			System.out.print("ÇÐ³â(01,02,03)¹Ý(01~09)¹øÈ£(01~60) : ");
			String no = sc.nextLine();
			boolean value = checkInputPattern(no, 1);
			if (!value)
				return;

			System.out.print("ÀÌ¸§ÀÔ·Â : ");
			String name = sc.nextLine();
			value = checkInputPattern(name, 2);
			if (!value)
				return;

			System.out.print("korÀÔ·Â : ");
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 3);
			if (!value)
				return;

			System.out.print("engÀÔ·Â : ");
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 3);
			if (!value)
				return;

			System.out.print("mathÀÔ·Â : ");
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 3);
			if (!value)
				return;

			// µ¥ÀÌÅÍº£ÀÌ½º ÀÔ·Â
			Student student = new Student(no, name, kor, eng, math);
			student.calTotal();
			student.calAvr();
			student.calGrade();

			DBConnection dbc = new DBConnection();

			dbc.connect();

			int insertReturnValue = dbc.insert(student);

			if (insertReturnValue == -1) {
				System.out.println("»ðÀÔ½ÇÆÐÀÔ´Ï´Ù.");
			} else {
				System.out.println("»ðÀÔ¼º°øÀÔ´Ï´Ù. ¸®ÅÏ°ª=" + insertReturnValue);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("ÀÔ·ÂÅ¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù. ÀçÀÔ·Â¿äÃ»" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅÍº£ÀÌ½º ÀÔ·Â ¿¡·¯" + e.getStackTrace());
		} finally {
			sc.nextLine();
		}

	}

	// ¸Þ´º¼±ÅÃ
	public static int displayMenu() {
		int num = -1;

		try {
			System.out.print("1.ÀÔ·Â 2.¼öÁ¤ 3.»èÁ¦ 4.°Ë»ö 5.Ãâ·Â 6.Á¤·Ä 7.Åë°è 8.Á¾·á : ");
			num = sc.nextInt();

			String pattern = "^[1-8]$";
			boolean regex = Pattern.matches(pattern, String.valueOf(num));
		} catch (InputMismatchException e) {
			System.out.println();
			System.out.println("¼ýÀÚ·Î ÀÔ·ÂÇØÁÖ¼¼¿ä");
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
			message = "no ÀçÀÔ·Â¿ä¸Á";
			break;
		case 2:
			pattern = "^[°¡-ÆR]{2,4}$";
			message = "name ÀçÀÔ·Â¿ä¸Á";
			break;
		case 3:
			pattern = "^[0-9]{1,3}$";
			message = "score ÀçÀÔ·Â¿ä¸Á";

			break;
		case 4:
			pattern = "^[1-3]$";
			message = "Á¤·ÄÅ¸ÀÔ ÀçÀÔ·Â¿ä¸Á";
			break;
		case 5:
			pattern = "^[1-2]$";
			message = "Åë°èÅ¸ÀÔ ÀçÀÔ·Â¿ä¸Á";
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
