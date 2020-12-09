package edu.ubb;

import edu.ubb.controller.Controller;
import edu.ubb.model.Question;
import edu.ubb.model.QuestionSheet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
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
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private javafx.scene.control.Label timerLabel = new javafx.scene.control.Label();
    private int correctPoints;
    private int incorrectPoints;
    private final String admis = "Felicitari! Ati fost declarat" +
            " ADMIS la examenul de teorie.";
    private final String respins = "Testul a luat sfarsit. Ati fost declarat" +
            " RESPINS la examenul de teorie.";

    private static final Integer start = 60; //60 sec
    private Timeline timeline;
    private int min = 30; //30 min
    private int startTimeSec, startTimeMin;
    private List<Question> finalQuestions; //lista intrebari raspunse
    private List<String> finalAnswers; //lista raspunsuri


    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller();
        correctPoints = 0;
        incorrectPoints = 0;
        QuestionSheet questionSheet = controller.initializeSheet();
        final int[] count = {0}; //nr intrebare curenta
        finalQuestions = new ArrayList<>(); //resetare liste la inceperea fiecarui chestionar
        finalAnswers = new ArrayList<>();
        final int[] index = new int[1];
        List<Integer> questionNumber = new ArrayList<>();
        final int[] countLaterQ = {0};


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
                //se deschide scene2 => pagina cu prima intrebare
                primaryStage.setScene(scene2);
                timeline = new Timeline();
                if (timeline != null) {
                    timeline.stop();
                }
                if (startTimeMin >= 0) {
                    //incepe timer-ul
                    KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    startTimeSec--;
                                    boolean isSecondZero = startTimeSec < 0; //daca secundele ajung la 0 => scad minutele
                                    boolean timeToStop = startTimeSec < 0 && startTimeMin == 0; //daca ajung sec + min la 0 => se opreste timpul => se ajunge la ultima pag

                                    if (isSecondZero) {
                                        startTimeMin--;
                                        startTimeSec = 59;
                                    }

                                    if (timeToStop) {
                                        timeline.stop();
                                        startTimeMin = 0;
                                        startTimeSec = 0;
                                        //scene4 - ultima pagina
                                        Scene scene4 = new Scene(new Group(), 1100, 800);
                                        if (correctPoints >= 22) {
                                            //cel putin 22 rasp corecte => admis
                                            sfarsit(scene4, primaryStage, admis);

                                        } else {
                                            //altfel => respins
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


        //box pagina inceput
        VBox firstPageBox = new VBox();
        firstPageBox.setSpacing(50);
        firstPageBox.setPadding(new Insets(150, 0, 0, 275));
        firstPageBox.setAlignment(Pos.CENTER);
        firstPageBox.getChildren().addAll(titleLabel, subtitleLabel, iv, startButton);
        ((Group) scene.getRoot()).getChildren().addAll(firstPageBox);


        //clasa creare scena folosita pentru fiecare intrebare de la a 2-a incepand
        class CreateScene {
            void create() {
                //pagina intrebari (resetata pt fiecare intrebare)
                Scene scene3 = new Scene(new Group(), 1100, 800);

                //daca a ajuns la final si cu chestionarul si cu intrebarile pe care le-a lasat pe mai tarziu sau daca ajunge la final si are mai putin de 5 intrebari gresite => admis
                if ((count[0] >= 25 && countLaterQ[0] == questionNumber.size() && incorrectPoints < 5) || (questionNumber.isEmpty() && count[0] == 25 && incorrectPoints < 5)) {
                    sfarsit(scene3, primaryStage, admis);
                    timeline.stop();
                    startTimeMin = 0;
                    startTimeSec = 0;


                } else if (incorrectPoints > 4) {
                    //daca are mai mult de 4 intrebari gresite => respins
                    sfarsit(scene3, primaryStage, respins);
                    timeline.stop();
                    startTimeMin = 0;
                    startTimeSec = 0;

                } else {
                    //resetare pag cu intrebari

                    //daca mai are intrebari lasate pe mai tarziu, se ia numarul intrebarii
                    if (count[0] == 25) {
                        index[0] = questionNumber.get(countLaterQ[0]);
                        countLaterQ[0] += 1;
                    }
                    else {
                        index[0] = count[0] += 1;
                    }

                    //label intrebare
                    String q1 = index[0] + 1 + ". " + questionSheet.getQuestions().get(index[0]).getQuestion(); //ia intrebarile pe rand, incepand de la a2a
                    javafx.scene.control.Label question = new javafx.scene.control.Label(q1);
                    question.setFont(new Font("Arial", 24));
                    question.setWrapText(true);
                    question.setTextAlignment(TextAlignment.JUSTIFY);
                    question.setMaxWidth(900);

                    //ia raspunsurile pe rand
                    String a1 = questionSheet.getQuestions().get(index[0]).getAnswers().get(0);
                    String a2 = questionSheet.getQuestions().get(index[0]).getAnswers().get(1);
                    String a3 = questionSheet.getQuestions().get(index[0]).getAnswers().get(2);


                    //label-uri raspunsuri
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


                    //text field raspuns user + hint
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
                        //se inregistreaza rasp, se vf daca e corect si se actualizeaza nr de puncte corecte/gresite
                        String answer = inputAnswer1.getText();
                        Question question1 = questionSheet.getQuestions().get(index[0]);
                        finalQuestions.add(question1);
                        finalAnswers.add(answer);
                        correctPoints = controller.answerQuestion(correctPoints, incorrectPoints, answer, question1)[0];
                        incorrectPoints = controller.answerQuestion(correctPoints, incorrectPoints, answer, question1)[1];

                        //se apeleaza din nou functia
                        CreateScene createScene = new CreateScene();
                        createScene.create();


                    });


                    //buton mai tazriu
                    javafx.scene.control.Button laterButton = new javafx.scene.control.Button("MAI TARZIU");
                    laterButton.setPrefSize(170, 60);
                    laterButton.setFont(new Font("Arial", 20));
                    laterButton.setStyle("-fx-background-color: #A3E4D7; -fx-border-width: 5px; -fx-border-color: #76D7C4 ");
                    laterButton.setOnAction(event -> {
                        Question question1 = questionSheet.getQuestions().get(count[0]);
                        questionSheet.getQuestions().add(question1); //adaugam intrebarea la sfarsit
                        questionNumber.add(index[0]); //adaugam si numarul intrebarii

                        //se apeleaza din nou functia
                        CreateScene createScene = new CreateScene();
                        createScene.create();
                    });


                    //buton sterge
                    javafx.scene.control.Button deleteButton1 = new javafx.scene.control.Button("STERGE");
                    deleteButton1.setPrefSize(170, 60);
                    deleteButton1.setFont(new Font("Arial", 20));
                    deleteButton1.setStyle("-fx-background-color: #FD7D7D; -fx-border-width: 5px; -fx-border-color: #DF7070 ");
                    deleteButton1.setOnAction(event -> inputAnswer1.clear()); //sterge input-ul


                    //box antet
                    HBox antet = new HBox();
                    antet.setSpacing(150);
                    antet.setPadding(new Insets(0, 0, 0, 150));
                    antet.getChildren().addAll(correct, timerLabel, incorrect);


                    //box butoane
                    HBox buttonsBox = new HBox();
                    buttonsBox.setSpacing(70);
                    buttonsBox.setPadding(new Insets(50, 0, 0, 100));
                    buttonsBox.getChildren().addAll(deleteButton1, laterButton, nextButton1);


                    //box raspunsuri
                    VBox answerBox = new VBox();
                    answerBox.setSpacing(25);
                    answerBox.setPadding(new Insets(75, 0, 0, 0));
                    answerBox.getChildren().addAll(answer1, answer2, answer3, hint, inputAnswer1);

                    //box intreg
                    VBox finalBox = new VBox();
                    finalBox.setSpacing(20);
                    finalBox.setPadding(new Insets(50, 0, 0, 125));
                    finalBox.getChildren().addAll(antet, question, answerBox, buttonsBox);
                    ((Group) scene3.getRoot()).getChildren().addAll(finalBox);

                }
                primaryStage.setScene(scene3);
                primaryStage.show();


            }
        }

        //scene2
        //pagina care contine prima intrebare
        index[0] = count[0];
        String q = index[0] + 1 + ". " + questionSheet.getQuestions().get(count[0]).getQuestion(); //ia prima intrebare
        javafx.scene.control.Label firstQ = new javafx.scene.control.Label(q);
        firstQ.setFont(new Font("Arial", 24));
        firstQ.setWrapText(true);
        firstQ.setTextAlignment(TextAlignment.JUSTIFY);
        firstQ.setMaxWidth(900);

        //ia raspunsurile pe rand
        String a11 = questionSheet.getQuestions().get(0).getAnswers().get(0);
        String a12 = questionSheet.getQuestions().get(0).getAnswers().get(1);
        String a13 = questionSheet.getQuestions().get(0).getAnswers().get(2);


        //label-uri raspunsuri
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

        //text field raspuns user + hint
        javafx.scene.control.Label hint1 = new javafx.scene.control.Label("Exemplu raspuns: A, B, C");
        hint1.setFont(new Font("Arial", 16));
        hint1.setTextFill(javafx.scene.paint.Color.web("#A5A9AB"));

        javafx.scene.control.TextField inputAnswer = new javafx.scene.control.TextField();
        inputAnswer.setMaxWidth(200);
        inputAnswer.setFont(new Font("Arial", 18));

        //buton trimite (analog buton trimite din createScene)
        javafx.scene.control.Button nextButton = new javafx.scene.control.Button("TRIMITE");
        nextButton.setPrefSize(170, 60);
        nextButton.setFont(new Font("Arial", 20));
        nextButton.setStyle("-fx-background-color: #9CEA7E; -fx-border-width: 5px; -fx-border-color: #88CD6D ");
        nextButton.setOnAction((event) -> {
            String answer = inputAnswer.getText();
            Question question = questionSheet.getQuestions().get(count[0]);
            finalQuestions.add(question);
            finalAnswers.add(answer);
            correctPoints = controller.answerQuestion(correctPoints, incorrectPoints, answer, question)[0];
            incorrectPoints = controller.answerQuestion(correctPoints, incorrectPoints, answer, question)[1];

            CreateScene createScene = new CreateScene();
            createScene.create();

        });

        //buton mai tazriu
        javafx.scene.control.Button laterButton = new javafx.scene.control.Button("MAI TARZIU");
        laterButton.setPrefSize(170, 60);
        laterButton.setFont(new Font("Arial", 20));
        laterButton.setStyle("-fx-background-color: #A3E4D7; -fx-border-width: 5px; -fx-border-color: #76D7C4 ");
        laterButton.setOnAction(event -> {
            Question question1 = questionSheet.getQuestions().get(count[0]);
            questionSheet.getQuestions().add(question1); //adaugam intrebarea la sfarsit
            questionNumber.add(index[0]);

            //se apeleaza din nou functia
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
        HBox buttonsBox = new HBox();
        buttonsBox.setSpacing(70);
        buttonsBox.setPadding(new Insets(50, 0, 0, 100));
        buttonsBox.getChildren().addAll(deleteButton, laterButton, nextButton);


        //box raspunsuri
        VBox answerBox1 = new VBox();
        answerBox1.setSpacing(25);
        answerBox1.setPadding(new Insets(75, 0, 0, 0));
        answerBox1.getChildren().addAll(answer11, answer12, answer13, hint1, inputAnswer);


        //box intreg
        VBox finalBox = new VBox();
        finalBox.setSpacing(20);
        finalBox.setPadding(new Insets(50, 0, 0, 125));
        finalBox.getChildren().addAll(antet1, firstQ, answerBox1, buttonsBox);
        ((Group) scene2.getRoot()).getChildren().addAll(finalBox);


        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /***
     * functie creare pagina de sfarsit
     * @param scene - scena creata
     * @param primaryStage - primaryStage
     * @param text - ADMIS/RESPINS
     */
    private void sfarsit(Scene scene, Stage primaryStage, String text) {

        //label text admis/respins
        javafx.scene.control.Label gata = new javafx.scene.control.Label(text);
        gata.setFont(new Font("Arial", 30));
        gata.setWrapText(true);
        gata.setTextAlignment(TextAlignment.JUSTIFY);
        gata.setMaxWidth(900);

        //label raspunsuri corecte - gresite
        javafx.scene.control.Label correctAns = new javafx.scene.control.Label("Raspunsuri corecte: " + correctPoints);
        correctAns.setFont(new Font("Arial", 25));
        correctAns.setTextFill(javafx.scene.paint.Color.web("#187609"));
        javafx.scene.control.Label incorrectAns = new javafx.scene.control.Label("Raspunsuri gresite: " + incorrectPoints);
        incorrectAns.setFont(new Font("Arial", 25));
        incorrectAns.setTextFill(javafx.scene.paint.Color.web("#760909"));

        //buton restart
        Button restartButton = new Button("INCEARCA DIN NOU");
        restartButton.setPrefSize(250, 75);
        restartButton.setFont(new Font("Arial", 20));
        restartButton.setStyle("-fx-background-color: #8AD7F8");
        restartButton.setOnAction((event2) -> {
            try {
                //revine la prima pagina
                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //buton vezi raspunsuri
        Button answButton = new Button("VEZI RASPUNSURILE");
        answButton.setPrefSize(250, 50);
        answButton.setFont(new Font("Arial", 17));
        answButton.setStyle("-fx-background-color: #9CEA7E");
        answButton.setOnAction(event -> {
            //pagina cu intrebarile raspunse + rasp user + rasp corecte
            Scene seeAnswersScene = new Scene(new Group(), 1100, 800);
            primaryStage.setScene(seeAnswersScene);

            //box intrebari + rasp + rasp user + rasp corecte
            VBox totalBox = new VBox();
            totalBox.setSpacing(30);
            totalBox.setPadding(new Insets(50, 0, 0, 0));

            for (int i = 0; i < finalQuestions.size(); i++) {
                //se genereaza fiecare intrebare si raspunsurile ei

                //label intrebare
                javafx.scene.control.Label questionLabel = new javafx.scene.control.Label(i + 1 + ". " + finalQuestions.get(i).getQuestion());
                questionLabel.setFont(new Font("Arial", 24));
                questionLabel.setWrapText(true);
                questionLabel.setTextAlignment(TextAlignment.JUSTIFY);
                questionLabel.setMaxWidth(900);


                //label raspunsuri
                javafx.scene.control.Label answer1Label = new javafx.scene.control.Label(finalQuestions.get(i).getAnswers().get(0));
                answer1Label.setFont(new Font("Arial", 20));
                answer1Label.setWrapText(true);
                answer1Label.setTextAlignment(TextAlignment.JUSTIFY);
                answer1Label.setMaxWidth(900);

                javafx.scene.control.Label answer2Label = new javafx.scene.control.Label(finalQuestions.get(i).getAnswers().get(1));
                answer2Label.setFont(new Font("Arial", 20));
                answer2Label.setWrapText(true);
                answer2Label.setTextAlignment(TextAlignment.JUSTIFY);
                answer2Label.setMaxWidth(900);

                javafx.scene.control.Label answer3Label = new javafx.scene.control.Label(finalQuestions.get(i).getAnswers().get(2));
                answer3Label.setFont(new Font("Arial", 20));
                answer3Label.setWrapText(true);
                answer3Label.setTextAlignment(TextAlignment.JUSTIFY);
                answer3Label.setMaxWidth(900);

                //label rasp corecte + rasp user
                javafx.scene.control.Label correctAnswers = new javafx.scene.control.Label("Raspunsuri corecte: " + finalQuestions.get(i).getCorrectAnswers().toString());
                correctAnswers.setFont(new Font("Arial", 18));
                javafx.scene.control.Label yourAnswers = new javafx.scene.control.Label("Raspunsurile tale: " + finalAnswers.get(i));
                yourAnswers.setFont(new Font("Arial", 18));


                //box intrebare
                VBox questionBox = new VBox();
                questionBox.setSpacing(0);
                questionBox.setPadding(new Insets(20, 0, 0, 20));
                questionBox.getChildren().add(questionLabel);

                //box raspunsuri
                VBox answerBox = new VBox();
                answerBox.setSpacing(0);
                answerBox.setPadding(new Insets(0, 0, 0, 50));
                answerBox.getChildren().addAll(answer1Label, answer2Label, answer3Label);

                //box rasp corecte+input
                HBox correctAnsBox = new HBox();
                correctAnsBox.setSpacing(50);
                correctAnsBox.setPadding(new Insets(0, 0, 0, 50));
                correctAnsBox.getChildren().addAll(correctAnswers, yourAnswers);

                totalBox.getChildren().addAll(questionBox, answerBox, correctAnsBox);

            }


            //button back
            javafx.scene.control.Button backButton = new javafx.scene.control.Button("BACK");
            backButton.setPrefSize(120, 40);
            backButton.setFont(new Font("Arial", 20));
            backButton.setStyle("-fx-background-color: #BBB9B9");
            backButton.setOnAction(event1 -> primaryStage.setScene(scene)); //merge la pag de sfarsit

            //box back button
            VBox buttonBox = new VBox();
            buttonBox.setSpacing(0);
            buttonBox.setPadding(new Insets(25, 0, 0, 490));
            buttonBox.getChildren().add(backButton);

            //final box
            VBox finalBox = new VBox();
            finalBox.setSpacing(0);
            finalBox.setPadding(new Insets(0, 0, 0, 0));
            finalBox.getChildren().addAll(buttonBox, totalBox);


            //scrollbar
            javafx.scene.control.ScrollBar scrollBar = new javafx.scene.control.ScrollBar();
            ((Group) seeAnswersScene.getRoot()).getChildren().addAll(finalBox, scrollBar);
            scrollBar.setPrefWidth(20);
            scrollBar.setLayoutX(seeAnswersScene.getWidth() - scrollBar.getWidth());
            scrollBar.setMin(0);
            scrollBar.setOrientation(Orientation.VERTICAL);
            scrollBar.setPrefHeight(800);

            scrollBar.setMax(6500);

            scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    finalBox.setLayoutY(-t1.doubleValue());
                }
            });

        });


        //box text
        HBox textBox = new HBox();
        textBox.setSpacing(5);
        textBox.setPadding(new Insets(50, 0, 0, 0));
        textBox.getChildren().add(gata);

        //box rasp
        VBox ansBox = new VBox();
        ansBox.setSpacing(50);
        ansBox.setPadding(new Insets(150, 0, 0, 300));
        ansBox.getChildren().addAll(correctAns, incorrectAns);

        //box buton restart
        VBox restartBox = new VBox();
        restartBox.setSpacing(50);
        restartBox.setPadding(new Insets(150, 0, 0, 300));
        restartBox.getChildren().addAll(restartButton, answButton);

        //box intreg
        VBox finalBox = new VBox();
        finalBox.setSpacing(0);
        finalBox.setPadding(new Insets(20, 0, 0, 100));
        finalBox.getChildren().addAll(textBox, ansBox, restartBox);
        ((Group) scene.getRoot()).getChildren().addAll(finalBox);
    }


    public static void main(String[] args) throws IOException {
        launch(args);

    }
}
