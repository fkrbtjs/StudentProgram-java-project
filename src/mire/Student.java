package mire;

import java.io.Serializable;

public class Student implements Comparable<Student>, Serializable {

	// 필드
	private static final int SUBJECT = 3;
	private String no;
	private String name;
	private int kor;
	private int eng;
	private int math;
	private int total;
	private double avr;
	private String grade;
	private int rate;

	// 생성자
	public Student(String no, String name, int kor, int eng, int math) {
		this(no,name,kor,eng,math,0,0.0,null,0);
	}
	
	public Student(String no, String name, int kor, int eng, int math, int total, double avr, String grade, int rate) {
		super();
		this.no = no;
		this.name = name;
		this.kor = kor;
		this.eng = eng;
		this.math = math;
		this.total = total;
		this.avr = avr;
		this.grade = grade;
		this.rate = rate;
	}





	// 메소드
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKor() {
		return kor;
	}

	public void setKor(int kor) {
		this.kor = kor;
	}

	public int getEng() {
		return eng;
	}

	public void setEng(int eng) {
		this.eng = eng;
	}

	public int getMath() {
		return math;
	}

	public void setMath(int math) {
		this.math = math;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public double getAvr() {
		return avr;
	}

	public void setAvr(double avr) {
		this.avr = avr;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public void calTotal() {
		this.total = this.kor + this.eng + this.math;
	}

	public void calAvr() {
		this.avr = Double.parseDouble(String.format("%.2f", (this.total / (double) SUBJECT))) ;
	}

	public void calGrade() {
		switch ((int) (this.avr) / 10) {
		case 9:
			this.grade = "A";
			break;
		case 8:
			this.grade = "B";
			break;
		case 7:
			this.grade = "C";
			break;
		case 6:
			this.grade = "D";
			break;
		default:
			this.grade = "F";
			break;

		}

	}

	@Override
	public int hashCode() {

		return this.no.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Student)) {
			return false;
		}
		return this.no.equals(((Student) obj).no);
	}

	// 메소드 : 오버라이드(hascode,equals,compareTo,toString)
	@Override
	public int compareTo(Student student) {
		return this.no.compareToIgnoreCase(student.no);
	}

	@Override
	public String toString() {
		System.out.println("번호\t이름\t국어\t영어\t수학\t총합\t평균\t등급\t등수");
		return no + "\t" + name + "\t" + kor + "\t" + eng + "\t" + math + "\t" + total + "\t" + avr + "\t"
				+ grade + "\t" + rate;
	}

}
