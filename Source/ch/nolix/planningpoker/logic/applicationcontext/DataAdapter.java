package ch.nolix.planningpoker.logic.applicationcontext;

import ch.nolix.core.commontypetool.GlobalStringTool;
import ch.nolix.core.errorcontrol.exception.GeneralException;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpoker.datamodel.schema.Room;
import ch.nolix.planningpoker.datamodel.schema.RoomVisit;
import ch.nolix.planningpoker.datamodel.schema.User;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;

public final class DataAdapter implements IDataAdapter {

  private final ch.nolix.systemapi.objectdataapi.dataadapterapi.IDataAdapter internalDatabaseAdapter;

  private DataAdapter(final ch.nolix.systemapi.objectdataapi.dataadapterapi.IDataAdapter databaseAdapter) {
    this.internalDatabaseAdapter = databaseAdapter.getEmptyCopy();
  }

  public static DataAdapter usingDatabaseAdapter(
    final ch.nolix.systemapi.objectdataapi.dataadapterapi.IDataAdapter databaseAdapter) {
    return new DataAdapter(databaseAdapter);
  }

  @Override
  public void close() {
    internalDatabaseAdapter.close();
  }

  @Override
  public boolean containsUserWithId(String id) {
    return internalDatabaseAdapter.getStoredTableByEntityType(User.class).containsEntityWithId(id);
  }

  @Override
  public IRoom createNewRoomAndEnterRoom(final IUser user) {

    final var room = createNewRoom(user);
    enterRoom(user, room);

    return room;
  }

  @Override
  public IUser createUserWithName(final String name) {

    final var user = User.withName(name);
    internalDatabaseAdapter.insertEntity(user);

    return user;
  }

  @Override
  public IRoomVisit enterRoom(IUser user, IRoom room) {

    if (user.isInARoom()) {

      final var currentRoomVisit = user.getStoredCurrentRoomVisit();
      final var currentRoom = currentRoomVisit.getStoredParentRoom();

      if (currentRoom.hasId(room.getId())) {
        return currentRoomVisit;
      }

      leaveRoom(user);
    }

    return onlyEnterRoom(user, room);
  }

  @Override
  public IRoom getStoredRoomById(String id) {
    return internalDatabaseAdapter.getStoredTableByEntityType(Room.class).getStoredEntityById(id);
  }

  @Override
  public IRoom getStoredRoomByNumber(final String number) {

    GlobalValidator.assertThat(number).thatIsNamed("room number").isNotBlank();

    final var room = internalDatabaseAdapter
      .getStoredTableByEntityType(Room.class)
      .getStoredEntities()
      .getOptionalStoredFirst(r -> r.getNumber().equals(number));

    if (room.isEmpty()) {
      throw GeneralException.withErrorMessage(
        "The room " + GlobalStringTool.getInSingleQuotes(number) + " does not exist.");
    }

    return room.get();
  }

  @Override
  public IRoom getStoredRoomByNumberOrNull(final String number) {
    return internalDatabaseAdapter
      .getStoredTableByEntityType(Room.class)
      .getStoredEntities()
      .getOptionalStoredFirst(r -> r.getNumber().equals(number))
      .orElse(null);
  }

  @Override
  public IRoomVisit getStoredRoomVisitById(final String id) {
    return internalDatabaseAdapter
      .getStoredTableByEntityType(RoomVisit.class)
      .getStoredEntityById(id);
  }

  @Override
  public IUser getStoredUserById(final String id) {
    return internalDatabaseAdapter.getStoredTableByEntityType(User.class).getStoredEntityById(id);
  }

  @Override
  public IUser getStoredUserByIdOrNull(String id) {
    return internalDatabaseAdapter.getStoredTableByEntityType(User.class).getOptionalStoredEntityById(id).orElse(null);
  }

  @Override
  public boolean hasChanges() {
    return internalDatabaseAdapter.hasChanges();
  }

  @Override
  public boolean isClosed() {
    return internalDatabaseAdapter.isClosed();
  }

  @Override
  public void leaveRoom(final IUser user) {

    final var roomVisit = (RoomVisit) user.getStoredCurrentRoomVisit();
    final var room = roomVisit.getStoredParentRoom();

    room.removeRoomVisit(roomVisit);
    roomVisit.deleteWithoutReferenceCheck();
  }

  @Override
  public void saveChanges() {
    internalDatabaseAdapter.saveChanges();
  }

  private Room createNewRoom(final IUser user) {

    final var room = Room.fromParentCreator((User) user);
    internalDatabaseAdapter.insertEntity(room);

    return room;
  }

  private IRoomVisit onlyEnterRoom(IUser user, IRoom room) {

    final var roomVisit = RoomVisit.forVisitor((User) user);
    internalDatabaseAdapter.insertEntity(roomVisit);
    room.addRoomVisit(roomVisit);

    return roomVisit;
  }
}
