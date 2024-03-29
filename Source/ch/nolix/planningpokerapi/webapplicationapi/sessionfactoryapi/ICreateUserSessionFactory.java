package ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi;

import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.webapplication.WebClientSession;

public interface ICreateUserSessionFactory {

  WebClientSession<IPlanningPokerContext> createCreateUserSession();
}
