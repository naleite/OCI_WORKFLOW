package com.ihm;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.NodeInstance;
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
private UserTaskItemHandler humanActivitiesSimHandler;
private ManualTaskItemHandler humanActivitiesSimHandler2;

    @Override
    /**
     * Initialization de l'interface et workflow
     */
	public void initialize(URL location, ResourceBundle resources) {
    	
    	buttons=hbox.getChildren();
    	buttons.add(0, btnCreate);//Creer un button Create. 
    	listview.setItems(data);
    	
    	//Recuper la session
    	KieSession ksession = Manager.getSession();
    	
    	 humanActivitiesSimHandler = new UserTaskItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);
    	//humanActivitiesSimHandler.setCtrl(this);
    	 humanActivitiesSimHandler2 = new ManualTaskItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Manual Task", humanActivitiesSimHandler2);
        //humanActivitiesSimHandler2.setCtrl(this);
        
        //processInstance=Manager.getProcessInstance(false);
    	
    	String instanceName=Manager.getProcessInstance(false).getNodeInstances().iterator().next().getNodeName();
    	
    	for(int i=0;i<buttons.size();i++){
    		buttons.get(i).setDisable(true);
    	}
    	
    	btnCreate.setDisable(false);
    	btnCreate.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
			public void handle(ActionEvent e) {
    	    	 //System.out.println("dfkjfk");
    	    	processInstance=Manager.getProcessInstance(false);
    	    	//data.add(processInstance.getNodeInstances().iterator().next().getNodeName()+" Pid="+processInstance.getId());
    	    	//btnCreate.setDisable(true);
    	    	addToList("New Instance Created!");
    	    	//disableButtonForUserTask("Create",true,false);
    	    	//humanActivitiesSimHandler.completeWorkItem(null);
    	    	updateView(instanceName);
    	    	
    	    	//ksession.getWorkItemManager().completeWorkItem(processInstance.getNodeInstances().iterator().next().getId(), null);
    	    	
    	    }
    	});
    	
    	Iterator<Node> iterBtn=buttons.iterator();
    	while(iterBtn.hasNext()){
    		Button btn=(Button) iterBtn.next();
    		String text=btn.getText();
    		
    	}

    }

    /**
     * Mettre a jour la vue a chaque fois que l'etat avance.
     * @param instanceName nom de tache en fait. Pour comparer avec le button correspondent.
     */
    private void updateView(String instanceName) {
		System.out.println(instanceName);
		
		//Si node Ins ==2 on a deux taches paralleles. 
		if(processInstance.getNodeInstances().size()==2){
			
			Iterator<NodeInstance> nodes=processInstance.getNodeInstances().iterator();
			while(nodes.hasNext()){
				NodeInstance node=(NodeInstance) nodes.next();
				addToList(node.getNodeName());
				disableButtonForManualTask(node.getNodeName(), false, true);
				
				System.out.println("updateView Isname: "+instanceName);
			}
			
		}
		//si ==1 
		else if(processInstance.getNodeInstances().size()==1){
			disableButtonForUserTask(instanceName,false,true);
			addToList(processInstance.getNodeInstances().iterator().next().getNodeName());
			System.out.println("updateView Isname: "+instanceName);
		}
		//Si workflow se termine.
		else{
			Iterator<Node> btns=buttons.iterator();
			while(btns.hasNext()){
				btns.next().setDisable(true);
			}
			data.add("Finish workflow");
			Stage dialog = new Stage();
			dialog.initStyle(StageStyle.UTILITY);
			Scene scene = new Scene(new Group(new Text(25, 25, "All Tasks finished!")));
			dialog.setScene(scene);
			dialog.show();
		}
		//A chaque fois, on va rouler la liste pour le dermier element.
		listview.scrollTo(data.size()-1);
		
	}

    /**
	 * Control l'etat de Button pour UserTask
	 * @param btnText Le text de button
	 * @param disable le mode disable ou active
	 * @param otherBtn Les actions sur autres buttons, true: mettre autres buttons ! @param disable.
	 */
	public void disableButtonForUserTask(String btnText,boolean disable,boolean otherBtn){
    	Iterator<Node> iter=buttons.iterator();
    	while(iter.hasNext()){
    		Button btn=(Button) iter.next();
    		if(btn.getText().equals(btnText)){
    			btn.setDisable(false);
    			attcheButtonOnActionUserTask(btn,humanActivitiesSimHandler);
    			if(otherBtn){
    				break;
    			}
    			
    		}
    		else{
    			btn.setDisable(true);
    		}
    	}
    	//updateView(Manager.getProcessInstance(false).getNodeInstances().iterator().next().getNodeName());
    	
    }
    
	/**
	 * Control l'etat de Button pour ManualTask
	 * @param btnText Le text de button
	 * @param disable le mode disable ou active
	 * @param otherBtn Les actions sur autres buttons, true: mettre autres buttons ! @param disable.
	 */
    public void disableButtonForManualTask(String btnText,boolean disable,boolean otherBtn){
    	Iterator<Node> iter=buttons.iterator();
    	while(iter.hasNext()){
    		Button btn=(Button) iter.next();
    		if(btn.getText().equals(btnText)){
    			btn.setDisable(disable);
    			attcheButtonOnActionManualTask(btn,humanActivitiesSimHandler2);
    			if(otherBtn){
    				break;
    			}
    			
    		}
    		else{
    			btn.setDisable(!disable);
    		}
    	}
    	//updateView(Manager.getProcessInstance(false).getNodeInstances().iterator().next().getNodeName());
    }

    /**
     * Ajouter les information dans listview afin de mettre a jour l'etat de workflow 
     * @param tache Ce qui va afficher dans listview
     */
    public void addToList(String tache){
    	String pid=",pid="+processInstance.getId();
    	data.add(tache+pid);
    }
    
    
    /**
     * Attacher les actions pour un button qui va s'executer pour terminer une tache User
     * @param btn Le button qui va terminer la tache 
     * @param handler UserTaskItemHandler: appeler completeWorkItem(null)
     */

    public void attcheButtonOnActionUserTask(Button btn,UserTaskItemHandler handler){
    	btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				handler.completeWorkItem(null);
				btn.setDisable(true);
				if(processInstance.getNodeInstances().size()==0){
					//addToList("Finish");
					updateView("Finish");
				}
				else{
				//addToList(processInstance.getNodeInstances().iterator().next().getNodeName());
				updateView(Manager.getProcessInstance(false).getNodeInstances().iterator().next().getNodeName());
				}
			}
		});
    }
    
    /**
     * Attacher les actions pour un button qui va s'executer pour terminer une tache Manual
     * @param btn Le button qui va terminer la tache 
     * @param handler ManualTaskItemHandler
     */
    public void attcheButtonOnActionManualTask(Button btn,ManualTaskItemHandler handler){
    	btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				handler.completeWorkItem(null);
				btn.setDisable(true);
				if(processInstance.getNodeInstances().size()==0){
					//addToList("Finish");
					updateView("Finish");
				}
				else{
				//addToList(processInstance.getNodeInstances().iterator().next().getNodeName());
				updateView(Manager.getProcessInstance(false).getNodeInstances().iterator().next().getNodeName());
				}
			}
		});
    }

   }
