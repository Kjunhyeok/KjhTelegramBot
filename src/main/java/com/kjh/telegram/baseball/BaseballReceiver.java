package com.kjh.telegram.baseball;

import java.util.regex.Pattern;

public class BaseballReceiver {

    public String check(String message){
        Pattern pattern = Pattern.compile("^[0-9]*$");
       if(message.contains("시작")){
            try {
                int length = play(message);
                return "길이 " + length + "인 숫자야구를 시작합니다.";
            } catch (NumberFormatException e){
                return "길이를 정확히 입력해 주세요.";
            } catch (NumberLengthException e){
                return "길이는 3이상이어야 합니다.";
            } catch (AlreadyStartException e){
                return "이미 숫자야구가 시작 중입니다.";
            }
        } else if(message.contains("종료")){
            return stop();
        } else if(pattern.matcher(message).find()) {
            return verify(message);
        } else {
            return help();
        }
    }

    private String help(){
        return "숫자야구 하는법\n /숫자야구시작+숫자를 입력하면 됨\nex) /숫자야구시작4";
    }

    private int play(String message) throws NumberLengthException, NumberFormatException, AlreadyStartException{
        Pattern pattern = Pattern.compile("^[0-9]*$");
        char length = message.charAt(message.length() - 1);
        if (!pattern.matcher(String.valueOf(length)).find())
            throw new NumberFormatException();

        if(length - '0' < 3)
            throw new NumberLengthException();

        int curLength = length - '0';
        BaseballGame.startGame(curLength);
        return curLength;
    }

    private String stop(){
        if(BaseballGame.isStart()) {
            long startTime = BaseballGame.getStartTime();
            long restTime = (startTime + 10 * 60 * 1000) - System.currentTimeMillis();
            if(restTime > 0) {
                return restTime / 1000 / 60 + "분 후 종료 가능합니다.";
            } else {
                BaseballGame.endGame();
                return "숫자야구를 종료합니다.";
            }
        } else {
            return "숫자야구가 시작 중이지 않습니다.";
        }
    }

    private String verify(String message){
        if(message.length() > BaseballGame.length()){
            return "숫자가 너무 큽니다.";
        } else if(message.length() < BaseballGame.length()){
            return "숫자가 너무 작습니다.";
        } else {
            return BaseballGame.verify(message);
        }
    }
}
