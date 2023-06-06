package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.planningpoker.datamodel.Room;
import ch.nolix.planningpoker.datamodel.RoomVisit;
import ch.nolix.planningpoker.datamodel.User;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.IUser;
import ch.nolix.system.objectdatabase.database.DatabaseAdapter;

public final class DataController implements IDataController {
	
	public static DataController usingDatabaseAdapter(final DatabaseAdapter databaseAdapter) {
		return new DataController(databaseAdapter);
	}
	
	private final DatabaseAdapter databaseAdapter;
	
	private DataController(final DatabaseAdapter databaseAdapter) {
		this.databaseAdapter = databaseAdapter.getEmptyCopy();
	}
	
	@Override
	public void close() {
		databaseAdapter.close();
	}
	
	@Override
	public boolean containsUserWithId(String id) {
		return databaseAdapter.getOriTableByEntityType(User.class).containsEntityWithId(id);
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
		databaseAdapter.insert(user);
		
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
		return databaseAdapter.getOriTableByEntityType(Room.class).getOriEntityById(id);
	}
	
	@Override
	public IRoom getOriRoomByNumber(final String number) {
		return
		databaseAdapter
		.getOriTableByEntityType(Room.class)
		.getOriEntities()
		.getOriFirst(r -> r.getNumber().equals(number));		
	}
	
	@Override
	public IRoom getOriRoomByNumberOrNull(final String number) {
		return
		databaseAdapter
		.getOriTableByEntityType(Room.class)
		.getOriEntities()
		.getOriFirstOrNull(r -> r.getNumber().equals(number));
	}
	
	@Override
	public IRoomVisit getOriRoomVisitById(final String id) {
		return
		databaseAdapter
		.getOriTableByEntityType(RoomVisit.class)
		.getOriEntityById(id);
	}
	
	@Override
	public IUser getOriUserById(final String id) {
		return databaseAdapter.getOriTableByEntityType(User.class).getOriEntityById(id);
	}
	
	@Override
	public IUser getOriUserByIdOrNull(String id) {
		return databaseAdapter.getOriTableByEntityType(User.class).getOriEntityByIdOrNull(id);
	}
	
	@Override
	public boolean hasChanges() {
		return databaseAdapter.hasChanges();
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
		databaseAdapter.saveChangesAndReset();
	}
	
	private Room createNewRoom(final IUser user) {
		
		final var room = Room.fromParentCreator((User)user);
		databaseAdapter.insert(room);
		
		return room;
	}
	
	private IRoomVisit onlyEnterRoom(IUser user, IRoom room) {
		
		final var roomVisit = RoomVisit.forVisitor((User)user);
		databaseAdapter.insert(roomVisit);
		room.addRoomVisit(roomVisit);
		
		return roomVisit;
	}
}
