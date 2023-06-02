package ch.nolix.planningpoker.datamodel;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.core.programatom.name.LowerCaseCatalogue;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
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
	
	private final Value<String> number = new Value<>();
	
	private final Reference<User> parentCreator = Reference.forEntity(User.class);
	
	private final Value<Boolean> estimationsVisible = Value.withInitialValue(DEFAULT_ESTIMATIONS_VISIBLE_VALUE);
	
	private final MultiReference<RoomVisit> roomVisits = MultiReference.forEntity(RoomVisit.class);
	
	private final MultiReference<EstimationRound> estimationRounds = MultiReference.forEntity(EstimationRound.class);
	
	private Room() {
		
		initialize();
		
		setInsertAction(this::setNumber);
	}
	
	@Override
	public void addVisitor(final IUser visitor) {
		
		final var roomVisit = RoomVisit.forVisitor((User)visitor);
		
		getOriParentDatabase().insertEntity(roomVisit);
		
		roomVisits.addEntity(roomVisit);
	}
	
	@Override
	public String getNumber() {
		return number.getOriValue();
	}
	
	@Override
	public IContainer<? extends IEstimationRound> getOriEstimationRounds() {
		return estimationRounds.getReferencedEntities();
	}
	
	@Override
	public IUser getOriParentCreator() {
		return parentCreator.getReferencedEntity();
	}
	
	@Override
	public IContainer<? extends IRoomVisit> getOriRoomVisits() {
		return roomVisits.getReferencedEntities();
	}
	
	@Override
	public boolean hasSetEstimationsInvisible() {
		return !hasSetEstimationsVisible();
	}

	@Override
	public boolean hasSetEstimationsVisible() {
		return estimationsVisible.getOriValue();
	}
	
	@Override
	public void removeVisitor(final IUser visitor) {
		
		final var roomVisit = (RoomVisit)visitor.getOriCurrentRoomVisit();
		
		roomVisits.removeEntity(roomVisit);
		roomVisit.delete();
	}
	
	@Override
	public void setEstimationsInvisible() {
		estimationsVisible.setValue(false);
	}
	
	@Override
	public void setEstimationsVisible() {
		estimationsVisible.setValue(true);
	}
	
	private String createNumber() {
		return String.valueOf(getOriParentTable().getEntityCount());
	}
	
	private void setNumber() {
		setNumber(createNumber());
	}
	
	private void setNumber(final String number) {
		
		GlobalValidator.assertThat(number).thatIsNamed(LowerCaseCatalogue.NUMBER).isNotNull();
		
		this.number.setValue(number);
	}
	
	private void setParentCreator(final User parentCreator) {
		this.parentCreator.setEntity(parentCreator);
	}
}
