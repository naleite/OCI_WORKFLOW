package com.ihm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.process.WorkflowProcessInstance;

import com.sample.Manager;


public class Controller implements Initializable {

@FXML
private ListView listview;

@FXML
private HBox hbox;

private List<Node> buttons;
private ObservableList<String> data = FXCollections.observableArrayList();
private WorkflowProcessInstance processInstance;
    public void initialize(URL location, ResourceBundle resources) {
    	buttons=hbox.getChildren();
    	listview.setItems(data);
    	KieSession ksession = Manager.getSession();
    	
    	UserTaskItemHandler humanActivitiesSimHandler = new UserTaskItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);
    	
    	ManualTaskItemHandler humanActivitiesSimHandler2 = new ManualTaskItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Manual Task", humanActivitiesSimHandler2);
    	
        processInstance=Manager.getProcessInstance();
    	data.add(processInstance.getNodeInstances().iterator().next().getNodeName());
    	((ButtonBase) buttons.get(0)).setOnAction(new EventHandler<ActionEvent>() {
    	    public void handle(ActionEvent e) {
    	    	 System.out.println("dfkjfk");
    	    }
    	});
    }

    class MyAutomaticHumanSimulatorWorkItemHandler implements WorkItemHandler {

        public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
            System.out.println("Map of Parameters = " + workItem.getParameters());
            workItemManager.completeWorkItem(workItem.getId(), null);
        }

        public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

        }
    }

    class UserTaskItemHandler implements WorkItemHandler {
        private WorkItemManager workItemManager;
        private long workItemId;

        public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
            this.workItemId = workItem.getId();
            this.workItemManager = workItemManager;
            System.out.println("UserTask:Execute: Work process instance = " + workItem.getProcessInstanceId());
            System.out.println("UserTask:Execute: Map of Parameters = " + workItem.getParameters());
            
        }

        public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

        }

        public void completeWorkItem(Map<String, Object> parameters) {
        	System.out.println("UserTask:Completed");
            this.workItemManager.completeWorkItem(this.workItemId, parameters);

        }

    }


    class ManualTaskItemHandler implements WorkItemHandler {
        private WorkItemManager workItemManager;
        private long workItemId;

        public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
            this.workItemId = workItem.getId();
            this.workItemManager = workItemManager;
            System.out.println("ManualTask:Execute: Work process instance = " + workItem.getProcessInstanceId());
            System.out.println("ManualTask:Execute: Map of Parameters = " + workItem.getParameters());
            
        }

        public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

        }

        public void completeWorkItem(Map<String, Object> parameters) {
        	System.out.println("ManualTask:Completed");
            this.workItemManager.completeWorkItem(this.workItemId, parameters);

        }


    }
}
