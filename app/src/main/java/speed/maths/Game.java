package speed.maths;

import java.util.LinkedList;

public class Game {
    public static final String ADD = "+";
    public static final String SUBTRACT = "−";
    public static final String MULTIPLY = "×";

    private int maximumNumber;
    private int minimumNumber;

    private int numberOfQuestions;

    private LinkedList<Question> questions;

    private String[] allowedOperators;

    private String savecode;

    public Game(int minimumNumber, int maximumNumber, int numberOfQuestions, String[] allowedOperators) {
        this.minimumNumber = minimumNumber;
        this.maximumNumber = maximumNumber;
        this.numberOfQuestions = numberOfQuestions;
        this.allowedOperators = allowedOperators;
        questions = new LinkedList<>();
        generateSavecode();
        generateQuestions();

    }

    public void generateQuestions() {
        for (int i = 0; i < numberOfQuestions; i++) {
            questions.add(new Question(minimumNumber, maximumNumber, allowedOperators));
        }
    }

    private void generateSavecode() {
        String operatorscode = "";
        for (String operator : allowedOperators)
            operatorscode += operator;
        savecode = numberOfQuestions + "_" + operatorscode + "_" + minimumNumber + "-" + maximumNumber;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public String getSavecode() {
        return savecode;
    }

    public LinkedList<Question> getQuestions() {
        return questions;
    }


}
