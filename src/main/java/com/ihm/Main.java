package com.ihm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        URL uri_fxml=getClass().getClassLoader().getResource("sample.fxml");
        URL uri_bpmn=getClass().getClassLoader().getResource("sample.bpmn");
        try {
            File fxml=new File(uri_fxml.toURI());
            File bpmn=new File(uri_bpmn.toURI());
            Generator gen=new Generator(bpmn,fxml);
            gen.parseBPMN();


        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Parent root = FXMLLoader.load(uri_fxml);
        primaryStage.setTitle(" JBPM");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
