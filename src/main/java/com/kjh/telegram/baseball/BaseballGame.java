package com.kjh.telegram.baseball;

import java.util.Arrays;

public class BaseballGame {

	private static int[] com;
	private static int counter = 0;
	private static boolean isStart = false;
	private static long startTime;

	private BaseballGame(){
	}

	public static void startGame(int length) throws AlreadyStartException {
		if(isStart){
			throw new AlreadyStartException();
		}
		com = new int[length];
		boolean randomBall;

		do{
			randomBall = false;
			for (int i = 0; i < com.length; i++) {
				com[i] = (int)(Math.random() * 9) + 1;
			}
			for(int i = 0; i < com.length; i++){
				for (int j = 0; j < com.length - 1; j++) {
					if (com[i] == com[j] && i != j) {
						randomBall = true;
						break;
					}
				}
			}
		} while (randomBall);
		isStart = true;
		startTime = System.currentTimeMillis();
		System.out.print(Arrays.toString(com));
	}

	public static String verify(String message){
		int strike = 0;
		int ball = 0;

		char[] numArray = message.toCharArray();

		if(numArray[0] - '0' == 0){
			return "잘못 입력하셨습니다.";
		}
		for (int i = 0; i < numArray.length; i++) {
			for (int j = 0; j < numArray.length - 1; j++) {
				if(numArray[i] == numArray[j] && i != j){
					return "같은 숫자는 입력할 수 없습니다.";
				}
			}
		}
		for (int i = 0; i < com.length; i++) {
			for (int j = 0; j < numArray.length; j++) {
				int num = numArray[j] - '0';
				if (com[i] == num && i == j) {
					strike++;
				} else if (com[i] == num && i != j) {
					ball++;
				}
			}
		}
		if (strike == com.length){
			isStart = false;
			int tmp = counter;
			counter = 0;
			return "정답입니다. 횟수 : " + tmp;
		} else {
			counter++;
			return strike + "스트라이크 " + ball + "볼";
		}
	}

	public static int length(){
		return com.length;
	}

	public static boolean isStart(){
		return isStart;
	}

	public static void endGame(){
		isStart = false;
	}

	public static long getStartTime(){
		return startTime;
	}
}