package ch.nolix.planningpokerapi.backendapi.dataadapterapi;

import ch.nolix.coreapi.resourcecontrolapi.savecontrolapi.IChangeSaver;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IUser;

public interface IDataAdapter extends IChangeSaver {

  boolean containsUserWithId(String id);

  IRoom createNewRoomAndEnterRoom(IUser user);

  IUser createUserWithName(String name);

  IRoomVisit enterRoom(IUser user, IRoom room);

  IRoom getStoredRoomById(String id);

  IRoom getStoredRoomByNumber(String number);

  IRoom getStoredRoomByNumberOrNull(String number);

  IRoomVisit getStoredRoomVisitById(String id);

  IUser getStoredUserById(String id);

  IUser getStoredUserByIdOrNull(String id);

  void leaveRoom(IUser user);
}
