package ch.nolix.planningpoker.datamodelapi;

import ch.nolix.coreapi.attributeapi.mutablemandatoryattributeuniversalapi.Namable;

public interface IUser extends Namable<IUser> {
	
	IRoom createAndEnterNewRoom();
	
	void enterRoom(IRoom romm);
	
	void enterRoomByIdentification(String identification);
	
	IRoomVisit getRefRoomVisit();
	
	boolean isInRoom();
}
