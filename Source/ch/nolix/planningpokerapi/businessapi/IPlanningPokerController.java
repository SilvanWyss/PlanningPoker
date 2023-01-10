package ch.nolix.planningpokerapi.businessapi;

public interface IPlanningPokerController {
	
	IDataController getRefDataController();
	
	IEventController getRefEventController();
}
