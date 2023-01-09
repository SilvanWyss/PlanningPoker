package ch.nolix.planningpokerapi.businessapi;

import ch.nolix.coreapi.functionapi.mutationuniversalapi.IChangeSaver;
import ch.nolix.planningpokerapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.IUser;

public interface IPlanningPokerAccess extends IChangeSaver {
	
	IRoom createAndEnterNewRoom(IUser user);
	
	IUser createUserWithName(String name);
	
	IRoom getRefRoomByIdentification(String identification);
	
	IUser getRefUserById(String id);
}
