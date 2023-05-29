package ch.nolix.planningpokerapi.applicationcontextapi;

public interface IApplicationContext {
	
	IDataController createDataController();
	
	IRoomChangeNotifier getOriEventController();
}
