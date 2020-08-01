import java.util.Arrays;
import java.util.Scanner;

/**
 * RTS Labs Coding Challenge
 * @author Patrick Wamsley
 */
public class Problems {
	
	/* 
	 * Problem 1:
	 * 
	 * Print the number of integers in an array that are above the given input 
	 * and the number that are below, e.g. for the array 
	 * [1, 5, 2, 1, 10] with input 6, print “above: 1, below: 4”
	 */
	public static void printAboveAndBelowNum(int num, int[] array) {
		int above = 0;
		int below = 0;
		
		for (int i : array) {
			if (i < num) {
				below++;
			} else if (i > num) {
				above++;
			}
		}
		System.out.println("above: " + above + ", below: " + below);
	}
	
	/*
	 * Problem 2:
	 * 
	 * Rotate the characters in a string by a given input and have the overflow appear at the beginning, 
	 * e.g. “MyString” rotated by 2 is “ngMyStri”.
	 */
	public static String rotateString(String original, int amount) {
		if (original == null || original.isEmpty() || amount == 0) {
			return original;
		}
		
		int safeAmount = amount % original.length();
		int beginIndex = original.length() - safeAmount;
		return original.substring(beginIndex) + original.substring(0, beginIndex);
	}
	
	public static void main(String[] args) {
		System.out.println("---problem one---");
		testProblemOne();
		System.out.println("---problem two---");
		testProblemTwo();
	}
	
	public static void testProblemOne() {
		int[][] tests = {{1, 5, 2, 1, 10}, {}, {-2, -3, 5}, {120, -234134, 1234132, 13, 42, 830, -12, 19}, {-1}, {1, 1000, 90, 99, 24}};
		int[] nums = {6, 5, 0, 100, -2, 98};
		for (int i = 0; i < tests.length; i++) {
			System.out.println("Testing input: " + nums[i] + " on array: " + Arrays.toString(tests[i]));
			printAboveAndBelowNum(nums[i], tests[i]);
		}
	}
	
	public static void testProblemTwo() {
		String[] tests = {"MyString", "MyString", "MyString", "MyString", "MyString", "", null, "a", "thisStringIsLonger and has spaces"};
		for (int i = 0; i < tests.length; i++) {
			System.out.println("Testing rotateString with String: (" + tests[i] + ") and amount: " + i);
			System.out.println(rotateString(tests[i], i));
		}
	}
}
