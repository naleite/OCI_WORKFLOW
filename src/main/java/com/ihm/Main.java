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


    	//Charger le fichier fxml
        URL uri_fxml=getClass().getClassLoader().getResource("sample.fxml");
        
        //Charger le fichier bpmn
        URL uri_bpmn=getClass().getClassLoader().getResource("sample.bpmn");
        try {
            File fxml=new File(uri_fxml.toURI());
            File bpmn=new File(uri_bpmn.toURI());
            
            //Instancier un generateur pour generer l'interface
            Generator gen=new Generator(bpmn,fxml);
            //Generer l'interface en fonction du fichier sample.bpmn en utilisant le squelette de fxml
            gen.parseBPMN();


        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Parent root = FXMLLoader.load(uri_fxml);
        primaryStage.setTitle("JBPM Workflow");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
