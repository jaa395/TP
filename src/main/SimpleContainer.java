package main;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class SimpleContainer {
	
	public static void main(String[] args) throws Exception {
		  
		  Runtime runtime = Runtime.instance();
		  
		  ProfileImpl configuration = new ProfileImpl();
		  configuration.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		  configuration.setParameter(ProfileImpl.MAIN_PORT, "1099");
		  AgentContainer mainContainer = runtime.createAgentContainer(configuration);
		  mainContainer.start();
		  
		  
	}

}
