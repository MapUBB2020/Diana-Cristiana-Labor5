package edu.ubb;

import edu.ubb.controller.Controller;
import edu.ubb.model.Question;
import edu.ubb.model.QuestionSheet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Time;

public class Main extends Application {

    private javafx.scene.control.Label timerLabel = new javafx.scene.control.Label();
    private Integer timeMinutes = 30;
    private int correctPoints;
    private int incorrectPoints;
    private final String admis = "Felicitari! Ati fost declarat" +
            " ADMIS la examenul de teorie.";
    private final String respins = "Testul a luat sfarsit. Ati fost declarat" +
            " RESPINS la examenul de teorie.";

    private static final Integer start = 60;
    private Timeline timeline;
    private int min = 30;
    private int startTimeSec, startTimeMin;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller();
        correctPoints = 0;
        incorrectPoints = 0;
        QuestionSheet questionSheet = controller.initializeSheet();
        final int[] count = {0};


        //nume pagina
        Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));
        primaryStage.setTitle("Chestionar Auto");
        primaryStage.setScene(new Scene(root, 1100, 800));

        Scene scene = new Scene(new Group(), 1100, 800);
        Scene scene2 = new Scene(new Group(), 1100, 800);


        //timer
        timerLabel.setText("30:00");
        timerLabel.setTextFill(javafx.scene.paint.Color.web("#FD0909"));
        timerLabel.setStyle("-fx-font-size: 4em;");

        //antet: rasp corecte + gresite
        javafx.scene.control.Label correct = new javafx.scene.control.Label();
        correct.setFont(new Font("Arial", 46));
        javafx.scene.control.Label incorrect = new javafx.scene.control.Label();
        incorrect.setFont(new Font("Arial", 46));


        //label titlu
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("CHESTIONAR AUTO");
        titleLabel.setFont(new Font("Arial", 56));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#454545"));

        //label subtitlu
        javafx.scene.control.Label subtitleLabel = new javafx.scene.control.Label("Categoria B, B1, TR");
        subtitleLabel.setFont(new Font("Arial", 28));
        subtitleLabel.setTextFill(javafx.scene.paint.Color.web("#6E6E6E"));

        //imagine
        File file = new File("D:\\MAP\\Diana-Cristiana-Labor5\\car1.png"); //trebuie scrisa adresa imaginii salvate in calc tau
        javafx.scene.image.Image image = new Image(file.toURI().toString());
        javafx.scene.image.ImageView iv = new ImageView(image);


        //buton start
        javafx.scene.control.Button startButton = new javafx.scene.control.Button("START");
        startButton.setPrefSize(170, 70);
        startButton.setFont(new Font("Arial", 20));
        startButton.setStyle("-fx-background-color: #9CEA7E; -fx-border-width: 5px; -fx-border-color: #88CD6D ");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(scene2);
                timeline = new Timeline();
                if (timeline != null) {
                    timeline.stop();
                }
                if (startTimeMin >= 0) {
                    KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    startTimeSec--;
                                    boolean isSecondZero = startTimeSec < 0;
                                    boolean timeToStop = startTimeSec < 0 && startTimeMin == 0;

                                    if (isSecondZero) {
                                        startTimeMin--;
                                        startTimeSec = 59;
                                    }

                                    if (timeToStop) {
                                        timeline.stop();
                                        startTimeMin = 0;
                                        startTimeSec = 0;
                                        Scene scene4 = new Scene(new Group(), 1100, 800);
                                        if (correctPoints >= 22) {
                                            sfarsit(scene4, primaryStage, admis);

                                        } else {
                                            sfarsit(scene4, primaryStage, respins);
                                        }
                                        primaryStage.setScene(scene4);
                                        primaryStage.show();

                                    }
                                    timerLabel.setText(String.format("%02d:%02d", startTimeMin, startTimeSec));

                                }
                            });
                    startTimeSec = start;
                    startTimeMin = min - 1;
                    timeline.setCycleCount(Timeline.INDEFINITE);
                    timeline.getKeyFrames().add(keyFrame);
                    timeline.playFromStart();
                }

            }
        });


        VBox myVBox = new VBox();
        myVBox.setSpacing(50);
        myVBox.setPadding(new Insets(150, 0, 0, 275));
        myVBox.setAlignment(Pos.CENTER);
        myVBox.getChildren().addAll(titleLabel, subtitleLabel, iv, startButton);
        ((Group) scene.getRoot()).getChildren().addAll(myVBox);


        class CreateScene {
            void create() {
                Scene scene3 = new Scene(new Group(), 1100, 800);

                if (count[0] == 25 && incorrectPoints < 5) {
                    sfarsit(scene3, primaryStage, admis);
                    timeline.stop();
                    startTimeMin = 0;
                    startTimeSec = 0;


                } else if (incorrectPoints > 4) {
                    sfarsit(scene3, primaryStage, respins);
                    timeline.stop();
                    startTimeMin = 0;
                    startTimeSec = 0;

                } else {

                    //label intrebare
                    int index = count[0] += 1;
                    String q1 = count[0] + 1 + ". " + questionSheet.getQuestions().get(index).getQuestion();
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
                    javafx.scene.control.Label hint = new javafx.scene.control.Label("Exemplu raspuns: A, B, C");
                    hint.setFont(new Font("Arial", 16));
                    hint.setTextFill(javafx.scene.paint.Color.web("#A5A9AB"));

                    javafx.scene.control.TextField inputAnswer1 = new javafx.scene.control.TextField();
                    inputAnswer1.setMaxWidth(200);
                    inputAnswer1.setFont(new Font("Arial", 18));

                    //update valori puncte
                    correct.setText("✓" + correctPoints);
                    incorrect.setText("✗" + incorrectPoints);

                    //buton trimite
                    javafx.scene.control.Button nextButton1 = new javafx.scene.control.Button("TRIMITE");
                    nextButton1.setPrefSize(170, 60);
                    nextButton1.setFont(new Font("Arial", 20));
                    nextButton1.setStyle("-fx-background-color: #9CEA7E; -fx-border-width: 5px; -fx-border-color: #88CD6D ");
                    nextButton1.setOnAction((event) -> {

                        String answer = inputAnswer1.getText();
                        Question question1 = questionSheet.getQuestions().get(count[0]);
                        correctPoints = controller.answerQuestion(correctPoints, incorrectPoints, answer, question1)[0];
                        incorrectPoints = controller.answerQuestion(correctPoints, incorrectPoints, answer, question1)[1];

                        CreateScene createScene = new CreateScene();
                        createScene.create();


                    });

                    //buton sterge
                    javafx.scene.control.Button deleteButton1 = new javafx.scene.control.Button("STERGE");
                    deleteButton1.setPrefSize(170, 60);
                    deleteButton1.setFont(new Font("Arial", 20));
                    deleteButton1.setStyle("-fx-background-color: #FD7D7D; -fx-border-width: 5px; -fx-border-color: #DF7070 ");
                    deleteButton1.setOnAction(event -> inputAnswer1.clear());


                    //box antet
                    HBox antet = new HBox();
                    antet.setSpacing(150);
                    antet.setPadding(new Insets(0, 0, 0, 150));
                    antet.getChildren().addAll(correct, timerLabel, incorrect);


                    //box butoane
                    HBox myHBox1 = new HBox();
                    myHBox1.setSpacing(150);
                    myHBox1.setPadding(new Insets(50, 0, 0, 200));
                    myHBox1.getChildren().addAll(deleteButton1, nextButton1);


                    //box raspunsuri
                    VBox answerBox = new VBox();
                    answerBox.setSpacing(25);
                    answerBox.setPadding(new Insets(75, 0, 0, 0));
                    answerBox.getChildren().addAll(answer1, answer2, answer3, hint, inputAnswer1);

                    //box intreg
                    VBox vBox1 = new VBox();
                    vBox1.setSpacing(20);
                    vBox1.setPadding(new Insets(50, 0, 0, 125));
                    vBox1.getChildren().addAll(antet, question, answerBox, myHBox1);
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
        javafx.scene.control.Label hint1 = new javafx.scene.control.Label("Exemplu raspuns: A, B, C");
        hint1.setFont(new Font("Arial", 16));
        hint1.setTextFill(javafx.scene.paint.Color.web("#A5A9AB"));

        javafx.scene.control.TextField inputAnswer = new javafx.scene.control.TextField();
        inputAnswer.setMaxWidth(200);
        inputAnswer.setFont(new Font("Arial", 18));

        //buton trimite
        javafx.scene.control.Button nextButton = new javafx.scene.control.Button("TRIMITE");
        nextButton.setPrefSize(170, 60);
        nextButton.setFont(new Font("Arial", 20));
        nextButton.setStyle("-fx-background-color: #9CEA7E; -fx-border-width: 5px; -fx-border-color: #88CD6D ");
        nextButton.setOnAction((event) -> {
            String answer = inputAnswer.getText();
            Question question = questionSheet.getQuestions().get(count[0]);
            correctPoints = controller.answerQuestion(correctPoints, incorrectPoints, answer, question)[0];
            incorrectPoints = controller.answerQuestion(correctPoints, incorrectPoints, answer, question)[1];

            CreateScene createScene = new CreateScene();
            createScene.create();

        });

        //update valori puncte
        correct.setText("✓" + correctPoints);
        incorrect.setText("✗" + incorrectPoints);

        //buton sterge
        javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("STERGE");
        deleteButton.setPrefSize(170, 60);
        deleteButton.setFont(new Font("Arial", 20));
        deleteButton.setStyle("-fx-background-color: #FD7D7D; -fx-border-width: 5px; -fx-border-color: #DF7070 ");
        deleteButton.setOnAction(event -> inputAnswer.clear());


        //box antet
        HBox antet1 = new HBox();
        antet1.setSpacing(150);
        antet1.setPadding(new Insets(0, 0, 0, 150));
        antet1.getChildren().addAll(correct, timerLabel, incorrect);


        //box butoane
        HBox myHBox = new HBox();
        myHBox.setSpacing(150);
        myHBox.setPadding(new Insets(50, 0, 0, 200));
        myHBox.getChildren().addAll(deleteButton, nextButton);


        //box raspunsuri
        VBox answerBox1 = new VBox();
        answerBox1.setSpacing(25);
        answerBox1.setPadding(new Insets(75, 0, 0, 0));
        answerBox1.getChildren().addAll(answer11, answer12, answer13, hint1, inputAnswer);


        //box intreg
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(50, 0, 0, 125));
        vBox.getChildren().addAll(antet1, firstQ, answerBox1, myHBox);
        ((Group) scene2.getRoot()).getChildren().addAll(vBox);


        // Scene scene = new Scene( 1100, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sfarsit(Scene scene4, Stage primaryStage, String text) {
        javafx.scene.control.Label gata = new javafx.scene.control.Label(text);
        gata.setFont(new Font("Arial", 30));
        gata.setWrapText(true);
        gata.setTextAlignment(TextAlignment.JUSTIFY);
        gata.setMaxWidth(900);

        javafx.scene.control.Label correctAns = new javafx.scene.control.Label("Raspunsuri corecte: " + correctPoints);
        correctAns.setFont(new Font("Arial", 25));
        correctAns.setTextFill(javafx.scene.paint.Color.web("#187609"));
        javafx.scene.control.Label incorrectAns = new javafx.scene.control.Label("Raspunsuri gresite: " + incorrectPoints);
        incorrectAns.setFont(new Font("Arial", 25));
        incorrectAns.setTextFill(javafx.scene.paint.Color.web("#760909"));

        Button restartButton = new Button("INCEARCA DIN NOU");
        restartButton.setPrefSize(250, 75);
        restartButton.setFont(new Font("Arial", 20));
        restartButton.setStyle("-fx-background-color: #8AD7F8");
        restartButton.setOnAction((event2) -> {
            try {
                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //box text
        HBox hbox2 = new HBox();
        hbox2.setSpacing(5);
        hbox2.setPadding(new Insets(50, 0, 0, 0));
        hbox2.getChildren().add(gata);

        //box rasp
        VBox vBox3 = new VBox();
        vBox3.setSpacing(50);
        vBox3.setPadding(new Insets(150, 0, 0, 300));
        vBox3.getChildren().addAll(correctAns, incorrectAns);

        //box buton restart
        VBox vBox = new VBox();
        vBox.setSpacing(50);
        vBox.setPadding(new Insets(150, 0, 0, 300));
        vBox.getChildren().add(restartButton);

        //box intreg
        VBox vBox2 = new VBox();
        vBox2.setSpacing(30);
        vBox2.setPadding(new Insets(25, 0, 0, 100));
        vBox2.getChildren().addAll(hbox2, vBox3, vBox);
        ((Group) scene4.getRoot()).getChildren().addAll(vBox2);
    }


    public static void main(String[] args) throws IOException {
        launch(args);

    }
}
