package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeuniversalapi.IdentifiedByString;

public interface IUser extends IdentifiedByString {
	
	String getName();
	
	IRoomVisit getRefCurrentRoomVisit();
	
	boolean isInRoom();
	
	void setName(String name);
}
