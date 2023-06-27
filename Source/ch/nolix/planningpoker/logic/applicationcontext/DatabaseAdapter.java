package ch.nolix.planningpoker.logic.applicationcontext;

import ch.nolix.core.commontype.commontypehelper.GlobalStringHelper;
import ch.nolix.core.errorcontrol.exception.GeneralException;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpoker.datamodel.schema.Room;
import ch.nolix.planningpoker.datamodel.schema.RoomVisit;
import ch.nolix.planningpoker.datamodel.schema.User;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDatabaseAdapter;

public final class DatabaseAdapter implements IDatabaseAdapter {
	
	public static DatabaseAdapter usingDatabaseAdapter(
		final ch.nolix.system.objectdatabase.database.DatabaseAdapter databaseAdapter
	) {
		return new DatabaseAdapter(databaseAdapter);
	}
	
	private final ch.nolix.system.objectdatabase.database.DatabaseAdapter internalDatabaseAdapter;
	
	private DatabaseAdapter(final ch.nolix.system.objectdatabase.database.DatabaseAdapter databaseAdapter) {
		this.internalDatabaseAdapter = databaseAdapter.getEmptyCopy();
	}
	
	@Override
	public void close() {
		internalDatabaseAdapter.close();
	}
	
	@Override
	public boolean containsUserWithId(String id) {
		return internalDatabaseAdapter.getOriTableByEntityType(User.class).containsEntityWithId(id);
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
		internalDatabaseAdapter.insert(user);
		
		return user;
	}
	
	@Override
	public IRoomVisit enterRoom(IUser user, IRoom room) {
		
		if (user.isInARoom()) {
			
			final var currentRoomVisit = user.getOriCurrentRoomVisit();
			
			if (currentRoomVisit.getOriParentRoom().hasId(room.getId())) {
				return currentRoomVisit;
			}
			
			leaveRoom(user);
		}
		
		return onlyEnterRoom(user, room);
	}
	
	@Override
	public IRoom getOriRoomById(String id) {
		return internalDatabaseAdapter.getOriTableByEntityType(Room.class).getOriEntityById(id);
	}
	
	@Override
	public IRoom getOriRoomByNumber(final String number) {
		
		GlobalValidator.assertThat(number).thatIsNamed("room number").isNotBlank();
		
		final var room =
		internalDatabaseAdapter
		.getOriTableByEntityType(Room.class)
		.getOriEntities()
		.getOriFirstOrNull(r -> r.getNumber().equals(number));
		
		if (room == null) {
			throw GeneralException.withErrorMessage(
				"The room " + GlobalStringHelper.getInQuotes(number) + " does not exist."
			);
		}
		
		return room;
	}
	
	@Override
	public IRoom getOriRoomByNumberOrNull(final String number) {
		return
		internalDatabaseAdapter
		.getOriTableByEntityType(Room.class)
		.getOriEntities()
		.getOriFirstOrNull(r -> r.getNumber().equals(number));
	}
	
	@Override
	public IRoomVisit getOriRoomVisitById(final String id) {
		return
		internalDatabaseAdapter
		.getOriTableByEntityType(RoomVisit.class)
		.getOriEntityById(id);
	}
	
	@Override
	public IUser getOriUserById(final String id) {
		return internalDatabaseAdapter.getOriTableByEntityType(User.class).getOriEntityById(id);
	}
	
	@Override
	public IUser getOriUserByIdOrNull(String id) {
		return internalDatabaseAdapter.getOriTableByEntityType(User.class).getOriEntityByIdOrNull(id);
	}
	
	@Override
	public boolean hasChanges() {
		return internalDatabaseAdapter.hasChanges();
	}
	
	@Override
	public void leaveRoom(final IUser user) {
		
		final var roomVisit = (RoomVisit)user.getOriCurrentRoomVisit();
		final var room = roomVisit.getOriParentRoom();
		
		room.removeRoomVisit(roomVisit);
		roomVisit.deleteWithoutReferenceCheck();
	}
	
	@Override
	public void saveChanges() {
		internalDatabaseAdapter.saveChangesAndReset();
	}
	
	private Room createNewRoom(final IUser user) {
		
		final var room = Room.fromParentCreator((User)user);
		internalDatabaseAdapter.insert(room);
		
		return room;
	}
	
	private IRoomVisit onlyEnterRoom(IUser user, IRoom room) {
		
		final var roomVisit = RoomVisit.forVisitor((User)user);
		internalDatabaseAdapter.insert(roomVisit);
		room.addRoomVisit(roomVisit);
		
		return roomVisit;
	}
}
