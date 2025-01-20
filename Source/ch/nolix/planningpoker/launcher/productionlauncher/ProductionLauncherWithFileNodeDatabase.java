package ch.nolix.planningpoker.launcher.productionlauncher;

import ch.nolix.planningpoker.frontend.main.PlanningPokerApplication;
import ch.nolix.system.application.main.SslServer;

final class ProductionLauncherWithFileNodeDatabase {

  private ProductionLauncherWithFileNodeDatabase() {
  }

  public static void main(String[] args) {

    //Creates a SecureServer.
    final var sslServer = SslServer.forHttpsPortAndDomainAndSSLCertificateFromNolixConfiguration();

    //Creates a PlanningPokerApplication.
    final var databaseFilePath = "planning_poker.spec";
    final var planningPokerApplication = PlanningPokerApplication.withFileNodeDatabase(databaseFilePath);

    //Adds the PlanningPokerApplication as default application to the SecureServer.
    sslServer.addDefaultApplication(planningPokerApplication);
  }
}
