package ch.nolix.planningpokerapi.datamodelapi.schemaapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeapi.Identified;
import ch.nolix.coreapi.attributeapi.mutablemandatoryattributeapi.Nameable;

public interface IUser extends Identified, Nameable {

  IRoomVisit getStoredCurrentRoomVisit();

  boolean isInARoom();
}
