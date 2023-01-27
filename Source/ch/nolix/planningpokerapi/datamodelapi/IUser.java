package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeuniversalapi.IdentifiedByString;
import ch.nolix.coreapi.attributeapi.mutablemandatoryattributeuniversalapi.Namable;

public interface IUser extends IdentifiedByString, Namable {
	
	IRoomVisit getRefCurrentRoomVisit();
	
	boolean isInARoom();
}
