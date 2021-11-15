package modules;

import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {

		check("55515678", true);
		check("5551 5678", true);
		check("02 55515678", true);
		check("02 5551 5678", true);
		check("02 55 515 678", true);
		check("0255 515 678", true);
		check("(02) 5551 5678", true);
		check("(02)55515678", true);
		check("0255515678", true);
		check("+61255515678", true);
		check("+6125551 5678", true);
		check("+61 2 5551 5678", true);
		check("+61-2-5551-5678", true);
		check("+61.2.5551.5678", true);
		check("2 48 444 354", false);
		check("+6155515678", false);
		check("-61255515678", false);
		check("5551 567", false);
		check("(02) 551 5678", false);
		check("0 5551 5678", false);
		check("+61 2 55512 5678", false);
		check("04 51 5678", false);
		check("045 515 678", false);
		check("+61 4 5551 56789", false);
		check("+625551 56789", false);
		check ("48444354", true);
		check ("4844435", false);
		check ("4844 4354", true);
		check ("4844 435", false);
		check ("484 4355", false);
		check("48 444 354", true);
		check("444 354", false);
		check("2 444 354", false);
		check("+61 (0)438 412 066", true);
	}

	public static void check(String s, boolean expectedResult) {
		String prefix = "(((\\+\\d{2}[ -\\.]?(\\d|(\\(\\d\\)))[ -\\.]?)|(\\(0\\d\\)[ -\\.]?)|0\\d[ -\\.]?))?";
		String main = "((\\d{4}[ -\\.]?\\d{4})|(\\d{2}[ -\\.]?\\d{3}[ -\\.]?\\d{3}))";
		String pattern = prefix +  main;
		boolean result = Pattern.matches(pattern, s);
		if (result != expectedResult) {
			System.out.println(s + " returns " + result + " but expected " + expectedResult);
		}
	}
}
