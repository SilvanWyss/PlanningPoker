package ch.nolix.planningpokerapi.datamodelapi.schemaapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeapi.IIdHolder;
import ch.nolix.coreapi.attributeapi.mutablemandatoryattributeapi.IMutableNameHolder;

public interface IUser extends IIdHolder, IMutableNameHolder {

  IRoomVisit getStoredCurrentRoomVisit();

  boolean isInARoom();
}
