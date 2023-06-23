package ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.system.application.webapplication.WebClientSession;

public interface ICreateUserSessionFactory {
	
	WebClientSession<IApplicationContext> createCreateUserSession();
}
