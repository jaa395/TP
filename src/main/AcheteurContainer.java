package main;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class AcheteurContainer{
	
	  public static void main(String[] args) throws Exception {
	  
	  Runtime runtime = Runtime.instance();	  
	  ProfileImpl configuration = new ProfileImpl();
	  configuration.setParameter(ProfileImpl.MAIN_HOST, "localhost");
      AgentContainer agentContainer = runtime.createAgentContainer(configuration);
      AgentController agentcontroller = agentContainer.createNewAgent("ACHETEUR", "agents.AcheteurAgent", new Object[] {"IAD pour débutant"});
      agentContainer.start();
      agentcontroller.start();
   	  
    		  
    		  
      
      
	  
	  }

}
