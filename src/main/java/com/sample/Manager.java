package com.sample;

import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

public class Manager {
	static KieSession ksession;
	static WorkflowProcessInstance processInstance;
	public Manager(){
	
	}
	
	public static KieSession getSession(){
		if (ksession==null){
			KieHelper kieHelper = new KieHelper();
			KieBase kieBase = kieHelper
					.addResource(ResourceFactory.newClassPathResource("sample.bpmn"))
					.build();
				
					
					// construit une nouvelle session pour ce workflow
					ksession = kieBase.newKieSession();
		}
		return ksession;
	}
	
	public static void disposeSession(){
		ksession.dispose();
	}
	
	public static WorkflowProcessInstance getProcessInstance(){
		if(processInstance==null){
			processInstance=(WorkflowProcessInstance) getSession().startProcess("com.sample.bpmn.hello");
		}
		return processInstance;
	}
}
