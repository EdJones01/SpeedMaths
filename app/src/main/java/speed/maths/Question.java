package speed.maths;

import java.util.Random;

public class Question {
    private int max = 10;
    private int min = 1;
    String[] allowedOperators;

    private int a;
    private int b;
    private String operator;
    private int answer;

    public Question(int min, int max, String[] allowedOperators) {
        this.min = min;
        this.max = max;
        this.allowedOperators = allowedOperators;

        a = randomNumber(min, max);
        b = randomNumber(min, max);
        operator = randomOperator();
        calculateAnswer();
    }

    public String toString() {
        return a + " " + operator + " " + b;
    }

    private int randomNumber(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    private String randomOperator() {
        return allowedOperators[randomNumber(0, allowedOperators.length)];
    }

    private void calculateAnswer() {
        if (operator.equals(Game.ADD))
            answer = a + b;
        if (operator.equals(Game.SUBTRACT))
            answer = a - b;
        if (operator.equals(Game.MULTIPLY))
            answer = a * b;
    }

    public int getAnswer() {
        return answer;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
