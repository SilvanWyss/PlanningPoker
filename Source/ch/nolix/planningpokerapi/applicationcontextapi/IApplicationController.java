package ch.nolix.planningpokerapi.applicationcontextapi;

public interface IApplicationController {
	
	IDataController createDataController();
	
	IEventController getRefEventController();
}
