package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/4/29.
 * old
 */

public class AnswerHistoryBean {

    private String question;
    private String correctAnswer;
    private String yourAnswer;

    public AnswerHistoryBean() {
    }

    public AnswerHistoryBean(String question, String correctAnswer, String yourAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.yourAnswer = yourAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getYourAnswer() {
        return yourAnswer;
    }

    @Override
    public String toString() {
        return "QuestionHistoryBean{" +
                "question='" + question + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", yourAnswer='" + yourAnswer + '\'' +
                '}';
    }
}
