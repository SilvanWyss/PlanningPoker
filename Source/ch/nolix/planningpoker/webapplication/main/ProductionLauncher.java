package ch.nolix.planningpoker.webapplication.main;

import ch.nolix.system.application.main.SecureServer;

final class ProductionLauncher {
	
	public static void main(String[] args) {
		
		final var secureServer = SecureServer.forHttpsPortAndDomainAndSSLCertificateFromNolixConfiguration();
		
		secureServer.addDefaultApplication(PlanningPokerApplication.withInMemoryNodeDatabase());
	}
	
	private ProductionLauncher() {}
}
