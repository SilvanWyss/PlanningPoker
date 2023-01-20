package ch.nolix.planningpokerapi.applicationcontextapi;

public interface IApplicationController extends AutoCloseable {
	
	IDataController getRefDataController();
	
	IEventController getRefEventController();
}
