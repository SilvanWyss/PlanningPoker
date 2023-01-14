package ch.nolix.planningpokerapi.businessapi;

public interface IApplicationController {
	
	IDataController getRefDataController();
	
	IEventController getRefEventController();
}
