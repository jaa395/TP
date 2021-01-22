package agents;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class VendeurAgent extends GuiAgent{
	protected VendeurGui gui ;  
	private String livre;
	protected AID[] vendeurs ; 
	
	
	
	 @Override
		protected void setup ()
		{   
			//coupler l'acheteur avec son interface graphique
			if(getArguments().length==1)
			{
				gui = (VendeurGui) getArguments()[0];
				gui.vendeurAgent=this;
				
			}
			
			
			
			
			
			
			
			Object[] params = getArguments();
		      livre=params[0].toString();
		    System.out.println("****************");
			System.out.println("deploiement de l'agent "+getAID().getName());
			System.out.println("je vais tenter d'acheter le livre "+ livre  );
			System.out.println("****************");
	        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
			
	        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
			addBehaviour(parallelBehaviour);
			
			parallelBehaviour.addSubBehaviour(new TickerBehaviour(this, 5000) {
				
				@Override
				protected void onTick() {
				 
					DFAgentDescription dfAgentDescription= new DFAgentDescription();
					ServiceDescription serviceDescription= new ServiceDescription();
					serviceDescription.setType("transaction");
					serviceDescription.setName("vente-livre");
					dfAgentDescription.addServices(serviceDescription);
					
					try {
						DFAgentDescription[] results = DFService.search(myAgent, dfAgentDescription);
						vendeurs= new AID[results.length];
						for (int i=0; i<vendeurs.length; i++) {
							vendeurs[i]=results[i].getName(); 
							
						}
					} catch (FIPAException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
	
				}
			 
				
				
			});
			
			
			parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
				@Override
				public void action() {
					// TODO Auto-generated method stub
					DFAgentDescription agentDescription=new DFAgentDescription();
					agentDescription.setName(getAID());
					ServiceDescription serviceDescription=new ServiceDescription();
					serviceDescription.setType("transaction");
					serviceDescription.setName("vente-livre");
					agentDescription.addServices(serviceDescription);
					try {
						DFService.register(myAgent, agentDescription);
					} catch (FIPAException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} 
			});
			
			parallelBehaviour.addSubBehaviour(new CyclicBehaviour()
					{
				@Override
				public void action() {
					MessageTemplate messageTemplate=MessageTemplate.and(MessageTemplate.MatchOntology("vente-livre"), MessageTemplate.MatchPerformative(ACLMessage.CFP));
					
					ACLMessage aclMessage=receive(messageTemplate); 
					if(aclMessage!=null) {
						
						gui.logMessage(aclMessage);
						switch (aclMessage.getPerformative())
						{
						case ACLMessage.CFP:
							ACLMessage reply=aclMessage.createReply();
							reply.setPerformative(ACLMessage.PROPOSE);
							reply.setContent(String.valueOf(500+new Random().nextInt(1000)));
							send(reply);
							break;
						case ACLMessage.ACCEPT_PROPOSAL:
							ACLMessage aclMessage2=aclMessage.createReply();
							aclMessage2.setPerformative(ACLMessage.AGREE);
							send(aclMessage2);
							break;
						default:
							break;
							
				
					      
						}
						/*System.out.println("------------------");
						System.out.println("réception de message");
						System.out.println("Le contenu de ce message: "+aclMessage.getContent());
						System.out.println("Acte de Communication : "+ACLMessage.getPerformative(aclMessage.getPerformative()));
						System.out.println("Langue utilisée:"+aclMessage.getLanguage());
						System.out.println("ontologie: "+aclMessage.getOntology());
						System.out.println("emmetteur du message :"+aclMessage.getSender());
						System.out.println("------------------");*/
						//ACLMessage reply = aclMessage.createReply();
						//reply.setContent("ok pour "+aclMessage.getContent());
						//send(reply);
					
					//ACLMessage aclMessage2=new ACLMessage(ACLMessage.INFORM);
					//aclMessage2.addReceiver(new AID ("vendeur1", AID.ISLOCALNAME));
					//aclMessage2.addReceiver(aclMessage.getSender());
					//aclMessage2.setContent("prix=900");
					//aclMessage2.setOntology(aclMessage.getOntology());
					//aclMessage.setLanguage(aclMessage.getLanguage());
					//ACLMessage aclMessage2=aclMessage.createReply();
					//aclMessage2.setContent("prix=800");
					//send(aclMessage2);
					}
					else {
						block();
					}
					}
				
					});
			}
	 
	
	 
	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	protected void onGuiEvent(GuiEvent event) {
		// TODO Auto-generated method stub
		
	}

}
