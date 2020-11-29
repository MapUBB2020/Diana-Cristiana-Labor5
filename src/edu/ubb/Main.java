package edu.ubb;

import com.sun.javafx.binding.StringFormatter;
import edu.ubb.controller.Controller;
import edu.ubb.model.Question;
import edu.ubb.repository.FileRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));
        primaryStage.setTitle("Chestionar Auto");
        primaryStage.setScene(new Scene(root, 1100, 800));

        Scene scene = new Scene(new Group(), 1100, 800);
        Scene scene2 = new Scene(new Group(), 1100, 800);

        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("CHESTIONAR AUTO");
        titleLabel.setFont(new Font("Arial", 56));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#454545"));

        javafx.scene.control.Button startButton = new javafx.scene.control.Button("START");
        startButton.setPrefSize(170,70);
        startButton.setFont(new Font("Arial", 20));
        startButton.setStyle("-fx-background-color: #9CEA7E; -fx-border-width: 5px; -fx-border-color: #88CD6D ");
        startButton.setOnAction(e -> primaryStage.setScene(scene2));


        VBox myVBox = new VBox();
        myVBox.setSpacing(225);
        myVBox.setPadding(new Insets(150, 0, 0, 275));
        myVBox.setAlignment(Pos.CENTER);
        myVBox.getChildren().addAll(titleLabel, startButton);
        ((Group) scene.getRoot()).getChildren().addAll(myVBox);


        //scene2

        Date date = java.util.Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        javafx.scene.control.Label label = new javafx.scene.control.Label(strDate);
        titleLabel.setFont(new Font("Arial", 56));

        //buton trimite
        javafx.scene.control.Button nextButton = new javafx.scene.control.Button("TRIMITE");
        nextButton.setPrefSize(170,60);
        nextButton.setFont(new Font("Arial", 20));
        nextButton.setStyle("-fx-background-color: #9CEA7E; -fx-border-width: 5px; -fx-border-color: #88CD6D ");
        nextButton.setOnAction(e -> primaryStage.setScene(scene2));

        //buton sterge
        javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("STERGE");
        deleteButton.setPrefSize(170,60);
        deleteButton.setFont(new Font("Arial", 20));
        deleteButton.setStyle("-fx-background-color: #FD7D7D; -fx-border-width: 5px; -fx-border-color: #DF7070 ");

        HBox myHBox = new HBox();
        myHBox.setSpacing(150);
        myHBox.setPadding(new Insets(650, 0, 0, 310));
        myHBox.getChildren().addAll(deleteButton, nextButton, label);
        ((Group) scene2.getRoot()).getChildren().addAll(myHBox);





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
