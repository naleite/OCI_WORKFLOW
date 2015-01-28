package com.sample;

import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.ihm.Controller;

public class ManualTaskItemHandler implements WorkItemHandler {
    private WorkItemManager workItemManager;
    private long workItemId;
    /*private Controller ctrl;

    public Controller getCtrl() {
		return ctrl;
	}

	public void setCtrl(Controller ctrl) {
		this.ctrl = ctrl;
	}*/

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

