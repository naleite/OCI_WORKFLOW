package com.ihm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkflowProcessInstance;

import com.sample.Manager;
import com.sample.ManualTaskItemHandler;
import com.sample.UserTaskItemHandler;


public class Controller implements Initializable {

@FXML
private ListView listview;

@FXML
private HBox hbox;

@FXML
private Button btnCreate;

private List<Node> buttons;
private ObservableList<String> data = FXCollections.observableArrayList();
private WorkflowProcessInstance processInstance;
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	buttons=hbox.getChildren();
    	buttons.add(0, btnCreate);
    	listview.setItems(data);
    	KieSession ksession = Manager.getSession();
    	
    	UserTaskItemHandler humanActivitiesSimHandler = new UserTaskItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);
    	//humanActivitiesSimHandler.setCtrl(this);
    	ManualTaskItemHandler humanActivitiesSimHandler2 = new ManualTaskItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Manual Task", humanActivitiesSimHandler2);
        //humanActivitiesSimHandler2.setCtrl(this);
        
    	
    	btnCreate.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
			public void handle(ActionEvent e) {
    	    	 //System.out.println("dfkjfk");
    	    	processInstance=Manager.getProcessInstance();
    	    	//data.add(processInstance.getNodeInstances().iterator().next().getNodeName()+" Pid="+processInstance.getId());
    	    	//btnCreate.setDisable(true);
    	    	addToList(processInstance.getNodeInstances().iterator().next().getNodeName());
    	    	disableButton("Create",true);
    	    	//humanActivitiesSimHandler.completeWorkItem(null);
    	    	humanActivitiesSimHandler.completeWorkItem(null);
    	    }
    	});

    }

    public void disableButton(String btnText,boolean disable){
    	Iterator<Node> iter=buttons.iterator();
    	while(iter.hasNext()){
    		Button btn=(Button) iter.next();
    		if(btn.getText().equals(btnText)){
    			btn.setDisable(disable);
    			
    		}
    		else{
    			btn.setDisable(!disable);
    		}
    	}
    }
    
    public void addToList(String tache){
    	String pid=",pid="+processInstance.getId();
    	data.add(tache+pid);
    }
    
    public void attcheButtonOnAction(Button btn,WorkItemHandler handler){
    	btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				
			}
		});
    }

   }
