package agents;
import jade.core.AgentContainer;

import jade.core.ProfileImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;

public class AcheteurGui extends Application{
	protected AcheteurAgent acheteurAgent;
	protected ObservableList<String> observableList;
public static void main(String[] args)
{
launch(args);
}
@Override
public void start(Stage primaryStage) throws Exception
{
startContainer();	
primaryStage.setTitle("Acheteur");
//affichage de message pr la simulation
//definition de l'affichage => gestion des elements graphiques
BorderPane borderPane = new BorderPane();
VBox vBox = new VBox();
observableList = FXCollections.observableArrayList();
ListView<String> listView = new ListView<String>(observableList);
vBox.getChildren().add(listView);
borderPane.setCenter(vBox);
Scene scene = new Scene(borderPane,300,400);
primaryStage.setScene(scene);
primaryStage.show();



}
private void startContainer() throws Exception{
	Runtime runtime = Runtime.instance();
	ProfileImpl profileImpl = new ProfileImpl();
	profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
	jade.wrapper.AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
	AgentController agentController = agentContainer.createNewAgent("ACHETEUR","agents.AcheteurAgent",new Object[]{this});
	agentController.start();
	agentContainer.start();
	
	
}
public void logMessage(ACLMessage aclmessage)
{
	Platform.runLater(()->{
		observableList.add(aclMessage.getContent()
				+", "+aclMessage.getSender().getName()
			     );
	
	});
	}
}
