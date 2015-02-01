package com.ihm;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Generator {
	 File bpmn, fxml;
	 Document doc_bpmn;
	 Document doc_fxml;
	 StreamResult result;
	 Transformer transformer;
	 DOMSource source;
	 
	 public Generator(File bpmn, File fxml){
		 this.bpmn=bpmn;
		 this.fxml=fxml;
		 
		 DocumentBuilderFactory Factory1 = DocumentBuilderFactory.newInstance();
		 DocumentBuilderFactory Factory2 = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder dBuilder = Factory1.newDocumentBuilder();
			DocumentBuilder rBuilder = Factory2.newDocumentBuilder();
			this.doc_bpmn=dBuilder.parse(this.bpmn);
			this.doc_fxml=rBuilder.parse(this.fxml);
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			 transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {

			e.printStackTrace();
		}
		 source = new DOMSource(doc_fxml);
		 result = new StreamResult(this.fxml);
	 }

	 /**
	  * Parser BPMN pour recuperer les taches qui vont creer des buttons.
	  */
	public void parseBPMN(){
		doc_bpmn.normalizeDocument();
		init_fxml();
		List<NodeList> tasksList=new ArrayList();
		//Choisir les elements de usertask
		NodeList usertasks=doc_bpmn.getElementsByTagName("bpmn2:userTask");
		//Choisir les elements de manualTask
		NodeList manueltasks=doc_bpmn.getElementsByTagName("bpmn2:manualTask");
		tasksList.add(usertasks);
		tasksList.add(manueltasks);
		Iterator<NodeList> iter=tasksList.iterator();
		
		//Pour chaque tache, on recupere le nom de tache et puis on cree un button avec ce nom.
		while(iter.hasNext()){
			NodeList list=iter.next();
		for(int i=0;i<list.getLength();i++){
			Node task=list.item(i) ;
			String nodename=task.getAttributes().getNamedItem("name").getNodeValue();
			System.out.println(nodename);
			//Creer des buttons 
			createButtons(nodename);
		}
		}

	}

	/**
	 * On injecte le fichier fxml, modifie les elements dans le fichier et sauvegarde le fichier.
	 * Dans cette methode, on va recuperer l'element dans HBox et injecter les buttons.
	 */
	private void init_fxml() {
		
		NodeList paneList = doc_fxml.getElementsByTagName("HBox");
		Node btnPane=paneList.item(0).getChildNodes().item(1);

		
		NodeList list=btnPane.getChildNodes();
	

		while(list.getLength()!=0) {
			btnPane.removeChild(list.item(0));
		}


		System.out.println(btnPane.getChildNodes().getLength());
	}

	public void  createButtons(String btnName){
		
		
		 //<Button fx:id="btnCreate" text="Create" />
		 NodeList paneList = doc_fxml.getElementsByTagName("HBox");
		 Node btnPane=paneList.item(0).getChildNodes().item(1);

		 Element newBtn= doc_fxml.createElement("Button");
		 Attr attr_fxid=doc_fxml.createAttribute("fx:id");
		 attr_fxid.setValue(btnName+"btn");

		 Attr attr_text=doc_fxml.createAttribute("text");
		 attr_text.setValue(btnName);


		 newBtn.setAttributeNode(attr_fxid);
		 newBtn.setAttributeNode(attr_text);

		 btnPane.appendChild(newBtn);

		 write();
	 }
	 
	/**
	 * sauvegarder le fichier.
	 */
		private void write(){
			try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	 
	 
	 
}
