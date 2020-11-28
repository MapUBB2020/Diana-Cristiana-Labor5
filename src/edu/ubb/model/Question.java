package edu.ubb.model;

import java.util.List;

public class Question {
    private String question;
    private List<String> answers;
    private List<Integer> correctAnswers;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Question(String question, List<String> answers, List<Integer> correctAnswers) {
        this.question = question;
        this.answers = answers;
        this.correctAnswers = correctAnswers;
    }
}
