package ch.nolix.planningpoker.webapplication.main;

import ch.nolix.core.environment.localcomputer.ShellProvider;
import ch.nolix.system.application.main.Server;

final class Launcher {
	
	public static void main(String[] args) {
		
		final var server = Server.forHttpPort();
		
		server.addDefaultApplication(PlanningPokerApplication.withTemporaryNodeDatabase());
		
		ShellProvider.startDefaultWebBrowserOpeningLoopBackAddress();
	}
	
	private Launcher() {}
}
