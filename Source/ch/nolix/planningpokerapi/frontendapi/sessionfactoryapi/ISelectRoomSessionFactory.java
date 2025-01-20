package ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi;

import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.system.application.webapplication.WebClientSession;

public interface ISelectRoomSessionFactory {

  WebClientSession<IPlanningPokerService> createSelectRoomSessionWihtUserId(String userId);
}
