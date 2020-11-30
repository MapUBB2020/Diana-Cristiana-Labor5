package edu.ubb;

import com.sun.javafx.binding.StringFormatter;
import edu.ubb.controller.Controller;
import edu.ubb.model.Question;
import edu.ubb.model.QuestionSheet;
import edu.ubb.repository.FileRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller();
        QuestionSheet questionSheet = controller.initializeSheet();
        final int[] count = {0};

        Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));
        primaryStage.setTitle("Chestionar Auto");
        primaryStage.setScene(new Scene(root, 1100, 800));

        Scene scene = new Scene(new Group(), 1100, 800);
        Scene scene2 = new Scene(new Group(), 1100, 800);

        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("CHESTIONAR AUTO");
        titleLabel.setFont(new Font("Arial", 56));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#454545"));

        javafx.scene.control.Button startButton = new javafx.scene.control.Button("START");
        startButton.setPrefSize(170, 70);
        startButton.setFont(new Font("Arial", 20));
        startButton.setStyle("-fx-background-color: #9CEA7E; -fx-border-width: 5px; -fx-border-color: #88CD6D ");
        startButton.setOnAction(e -> primaryStage.setScene(scene2));


        VBox myVBox = new VBox();
        myVBox.setSpacing(225);
        myVBox.setPadding(new Insets(150, 0, 0, 275));
        myVBox.setAlignment(Pos.CENTER);
        myVBox.getChildren().addAll(titleLabel, startButton);
        ((Group) scene.getRoot()).getChildren().addAll(myVBox);


        class CreateScene {
            void create() {
                Scene scene3 = new Scene(new Group(), 1100, 800);

                if (count[0] == 25) {
                    javafx.scene.control.Label gata = new javafx.scene.control.Label("GATA");
                    gata.setFont(new Font("Arial", 56));

                    javafx.scene.control.Button restartButton = new javafx.scene.control.Button("INCEARCA DIN NOU");
                    restartButton.setPrefSize(250, 60);
                    restartButton.setFont(new Font("Arial", 18));
                    restartButton.setStyle("-fx-background-color: #8AD7F8");
                    restartButton.setOnAction((event) ->{
                        try {
                            start(primaryStage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    VBox vBox2 = new VBox();
                    vBox2.setSpacing(5);
                    vBox2.setPadding(new Insets(50, 0, 0, 150));
                    vBox2.getChildren().addAll(gata, restartButton);
                    ((Group) scene3.getRoot()).getChildren().addAll(vBox2);


                } else {

                    //label intrebare
                    int index = count[0]+=1;
                    String q1 = count[0]+1 + ". " + questionSheet.getQuestions().get(index).getQuestion();
                    javafx.scene.control.Label question = new javafx.scene.control.Label(q1);
                    question.setFont(new Font("Arial", 24));
                    question.setWrapText(true);
                    question.setTextAlignment(TextAlignment.JUSTIFY);
                    question.setMaxWidth(900);

                    //label raspunsuri
                    String a1 = questionSheet.getQuestions().get(index).getAnswers().get(0);
                    String a2 = questionSheet.getQuestions().get(index).getAnswers().get(1);
                    String a3 = questionSheet.getQuestions().get(index).getAnswers().get(2);

                    javafx.scene.control.Label answer1 = new javafx.scene.control.Label(a1);
                    answer1.setFont(new Font("Arial", 24));
                    answer1.setWrapText(true);
                    answer1.setTextAlignment(TextAlignment.JUSTIFY);
                    answer1.setMaxWidth(700);

                    javafx.scene.control.Label answer2 = new javafx.scene.control.Label(a2);
                    answer2.setFont(new Font("Arial", 24));
                    answer2.setWrapText(true);
                    answer2.setTextAlignment(TextAlignment.JUSTIFY);
                    answer2.setMaxWidth(700);

                    javafx.scene.control.Label answer3 = new javafx.scene.control.Label(a3);
                    answer3.setFont(new Font("Arial", 24));
                    answer3.setWrapText(true);
                    answer3.setTextAlignment(TextAlignment.JUSTIFY);
                    answer3.setMaxWidth(700);


                    //text field raspuns user
                    javafx.scene.control.Label hint = new javafx.scene.control.Label("Exemplu raspuns: 1, 2, 3");
                    hint.setFont(new Font("Arial", 14));
                    hint.setTextFill(javafx.scene.paint.Color.web("#A5A9AB"));

                    javafx.scene.control.TextField inputAnswer = new javafx.scene.control.TextField();
                    inputAnswer.setMaxWidth(100);


                    //buton trimite
                    javafx.scene.control.Button nextButton1 = new javafx.scene.control.Button("TRIMITE");
                    nextButton1.setPrefSize(170, 60);
                    nextButton1.setFont(new Font("Arial", 20));
                    nextButton1.setStyle("-fx-background-color: #9CEA7E; -fx-border-width: 5px; -fx-border-color: #88CD6D ");
                    nextButton1.setOnAction((event) -> {
                        CreateScene createScene = new CreateScene();
                        createScene.create();


                    });

                    //buton sterge
                    javafx.scene.control.Button deleteButton1 = new javafx.scene.control.Button("STERGE");
                    deleteButton1.setPrefSize(170, 60);
                    deleteButton1.setFont(new Font("Arial", 20));
                    deleteButton1.setStyle("-fx-background-color: #FD7D7D; -fx-border-width: 5px; -fx-border-color: #DF7070 ");


                    //box butoane
                    HBox myHBox1 = new HBox();
                    myHBox1.setSpacing(150);
                    myHBox1.setPadding(new Insets(300, 0, 0, 200));
                    myHBox1.getChildren().addAll(deleteButton1, nextButton1);


                    //box raspunsuri
                    VBox answerBox = new VBox();
                    answerBox.setSpacing(25);
                    answerBox.setPadding(new Insets(75, 0, 0, 0));
                    answerBox.getChildren().addAll(answer1, answer2, answer3, hint, inputAnswer);

                    //box intreg
                    VBox vBox1 = new VBox();
                    vBox1.setSpacing(5);
                    vBox1.setPadding(new Insets(50, 0, 0, 125));
                    vBox1.getChildren().addAll(question, answerBox, myHBox1);
                    ((Group) scene3.getRoot()).getChildren().addAll(vBox1);

                }
                primaryStage.setScene(scene3);
                primaryStage.show();


            }
        }

        //scene2

        String q = count[0] + 1 + ". " + questionSheet.getQuestions().get(count[0]).getQuestion();
        javafx.scene.control.Label firstQ = new javafx.scene.control.Label(q);
        firstQ.setWrapText(true);
        firstQ.setFont(new Font("Arial", 24));
        firstQ.setWrapText(true);
        firstQ.setTextAlignment(TextAlignment.JUSTIFY);
        firstQ.setMaxWidth(900);

        String a11 = questionSheet.getQuestions().get(0).getAnswers().get(0);
        String a12 = questionSheet.getQuestions().get(0).getAnswers().get(1);
        String a13 = questionSheet.getQuestions().get(0).getAnswers().get(2);

        javafx.scene.control.Label answer11 = new javafx.scene.control.Label(a11);
        answer11.setFont(new Font("Arial", 24));
        answer11.setWrapText(true);
        answer11.setTextAlignment(TextAlignment.JUSTIFY);
        answer11.setMaxWidth(900);

        javafx.scene.control.Label answer12 = new javafx.scene.control.Label(a12);
        answer12.setFont(new Font("Arial", 24));
        answer12.setWrapText(true);
        answer12.setTextAlignment(TextAlignment.JUSTIFY);
        answer12.setMaxWidth(900);

        javafx.scene.control.Label answer13 = new javafx.scene.control.Label(a13);
        answer13.setFont(new Font("Arial", 24));
        answer13.setWrapText(true);
        answer13.setTextAlignment(TextAlignment.JUSTIFY);
        answer13.setMaxWidth(900);

        //text field raspuns user
        javafx.scene.control.Label hint1 = new javafx.scene.control.Label("Exemplu raspuns: 1, 2, 3");
        hint1.setFont(new Font("Arial", 14));
        hint1.setTextFill(javafx.scene.paint.Color.web("#A5A9AB"));

        javafx.scene.control.TextField inputAnswer1 = new javafx.scene.control.TextField();
        inputAnswer1.setMaxWidth(100);

        //buton trimite
        javafx.scene.control.Button nextButton = new javafx.scene.control.Button("TRIMITE");
        nextButton.setPrefSize(170, 60);
        nextButton.setFont(new Font("Arial", 20));
        nextButton.setStyle("-fx-background-color: #9CEA7E; -fx-border-width: 5px; -fx-border-color: #88CD6D ");
        nextButton.setOnAction((event) -> {
            CreateScene createScene = new CreateScene();
            createScene.create();

        });

        //buton sterge
        javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("STERGE");
        deleteButton.setPrefSize(170, 60);
        deleteButton.setFont(new Font("Arial", 20));
        deleteButton.setStyle("-fx-background-color: #FD7D7D; -fx-border-width: 5px; -fx-border-color: #DF7070 ");

        //box butoane
        HBox myHBox = new HBox();
        myHBox.setSpacing(150);
        myHBox.setPadding(new Insets(300, 0, 0, 200));
        myHBox.getChildren().addAll(deleteButton, nextButton);


        //box raspunsuri
        VBox answerBox1 = new VBox();
        answerBox1.setSpacing(25);
        answerBox1.setPadding(new Insets(75, 0, 0, 0));
        answerBox1.getChildren().addAll(answer11, answer12, answer13, hint1, inputAnswer1);


        //box intreg
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(50, 0, 0, 125));
        vBox.getChildren().addAll(firstQ, answerBox1, myHBox);
        ((Group) scene2.getRoot()).getChildren().addAll(vBox);


        // Scene scene = new Scene( 1100, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*@Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Group());
        stage.setTitle("Chestionar Auto");
        stage.setWidth(1100);
        stage.setHeight(800);

        Label titleLabel = new Label("CHESTIONAR AUTO");
        titleLabel.setAlignment(Label.CENTER);
        titleLabel.setFont(new Font("Arial"));


        stage.show();
    }*/


    public static void main(String[] args) throws IOException {
        launch(args);
        /*Controller controller = new Controller();
        int index = 1;
        for (Question q : controller.initializeSheet().getQuestions()) {
            System.out.println(index++ + " " + q);
        }*/

    }
}
