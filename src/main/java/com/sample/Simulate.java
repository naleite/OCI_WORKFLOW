package com.sample;

import java.util.Iterator;
import java.util.Map;

import org.jbpm.process.workitem.bpmn2.ServiceTaskHandler;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.NodeInstance;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.internal.utils.KieHelper;

public class Simulate {
	
	public static void main(String[] args) throws InterruptedException {

	
	/*KieHelper kieHelper = new KieHelper();
	
	// charge le workflow et construit la base de connaissance sur le workflow
	KieBase kieBase = kieHelper
	.addResource(ResourceFactory.newClassPathResource("sample.bpmn"))
	.build();*/

	
	// construit une nouvelle session pour ce workflow
	//KieSession ksession = kieBase.newKieSession();
		KieSession ksession = Manager.getSession();

	// permet à l'utilisateur de controler l'étape d'execution du workflow
	// deux handlers distincts sont définis pour "Human Task" et "Manual Task" ca peut être le même handler pour les deux
	UserTaskItemHandler humanActivitiesSimHandler = new UserTaskItemHandler();
    ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);
	
	ManualTaskItemHandler humanActivitiesSimHandler2 = new ManualTaskItemHandler();
    ksession.getWorkItemManager().registerWorkItemHandler("Manual Task", humanActivitiesSimHandler2);

    
    // démarre une instance du process pour cette session	
    WorkflowProcessInstance processInstance = (WorkflowProcessInstance)  ksession
			.startProcess("com.sample.bpmn.hello");
	
    
    // démarre une deuxième instance du process pour cette session	
    //WorkflowProcessInstance processInstance2 = (WorkflowProcessInstance)  ksession
	//		.startProcess("com.sample.bpmn.hello");
	

    // affichage de l'etat courant 
    System.out.println("Current state? = "+processInstance.getNodeInstances().iterator().next().getNodeName());
    //System.out.println("Current state? = "+processInstance2.getNodeInstances().iterator().next().getNodeName());
     
    // réalisation de la premiere User Task (vérifier sur quelle instance dans le Handler)
    humanActivitiesSimHandler.completeWorkItem(null);

    // affichage de l'etat courant 
    System.out.println("Current state? = "+processInstance.getNodeInstances().iterator().next().getNodeName());
    //System.out.println("Current state? = "+processInstance2.getNodeInstances().iterator().next().getNodeName());
    
        
    // réalisation de la premiere User Task (vérifier sur quelle instance dans le Handler)
    humanActivitiesSimHandler.completeWorkItem(null);
    
    // on vérifie qu'on a bien deux noeuds Manual tasks ici
    if (processInstance.getNodeInstances().size() == 2) {
    	Iterator<NodeInstance> iterator = processInstance.getNodeInstances().iterator();
    	System.out.println("Double state? = "+iterator.next().getNodeName());
    	System.out.println("Double state? = "+iterator.next().getNodeName());
    }

    // réalisation de la premiere Manual Task (vérifier sur quelle task dans le Handler)
    humanActivitiesSimHandler2.completeWorkItem(null);

    // réalisation de la deuxieme Manual Task (vérifier sur quelle task dans le Handler)
    humanActivitiesSimHandler2.completeWorkItem(null);
    
    
    // est-on bien arrivé à la fin du traitement? (oui s'il n'y a plus d'instances de nodes)
    if (processInstance.getNodeInstances().size() != 0) 
    	System.out.println("Current state? = "+processInstance.getNodeInstances().iterator().next().getNodeName());
    else 
    	System.out.println("End of states");
        

    // on termine la session
	ksession.dispose();
	
	}
}

/*class MyAutomaticHumanSimulatorWorkItemHandler implements WorkItemHandler {

    public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
        System.out.println("Map of Parameters = " + workItem.getParameters());
        workItemManager.completeWorkItem(workItem.getId(), null);
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

    }
}
*/

/*class UserTaskItemHandler implements WorkItemHandler {
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
*/

/*class ManualTaskItemHandler implements WorkItemHandler {
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
*/