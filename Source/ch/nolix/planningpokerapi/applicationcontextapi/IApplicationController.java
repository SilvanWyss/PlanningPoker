package ch.nolix.planningpokerapi.applicationcontextapi;

public interface IApplicationController {
	
	IDataController getRefDataController();
	
	IEventController getRefEventController();
}
