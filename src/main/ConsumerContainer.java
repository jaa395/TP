package main;



import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import agents.ConsumerAgent;
import jade.core.AID;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.geometry.Insets;

public class ConsumerContainer extends Application{
	
	protected ConsumerAgent consumerAgent;
	ObservableList<String> observableList;
	public static void main(String[] args) throws Exception {
		  //demarrer l interface graphique
			launch(args);
			
		
	
		  
	}
	public ConsumerAgent getConsumerAgent() {
		return consumerAgent;
	}
	public void setConsumerAgent(ConsumerAgent consumerAgent) {
		this.consumerAgent = consumerAgent;
	}
	public void startContainer() throws Exception
	{
  /*Runtime runtime = Runtime.instance();
		  
		  ProfileImpl profileImpl = new ProfileImpl();
		  profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		
		//profileImpl.setParameter(ProfileImpl.MAIN_PORT, "1099");
		 AgentContainer container = (AgentContainer) runtime.createAgentContainer(profileImpl);
		  AgentController agentcontroller;
	
			agentcontroller = ((ContainerController) container).createNewAgent("Consumer", "agents.ConsumerAgent", new Object[] {"XML"});
		
		     agentcontroller.start();
		     ((AgentController) container).start();*/
		 Runtime runtime = Runtime.instance();	  
		  ProfileImpl configuration = new ProfileImpl();
		  configuration.setParameter(ProfileImpl.MAIN_HOST, "localhost");
	      AgentContainer agentContainer = runtime.createAgentContainer(configuration);
	      AgentController agentcontroller = agentContainer.createNewAgent("Consumer", "agents.ConsumerAgent", new Object[] {"IAD pour débutant"});
	      agentContainer.start();
	      agentcontroller.start();
		}
	      
	public void start(Stage primaryStage) throws Exception
	{
		startContainer();
		primaryStage.setTitle("consommateur");
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10));
		hBox.setSpacing(10);
		Label label = new Label("Livre: ");
		TextField textFieldLivre = new TextField();
		Button buttonAcheter = new Button("Acheter");
		hBox.getChildren().addAll(label,textFieldLivre,buttonAcheter);
		observableList = FXCollections.emptyObservableList();
		VBox vBox = new VBox();
		ListView<String> listViewMessages = new ListView<String>(observableList);
		hBox.getChildren().add(vBox);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(hBox); 
		borderPane.setCenter(vBox);
		
		
		Scene scene = new Scene(borderPane,600,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		buttonAcheter.setOnAction(evt->{
			String livre = textFieldLivre.getText();
			observableList.add(livre);
			GuiEvent event = new GuiEvent(this,1);
			consumerAgent.onGuiEvent(event);
		});
		
	}
	public void onGuiEvent(GuiEvent params)
	{
		
		if(params.getType()==1)
		{
			String livre = params.getParameter(0).toString();
			ACLMessage aclmessage = new ACLMessage(ACLMessage.REQUEST);
			aclmessage.setContent(livre);
			aclmessage.addReceiver(new AID("ACHETEUR",AID.ISLOCALNAME));
		
			consumerAgent.send(aclmessage);
			
		}
			
	}
	public void logMessage(ACLMessage aclMessage)
	{
Platform.runLater(()->{
	observableList.add(aclMessage.getContent()
			+", "+aclMessage.getSender().getName()
		     );
});

	}

}
