package ch.nolix.planningpokerapi.backendapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeapi.IIdHolder;
import ch.nolix.coreapi.attributeapi.mutablemandatoryattributeapi.IMutableNameHolder;

public interface IUser extends IIdHolder, IMutableNameHolder {

  IRoomVisit getStoredCurrentRoomVisit();

  boolean isInARoom();
}
