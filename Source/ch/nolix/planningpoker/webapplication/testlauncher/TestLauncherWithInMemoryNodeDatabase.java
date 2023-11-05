package ch.nolix.planningpoker.webapplication.testlauncher;

import ch.nolix.core.environment.localcomputer.ShellProvider;
import ch.nolix.planningpoker.webapplication.main.PlanningPokerApplication;
import ch.nolix.system.application.main.Server;

final class TestLauncherWithInMemoryNodeDatabase {

  private TestLauncherWithInMemoryNodeDatabase() {
  }

  public static void main(String[] args) {
  
    //Creates a Server.
    final var server = Server.forHttpPort();
  
    //Creates a PlanningPokerApplication.
    final var planningPokerApplication = PlanningPokerApplication.withInMemoryNodeDatabase();
  
    //Adds the PlanningPokerApplication as default application to the Server.
    server.addDefaultApplication(planningPokerApplication);
  
    //Starts a web browser that will connect to the Server.
    ShellProvider.startDefaultWebBrowserOpeningLoopBackAddress();
  }
}
