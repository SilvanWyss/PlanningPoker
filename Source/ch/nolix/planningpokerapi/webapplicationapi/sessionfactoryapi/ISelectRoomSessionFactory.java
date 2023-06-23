package ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi;

import ch.nolix.planningpokerapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.webapplication.WebClientSession;

public interface ISelectRoomSessionFactory {
	
	 WebClientSession<IPlanningPokerContext> createSelectRoomSessionWihtUserId(String userId);
}
