package ch.nolix.planningpoker.webapplication.launcher;

import ch.nolix.core.environment.localcomputer.ShellProvider;
import ch.nolix.planningpoker.webapplication.main.PlanningPokerApplication;
import ch.nolix.system.application.main.Server;

final class TestLauncherWithInMemoryNodeDatabase {
	
	public static void main(String[] args) {
		
		final var server = Server.forHttpPort();
		
		final var planningPokerApplication = PlanningPokerApplication.withInMemoryNodeDatabase();
		
		server.addDefaultApplication(planningPokerApplication);
		
		ShellProvider.startDefaultWebBrowserOpeningLoopBackAddress();
	}
	
	private TestLauncherWithInMemoryNodeDatabase() {}
}
