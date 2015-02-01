package com.sample;

import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.ihm.Controller;

public class UserTaskItemHandler implements WorkItemHandler {
    private WorkItemManager workItemManager;
    private long workItemId;
    /*private Controller ctrl;
    
    

    public Controller getCtrl() {
		return ctrl;
	}

	public void setCtrl(Controller ctrl) {
		this.ctrl = ctrl;
	}
*/
	public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
        this.workItemId = workItem.getId();
        this.workItemManager = workItemManager;
        System.out.println("UserTask:Execute: Work process instance = " + workItem.getProcessInstanceId());
        System.out.println("UserTask:Execute: Map of Parameters = " + workItem.getParameters());
        System.out.println("UserTask:Execute: Work Process id = " + workItemId);
        System.out.println("UserTask:Execute: Name = "+workItem.getParameters().get("NodeName") );
        //ctrl.disableButton((String) workItem.getParameters().get("NodeName"), true);
                
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

    }

    public void completeWorkItem(Map<String, Object> parameters) {
    	System.out.println("UserTask:Completed");
        this.workItemManager.completeWorkItem(this.workItemId, parameters);

    }

}
