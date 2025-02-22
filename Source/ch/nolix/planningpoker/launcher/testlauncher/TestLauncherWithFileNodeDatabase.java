package ch.nolix.planningpoker.launcher.testlauncher;

import ch.nolix.core.environment.localcomputer.ShellProvider;
import ch.nolix.planningpoker.frontend.main.PlanningPokerApplication;
import ch.nolix.system.application.main.Server;

final class TestLauncherWithFileNodeDatabase {

  private TestLauncherWithFileNodeDatabase() {
  }

  public static void main(String[] args) {

    //Creates a Server.
    final var server = Server.forHttpPort();

    //Creates a PlanningPokerApplication.
    final var databaseFilePath = "planning_poker.spec";
    final var planningPokerApplication = PlanningPokerApplication.withFileNodeDatabase(databaseFilePath);

    //Adds the PlanningPokerApplication as default application to the Server.
    server.addDefaultApplication(planningPokerApplication);

    //Starts a default web browser that will connect to the Server.
    ShellProvider.startDefaultWebBrowserOpeningLoopBackAddress();
  }
}
