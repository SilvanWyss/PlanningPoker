package ch.nolix.planningpoker.webapplication.main;

import ch.nolix.core.environment.localcomputer.ShellProvider;
import ch.nolix.core.programcontrol.sequencer.GlobalSequencer;
import ch.nolix.system.application.main.Server;

final class Launcher {
	
	public static void main(String[] args) {
		
		final var server = Server.forDefaultPort();
		
		server.addDefaultApplication(PlanningPokerApplication.withTemporaryNodeDatabase());
		
		ShellProvider.startDefaultWebBrowserOpeningLoopBackAddress();
		
		GlobalSequencer
		.waitForSeconds(2)
		.andThen()
		.asSoonAsNoMore(server::hasClientConnected)
		.runInBackground(server::close);
	}
	
	private Launcher() {}
}
