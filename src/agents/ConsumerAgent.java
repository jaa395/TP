package agents;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.application.Platform;
import main.ConsumerContainer;
public class ConsumerAgent extends GuiAgent{
	private transient ConsumerContainer gui;
protected void setup()
{
System.out.println("******************");
/*System.out.println("Agent Initialisation .........." + this.getAID().getName());
if(this.getArguments().length == 1)
	System.out.println("Je vais tenter d'acheter le livre" + getArguments()[0]);
System.out.println("******************");*/
if(getArguments().length==1)
	{gui = (ConsumerContainer) getArguments()[0];
	//association bidirectionnelle
	gui.setConsumerAgent(this);
	}
ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
addBehaviour(parallelBehaviour);

parallelBehaviour.addSubBehaviour(new CyclicBehaviour()
{
@Override
public void action() {
MessageTemplate messageTemplate=MessageTemplate.and(MessageTemplate.MatchOntology("vente-livre"), MessageTemplate.MatchPerformative(ACLMessage.CFP));

ACLMessage aclMessage=receive(messageTemplate); 
if(aclMessage!=null) {
	switch(aclMessage.getPerformative()) {
	case ACLMessage.CONFIRM:
		gui.logMessage(aclMessage);
		break;
	default:
		break;
	}
	
	
/*	System.out.println("**********************");
	System.out.println("réception de message");
	System.out.println(aclMessage.getContent());
	System.out.println(aclMessage.getSender().getName());

	System.out.println(aclMessage.getPerformative());
	System.out.println(aclMessage.getLanguage());
	System.out.println(aclMessage.getOntology());
*/	
	//send a reply
	ACLMessage reply = aclMessage.createReply();
	reply.setContent("Weekend libre oh non ");
	send(reply);
}
else block();}
});

	System.out.println("**********************");
}
@Override
protected void beforeMove()
{
	System.out.println("****************");
	System.out.println("Avant migration de l'agent"+getAID().getLocalName());
	System.out.println("****************");
}
@Override
protected void afterMove ()
{
	System.out.println("****************");
	System.out.println("Après migration de l'agent"+getAID().getLocalName());
	System.out.println("****************");
}
@Override
protected void takeDown ()
{
	System.out.println("****************");
	System.out.println("Je suis en train de mourir...");
	System.out.println("****************");
}
@Override
public void onGuiEvent(GuiEvent params)
{
	if(params.getType()==1)
	{
		String livre = (String) params.getParameter(0);
		ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
		aclMessage.setContent(livre);
		//remarque : RMA est celui qui permet d'afficher l'interface graphique
		aclMessage.addReceiver(new AID("ACHETEUR",AID.ISLOCALNAME));
		send(aclMessage);
	}

}

}
