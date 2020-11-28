package edu.ubb.model;

import java.util.List;

public class QuestionSheet {
    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public QuestionSheet(List<Question> questions) {
        this.questions = questions;
    }
}
