package ch.nolix.planningpoker.webapplication.productionlauncher;

import ch.nolix.planningpoker.webapplication.main.PlanningPokerApplication;
import ch.nolix.system.application.main.SslServer;

final class ProductionLauncherWithInMemoryNodeDatabase {

  private ProductionLauncherWithInMemoryNodeDatabase() {
  }

  public static void main(String[] args) {

    //Creates a SecureServer.
    final var sslServer = SslServer.forHttpsPortAndDomainAndSSLCertificateFromNolixConfiguration();

    //Creates a PlanningPokerApplication.
    final var planningPokerApplication = PlanningPokerApplication.withInMemoryNodeDatabase();

    //Adds the PlanningPokerApplication as default application to the SecureServer.
    sslServer.addDefaultApplication(planningPokerApplication);
  }
}
