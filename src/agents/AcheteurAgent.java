package agents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import FIPA.stringsHelper;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;




public class AcheteurAgent extends GuiAgent {
	//reference de l'interface graphique
	protected AcheteurGui gui;
	private String livre;
    protected AID[] vendeurs;
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("serial")
	//@Override
	//protected void onGuiEvent1(GuiEvent arg0) 
	//{}
	@Override
	protected void setup ()
	{   
		//coupler l'acheteur avec son interface graphique
		if(getArguments().length==1)
		{
			gui = (AcheteurGui) getArguments()[0];
			gui.acheteurAgent=this;
			
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
		
		
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour()
				{
			private int counter=0;
			private List<ACLMessage> replies=new ArrayList<ACLMessage>();
			
			@Override
			public void action() {
			
				MessageTemplate messageTemplate=MessageTemplate.and(MessageTemplate.MatchOntology("vente-livre"), MessageTemplate.MatchPerformative(ACLMessage.CFP));
				
				ACLMessage aclMessage=receive(messageTemplate); 
				if(aclMessage!=null) {
					switch (aclMessage.getPerformative()) {
					case ACLMessage.REQUEST:
						String livre=aclMessage.getContent();
						ACLMessage aclMessage2=new ACLMessage(ACLMessage.CFP);
						aclMessage2.setContent(livre);
						for(AID aid:vendeurs)
						{
							aclMessage2.addReceiver(aid);
						}
						send(aclMessage2);
						
						break;
                    case ACLMessage.PROPOSE:
					    ++counter;
					    replies.add(aclMessage);
					    if(counter==vendeurs.length) {
					    	ACLMessage meilleurOffre=replies.get(0);
						  double mini=Double.parseDouble(meilleurOffre.getContent());
						  
						  for (ACLMessage offre:replies) {
							  double price=Double.parseDouble(offre.getContent());
							  if(price<mini) {
								  meilleurOffre=offre;
								  mini=price;
							  }
								  
						  }
						  ACLMessage aclMessageAccept=meuilleurOffre.createReply();
						  aclMessage.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
						  send(aclMessageAccept);
						  
					    }
						break;
                     case ACLMessage.AGREE:
                    	 ACLMessage aclMessage3=new ACLMessage(ACLMessage.CONFIRM);
                    	 aclMessage3.addReceiver(new AID("Consumer",AID.ISLOCALNAME));
                    	 aclMessage3.setContent(aclMessage.getContent());
                    	 send(aclMessage3);
                    	 
                    	 break;
                     case ACLMessage.REFUSE:
                    	 
						
						break;
					default:
						break;
					}
					String  livre=aclMessage.getContent();
					gui.logMessage(aclMessage);
					/*System.out.println("------------------");
					System.out.println("réception de message");
					System.out.println("Le contenu de ce message: "+aclMessage.getContent());
					System.out.println("Acte de Communication : "+ACLMessage.getPerformative(aclMessage.getPerformative()));
					System.out.println("Langue utilisée:"+aclMessage.getLanguage());
					System.out.println("ontologie: "+aclMessage.getOntology());
					System.out.println("emmetteur du message :"+aclMessage.getSender());
					System.out.println("------------------");*/
					ACLMessage reply = aclMessage.createReply();
					reply.setContent("ok pour "+aclMessage.getContent());
					send(reply);
					ACLMessage aclMessage3 = new ACLMessage(ACLMessage.CFP);
					aclMessage3.setContent(livre);
					aclMessage3.addReceiver(new AID("VENDEUR", AID.ISLOCALNAME) );
					send(aclMessage3);
				
				//ACLMessage aclMessage2=new ACLMessage(ACLMessage.INFORM);
				//aclMessage2.addReceiver(new AID ("vendeur1", AID.ISLOCALNAME));
				//aclMessage2.addReceiver(aclMessage.getSender());
				//aclMessage2.setContent("prix=900");
				//aclMessage2.setOntology(aclMessage.getOntology());
				//aclMessage.setLanguage(aclMessage.getLanguage());
				ACLMessage aclMessage2=aclMessage.createReply();
				aclMessage2.setContent("prix=800");
				send(aclMessage2);
				}
				else {
					block();
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
		/*
		parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,2000)
        		{
             private int counter;
             @Override
             protected void onTick() {
            	 System.out.println("Tick"+(++counter));
             }
        		});
		
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour()
			
		{
			private int counter=1;	
			   @Override
	             public void action() {
	            	 System.out.println("Tentative numéro"+(++counter));
			   }
				});
				/*
 
		/*
		 try {
		 
		addBehaviour( new WakerBehaviour(this,dateFormat.parse("18/01/2020:12:10:00"))
				{
			    @Override
			    protected void onWake() {
			    	System.out.println("Tache planifiée");
			    }
				});
		} catch(ParseException e)
		{
			e.printStackTrace();
		}
		/*
		 addBehaviour(new CyclicBehaviour()
		 
				{
				private int counter=1;
		@Override
		public void action() {
		System.out.println("Tentative numéro"+(++counter));
		}
			
	});
	/*
		*/
		/*
		addBehaviour(new OneShotBehaviour() 
		{
			
			
			

			@Override
			public void action() {
				System.out.println(""); 
			}
		});
		*/
		
		
		
		
		/*
		 * addBehaviour(new Behaviour() {
		 
             private int counter=0;
			
			private static final long serialVersionUID = 1L;

              
			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				if(counter==10) return true; 
				else return false;
			}	
        	
			@Override
			public void action() {
				System.out.println("Tentative d'acheter le livre+" +livre+" =>" +counter);
				++counter;
			}
			
			@Override
			public void onStart() {
				System.out.println("Début de la tache");
			
			}
			
			@Override
			public int onEnd() {
				System.out.println("Fin de la tache");
			
                return counter;
             }
    
        });
        */
	} 

	@Override
	protected void beforeMove ()
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
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
