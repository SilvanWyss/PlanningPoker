package ch.nolix.planningpokerapi.applicationcontextapi;

import ch.nolix.coreapi.functionapi.mutationuniversalapi.IChangeSaver;
import ch.nolix.planningpokerapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.IUser;

public interface IDataController extends IChangeSaver {
	
	boolean containsUserWithId(String id);
	
	IRoom createAndEnterNewRoom(IUser user);
	
	IUser createUserWithName(String name);
	
	IRoom getOriRoomById(String id);
	
	IRoom getOriRoomByNumber(String number);
	
	IRoom getOriRoomByNumberOrNull(String number);
	
	IUser getOriUserById(String id);
	
	IUser getOriUserByIdOrNull(String id);
}
