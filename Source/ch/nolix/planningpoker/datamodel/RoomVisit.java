package ch.nolix.planningpoker.datamodel;

import ch.nolix.core.errorcontrol.invalidargumentexception.InvalidArgumentException;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.IUser;
import ch.nolix.system.objectdatabase.database.BackReference;
import ch.nolix.system.objectdatabase.database.Entity;
import ch.nolix.system.objectdatabase.database.OptionalValue;
import ch.nolix.system.objectdatabase.database.Reference;
import ch.nolix.system.objectdatabase.database.Value;

public final class RoomVisit extends Entity implements IRoomVisit {
	
	private static final boolean DEFAULT_INFINITE_ESTIMATION_FLAG = false;
	
	public static RoomVisit forVisitor(final User visitor) {
		
		final var roomVisit = new RoomVisit();
		roomVisit.setVisitor(visitor);
		
		return roomVisit;
	}
	
	private final BackReference<Room> parentRoom =
	BackReference.forEntityAndBackReferencedPropertyName(Room.class, "roomVisits");
	
	private final Reference<User> visitor = Reference.forEntity(User.class);
	
	private final OptionalValue<Double> estimationInStoryPoints = new OptionalValue<>();
	
	private final Value<Boolean> infiniteEstimationFlag = Value.withInitialValue(DEFAULT_INFINITE_ESTIMATION_FLAG);
	
	private RoomVisit() {
		initialize();
	}
	
	@Override
	public void deleteEstimation() {
		
		estimationInStoryPoints.clear();
		
		infiniteEstimationFlag.setValue(false);
	}
	
	@Override
	public double getEstimationInStoryPoints() {
		return estimationInStoryPoints.getOriValue();
	}
	
	@Override
	public IRoom getOriParentRoom() {
		return parentRoom.getBackReferencedEntity();
	}
	
	@Override
	public boolean hasEstimationInStorypoints() {
		return estimationInStoryPoints.containsAny();
	}
	
	@Override
	public IUser getOriVisitor() {
		return visitor.getReferencedEntity();
	}
	
	@Override
	public boolean hasInfiniteEstimation() {
		return infiniteEstimationFlag.getOriValue();
	}
	
	@Override
	public void setEstimationInStoryPoints(final double estimationInStoryPoints) {
		
		GlobalValidator.assertThat(estimationInStoryPoints).thatIsNamed("estimation in story points").isNotNegative();
		
		deleteEstimation();
		
		this.estimationInStoryPoints.setValue(estimationInStoryPoints);
	}
	
	@Override
	public void setInfiniteEstimation() {
		
		deleteEstimation();
		
		infiniteEstimationFlag.setValue(true);
	}
	
	private void assertVisitorIsNotInARoom(final User visitor) {
		if (visitor.isInARoom()) {
			throw
			InvalidArgumentException.forArgumentNameAndArgumentAndErrorPredicate(
				"visitor",
				visitor,
				"is in a room"
			);
		}
	}
	
	private void setVisitor(final User visitor) {
		
		assertVisitorIsNotInARoom(visitor);
		
		this.visitor.setEntity(visitor);
	}
}
