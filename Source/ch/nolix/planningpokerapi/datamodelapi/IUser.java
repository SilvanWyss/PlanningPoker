package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeuniversalapi.Identified;
import ch.nolix.coreapi.attributeapi.mutablemandatoryattributeuniversalapi.Nameable;

public interface IUser extends Identified, Nameable {
	
	IRoomVisit getRefCurrentRoomVisit();
	
	boolean isInARoom();
}
