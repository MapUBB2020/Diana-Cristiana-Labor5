package edu.ubb.repository;

import edu.ubb.model.Question;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {
    public List<Question> questions = new ArrayList<Question>();

    public FileRepository() {
    }

    public List<Question> readQuestions() throws IOException {
        String line = " ";
        BufferedReader br = new BufferedReader(new FileReader("src\\edu\\ubb\\questions.txt"));
        while ((line = br.readLine()) != null) {
            Question question = new Question();
            List<String> answers = new ArrayList<>();
            List<String> correct = new ArrayList<>();
            String[] obj = line.split("; ");

            question.setQuestion(obj[0]); //primul field e intrebarea
            answers.add(obj[1]); //fields 2, 3, 4 -> raspunsuri
            answers.add(obj[2]);
            answers.add(obj[3]);
            question.setAnswers(answers);

            //lista de la field4 -> lista index raspunsuri corecte
            String[] choice = obj[4].split(", ");
            for (int i = 0; i < choice.length; i++) {
                if (i == 0) {
                    correct.add(choice[0].substring(1, 2));
                } else {
                    correct.add(choice[i].substring(0, 1));
                }
            }
            question.setCorrectAnswers(correct);
            questions.add(question);
        }
        return questions;
    }
}