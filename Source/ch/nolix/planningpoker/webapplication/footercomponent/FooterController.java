package ch.nolix.planningpoker.webapplication.footercomponent;

import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.Controller;
import ch.nolix.system.application.webapplication.WebClientSession;

final class FooterController extends Controller<IPlanningPokerContext> {
	
	protected FooterController(final WebClientSession<IPlanningPokerContext> session) {
		super(session);
	}
}
