package ch.nolix.planningpokerapi.datamodelapi.schemaapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeuniversalapi.Identified;
import ch.nolix.coreapi.attributeapi.mutablemandatoryattributeuniversalapi.Nameable;

public interface IUser extends Identified, Nameable {
	
	IRoomVisit getOriCurrentRoomVisit();
	
	boolean isInARoom();
}