package ch.nolix.planningpokerapi.backendapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeapi.IIdHolder;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;

public interface IRoom extends IIdHolder {

  void addRoomVisit(IRoomVisit roomVisit);

  String getNumber();

  IUser getStoredParentCreator();

  IContainer<? extends IRoomVisit> getStoredRoomVisits();

  boolean hasSetEstimatesInvisible();

  boolean hasSetEstimatesVisible();

  void removeRoomVisit(IRoomVisit roomVisit);

  void setEstimatesInvisible();

  void setEstimatesVisible();
}
