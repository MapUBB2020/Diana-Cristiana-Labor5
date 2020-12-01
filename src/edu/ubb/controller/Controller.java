package edu.ubb.controller;

import edu.ubb.model.Question;
import edu.ubb.model.QuestionSheet;
import edu.ubb.repository.FileRepository;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //StartUp
    }
    public QuestionSheet questionSheet = new QuestionSheet();
    FileRepository fileRepository = new FileRepository();
    public List<Question> allQuestions;

    public QuestionSheet initializeSheet() throws IOException {
        allQuestions = fileRepository.readQuestions();
        List<Question> randomQuestions = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 26; i++) {
            //se iau random 26 intrebari pentru un chestionar
            int randomIndex = rand.nextInt(allQuestions.size()); //alegem un nr random care e indexul unei intrebari
            Question randomQuestion = allQuestions.get(randomIndex); //se ia intrebarea de la indexul random
            allQuestions.remove(randomIndex); //se sterge intrebarea din setul intreg de intrebari pentru a nu mai putea fi aleasa inca o data
            randomQuestions.add(randomQuestion); //se adauga la chestionar

        }
        questionSheet.setQuestions(randomQuestions);
        return questionSheet;
    }

    /**
     *
     * @param correct nr rasp corecte
     * @param incorrect nr rasp incorecte
     * @param answers input tastatura
     * @param q intrebare actuala
     * @return points[0] -correct, points[1] -incorrect
     * */
    public int[] answerQuestion(int correct, int incorrect, String answers, Question q) {
        String answer = "";
        for (int i = 0; i < q.getCorrectAnswers().size(); i++) {
            if (i == q.getCorrectAnswers().size()-1) {
                answer+=q.getCorrectAnswers().get(i);
            }
            else {
                answer += q.getCorrectAnswers().get(i) + ", ";
            }
        }
        int[] points = new int[2];
        if (answers.equals(answer)) {
            correct += 1;
        }
        else {
            incorrect += 1;
        }
        points[0] = correct;
        points[1] = incorrect;
        return points;
    }

}
