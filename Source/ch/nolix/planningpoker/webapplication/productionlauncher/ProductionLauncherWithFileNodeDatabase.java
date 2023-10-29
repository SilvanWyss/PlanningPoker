package ch.nolix.planningpoker.webapplication.productionlauncher;

import ch.nolix.planningpoker.webapplication.main.PlanningPokerApplication;
import ch.nolix.system.application.main.SecureServer;

final class ProductionLauncherWithFileNodeDatabase {

  public static void main(String[] args) {

    //Creates a SecureServer.
    final var secureServer = SecureServer.forHttpsPortAndDomainAndSSLCertificateFromNolixConfiguration();

    //Creates a PlanningPokerApplication.
    final var databaseFilePath = "planning_poker.spec";
    final var planningPokerApplication = PlanningPokerApplication.withFileNodeDatabase(databaseFilePath);

    //Adds the PlanningPokerApplication as default application to the SecureServer.
    secureServer.addDefaultApplication(planningPokerApplication);
  }

  private ProductionLauncherWithFileNodeDatabase() {
  }
}
