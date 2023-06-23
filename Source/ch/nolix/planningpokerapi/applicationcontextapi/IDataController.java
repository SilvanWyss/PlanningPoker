package ch.nolix.planningpokerapi.applicationcontextapi;

import ch.nolix.coreapi.functionapi.mutationuniversalapi.IChangeSaver;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;

public interface IDataController extends IChangeSaver {
	
	boolean containsUserWithId(String id);
	
	IRoom createNewRoomAndEnterRoom(IUser user);
	
	IUser createUserWithName(String name);
	
	IRoomVisit enterRoom(IUser user, IRoom room);
	
	IRoom getOriRoomById(String id);
	
	IRoom getOriRoomByNumber(String number);
	
	IRoom getOriRoomByNumberOrNull(String number);
	
	IRoomVisit getOriRoomVisitById(String id);
	
	IUser getOriUserById(String id);
	
	IUser getOriUserByIdOrNull(String id);
	
	void leaveRoom(IUser user);
}
