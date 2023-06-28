package ch.nolix.planningpoker.webapplication.launcher;

import ch.nolix.planningpoker.webapplication.main.PlanningPokerApplication;
import ch.nolix.system.application.main.SecureServer;

final class ProductionLauncherWithInMemoryNodeDatabase {
	
	public static void main(String[] args) {
		
		final var secureServer = SecureServer.forHttpsPortAndDomainAndSSLCertificateFromNolixConfiguration();
		
		final var planningPokerApplication = PlanningPokerApplication.withInMemoryNodeDatabase();
		
		secureServer.addDefaultApplication(planningPokerApplication);
	}
	
	private ProductionLauncherWithInMemoryNodeDatabase() {}
}
