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
		// 메뉴선택
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
				System.out.println("1~7번중에 선택해주세요.");
				break;
			}

		} // end of while

		System.out.println("시스템 종료");

	}

	private static void statictStudentData() {

		List<Student> list = new ArrayList<Student>();

		try {
			System.out.print("최고점수 : 1 , 최저점수 : 2 >> ");
			int type = sc.nextInt();

			boolean value = checkInputPattern(String.valueOf(type), 5);
			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			
			list = dbc.selectMaxMin(type);
			
			if (list.size() <= 0) {
				System.out.println("검색한 학생정보가 없습니다." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("타입이 맞지 않습니다. 재입력요청" + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("데이터베이스 학생통계 에러" + e.getMessage());
		}

	}

	// 정렬
	private static void sortStudentData() {
		List<Student> list = new ArrayList<Student>();

		try {
			DBConnection dbc = new DBConnection();
			dbc.connect();

			// 수정할 학생 번호 입력
			System.out.print("정렬방식선택(1.no 2.name 3.total) >> ");
			int type = sc.nextInt();

			// 번호 패턴검색
			boolean value = checkInputPattern(String.valueOf(type), 4);
			if (!value)
				return;

			list = dbc.selectOrderBy(type);

			if (list.size() <= 0) {
				System.out.println("보여줄 list가 없습니다." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (Exception e) {
			System.out.println("데이터베이스 정렬 에러" + e.getMessage());
		}
		return;

	}

	// 수정
	public static void UpdateStudentData() {
		List<Student> list = new ArrayList<Student>();
		try {
			// 수정할 학생 번호 입ㄹ력
			System.out.print("학생 번호 입력 >> ");
			String no = sc.nextLine();
			// 번호 패턴검색
			boolean value = checkInputPattern(no, 1);
			if (!value)
				return;

			// 번호로 검색해서 불러내야됨.
			DBConnection dbc = new DBConnection();
			// Database connection
			dbc.connect();
			// Entering article table data
			list = dbc.selectSearch(no, 1);

			if (list.size() <= 0) {
				System.out.println("입력된 정보가 없습니다.");
			}

			// 리스트 내용을 보여준다.
			for (Student student : list) {
				System.out.println(student);
			}

			// 수정할 리스트를 보여줘야 된다.
			Student imsiStudent = list.get(0);
			System.out.print("국어 점수 입력 >>");
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 3);
			if (!value)
				return;
			imsiStudent.setKor(kor);

			System.out.print("영어 점수 입력 >>");
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 3);
			if (!value)
				return;
			imsiStudent.setEng(eng);

			System.out.print("수학 점수 입력 >>");
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 3);
			if (!value)
				return;
			imsiStudent.setMath(math);

			imsiStudent.calTotal();
			imsiStudent.calAvr();
			imsiStudent.calGrade();

			// 데이터베이스 수정할 부분을 update 진행
			int returnUpdateValue = dbc.update(imsiStudent);
			if (returnUpdateValue == -1) {
				System.out.println("학생 수정 정보 없음");
				return;
			}
			System.out.println("학생 수정 완료하였습니다.");

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("입력 타입 맞지 않어. 다시 입력해");
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("데이터베이스 삭제 에러 . 다시 입력해");
			return;
		}
	}

	// 검색
	private static void searchStudentData() {
		List<Student> list = new ArrayList<Student>();

		try {
			System.out.print("검색할 학생 이름을 입력하세요 : ");
			String name = sc.nextLine();

			boolean value = checkInputPattern(name, 2);
			if (!value) {
				return;
			}

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.selectSearch(name, 2);
			if (list.size() <= 0) {
				System.out.println("보여줄 list가 없습니다." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("타입이 맞지 않습니다. 재입력요청" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("데이터베이스 검색 에러" + e.getStackTrace());
		}
	}

	// 출력
	private static void studentOutput() {
		List<Student> list = new ArrayList<Student>();
		try {

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.select();
			if (list.size() <= 0) {
				System.out.println("보여줄 list가 없습니다." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (Exception e) {
			System.out.println("데이터베이스 출력 에러" + e.getMessage());
		}
		return;
	}

	// 삭제
	private static void deleteStudentData() {
		try {
			System.out.print("삭제할 번호를 입력해주세요 : ");
			String no = sc.nextLine();

			boolean value = checkInputPattern(no, 1);

			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			int insertReturnValue = dbc.delete(no);
			if (insertReturnValue == -1) {
				System.out.println("삭제실패입니다." + insertReturnValue);
			}
			if (insertReturnValue == 0) {
				System.out.println("삭제할 번호가 존재하지 않습니다." + insertReturnValue);
			} else {
				System.out.println("삭제성공입니다. 리턴값 = " + insertReturnValue);
			}

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("타입이 맞지 않습니다. 재입력요청" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("데이터베이스 삭제 에러" + e.getStackTrace());
		}
	}

	// 입력
	private static void studentInputData() {

		String pattern = null;
		boolean regex = false;

		try {

			System.out.print("학년(01,02,03)반(01~09)번호(01~60) : ");
			String no = sc.nextLine();
			boolean value = checkInputPattern(no, 1);
			if (!value)
				return;

			System.out.print("이름입력 : ");
			String name = sc.nextLine();
			value = checkInputPattern(name, 2);
			if (!value)
				return;

			System.out.print("kor입력 : ");
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 3);
			if (!value)
				return;

			System.out.print("eng입력 : ");
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 3);
			if (!value)
				return;

			System.out.print("math입력 : ");
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 3);
			if (!value)
				return;

			// 데이터베이스 입력
			Student student = new Student(no, name, kor, eng, math);
			student.calTotal();
			student.calAvr();
			student.calGrade();

			DBConnection dbc = new DBConnection();

			dbc.connect();

			int insertReturnValue = dbc.insert(student);

			if (insertReturnValue == -1) {
				System.out.println("삽입실패입니다.");
			} else {
				System.out.println("삽입성공입니다. 리턴값=" + insertReturnValue);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("입력타입이 맞지 않습니다. 재입력요청" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("데이터베이스 입력 에러" + e.getStackTrace());
		} finally {
			sc.nextLine();
		}

	}

	// 메뉴선택
	public static int displayMenu() {
		int num = -1;

		try {
			System.out.print("1.입력 2.수정 3.삭제 4.검색 5.출력 6.정렬 7.통계 8.종료 : ");
			num = sc.nextInt();

			String pattern = "^[1-8]$";
			boolean regex = Pattern.matches(pattern, String.valueOf(num));
		} catch (InputMismatchException e) {
			System.out.println();
			System.out.println("숫자로 입력해주세요");
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
			message = "no 재입력요망";
			break;
		case 2:
			pattern = "^[가-힣]{2,4}$";
			message = "name 재입력요망";
			break;
		case 3:
			pattern = "^[0-9]{1,3}$";
			message = "score 재입력요망";

			break;
		case 4:
			pattern = "^[1-3]$";
			message = "정렬타입 재입력요망";
			break;
		case 5:
			pattern = "^[1-2]$";
			message = "통계타입 재입력요망";
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
