package ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi;

import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.system.application.webapplication.WebClientSession;

public interface IPokerSessionFactory {

  WebClientSession<IPlanningPokerService> createPokerSessionWihtUserId(String userId);
}
