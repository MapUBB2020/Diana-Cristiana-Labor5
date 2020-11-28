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

    public int answerQuestion(int points, List<Integer> answers, Question q) {
        if (answers == q.getCorrectAnswers()) {
            points += 1;
        }
        else {
            points -= 1;
        }
        return points;
    }

    public boolean verify(int points) {
        // daca are mai mult de 4 raspunsuri gresite => false
        return points < 4;
    }
}
