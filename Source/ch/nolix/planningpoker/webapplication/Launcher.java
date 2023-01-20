package ch.nolix.planningpoker.webapplication;

import ch.nolix.system.application.main.Server;

public final class Launcher {
	
	public static void main(String[] args) {
		Server.forDefaultPort().addDefaultApplication(PlanningPokerApplication.withTemporaryNodeDatabase());
	}
	
	private Launcher() {}
}
