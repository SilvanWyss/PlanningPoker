package ch.nolix.planningpokerapi.logicapi.applicationcontextapi;

import ch.nolix.coreapi.programcontrolapi.savecontrolapi.IChangeSaver;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;

public interface IDatabaseAdapter extends IChangeSaver {
	
	boolean containsUserWithId(String id);
	
	IRoom createNewRoomAndEnterRoom(IUser user);
	
	IUser createUserWithName(String name);
	
	IRoomVisit enterRoom(IUser user, IRoom room);
	
	IRoom getStoredRoomById(String id);
	
	IRoom getStoredRoomByNumber(String number);
	
	IRoom getStoredRoomByNumberOrNull(String number);
	
	IRoomVisit getStoredRoomVisitById(String id);
	
	IUser getStoredUserById(String id);
	
	IUser getStoredUserByIdOrNull(String id);
	
	void leaveRoom(IUser user);
}
