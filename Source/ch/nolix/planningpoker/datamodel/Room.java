package ch.nolix.planningpoker.datamodel;

import ch.nolix.coreapi.containerapi.mainapi.IContainer;
import ch.nolix.planningpokerapi.datamodelapi.IEstimationRound;
import ch.nolix.planningpokerapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.IUser;
import ch.nolix.system.objectdatabase.database.Entity;
import ch.nolix.system.objectdatabase.database.MultiReference;
import ch.nolix.system.objectdatabase.database.Reference;
import ch.nolix.system.objectdatabase.database.Value;

public final class Room extends Entity implements IRoom {
	
	public static final boolean DEFAULT_ESTIMATIONS_VISIBLE_VALUE = false;
	
	public static Room fromParentCreator(final User parentCreator) {
		
		final var room = new Room();
		room.setParentCreator(parentCreator);
		
		return room;
	}
	
	private final Value<String> identification =
	Value.withInitialValue(RoomGlobalIdentificationGenerator.generateNextIdentification());
	
	private final Reference<User> parentCreator = Reference.forEntity(User.class);
			
	private final Value<Boolean> estimationsVisible = Value.withInitialValue(DEFAULT_ESTIMATIONS_VISIBLE_VALUE);
	
	private final MultiReference<RoomVisit> visits = MultiReference.forEntity(RoomVisit.class);
	
	private final MultiReference<EstimationRound> estimationRounds = MultiReference.forEntity(EstimationRound.class);
	
	private Room() {
		initialize();
	}
	
	@Override
	public void addVisitor(final IUser visitor) {
		
		final var roomVisit = RoomVisit.forVisitor((User)visitor);
		
		getRefParentDatabase().insertEntity(roomVisit);
		
		visits.addEntity(roomVisit);
	}
	
	@Override
	public String getIdentification() {
		return identification.getRefValue();
	}
	
	@Override
	public IContainer<? extends IEstimationRound> getRefEstimationRounds() {
		return estimationRounds.getReferencedEntities();
	}
	
	@Override
	public IUser getRefParentCreator() {
		return parentCreator.getReferencedEntity();
	}
	
	@Override
	public IContainer<? extends IRoomVisit> getRefVisits() {
		return visits.getReferencedEntities();
	}
	
	@Override
	public boolean hasSetEstimationsInvisible() {
		return !hasSetEstimationsVisible();
	}

	@Override
	public boolean hasSetEstimationsVisible() {
		return estimationsVisible.getRefValue();
	}
	
	@Override
	public void setEstimationsInvisible() {
		estimationsVisible.setValue(false);
	}
	
	@Override
	public void setEstimationsVisible() {
		estimationsVisible.setValue(true);
	}
	
	private void setParentCreator(final User parentCreator) {
		this.parentCreator.setEntity(parentCreator);
	}
}
