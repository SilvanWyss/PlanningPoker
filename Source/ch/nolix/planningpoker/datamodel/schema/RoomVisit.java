package ch.nolix.planningpoker.datamodel.schema;

import ch.nolix.core.errorcontrol.invalidargumentexception.InvalidArgumentException;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.system.objectdatabase.database.BackReference;
import ch.nolix.system.objectdatabase.database.Entity;
import ch.nolix.system.objectdatabase.database.OptionalValue;
import ch.nolix.system.objectdatabase.database.Reference;
import ch.nolix.system.objectdatabase.database.Value;

public final class RoomVisit extends Entity implements IRoomVisit {
	
	private static final boolean DEFAULT_INFINITE_ESTIMATE_FLAG = false;
	
	public static RoomVisit forVisitor(final User visitor) {
		
		final var roomVisit = new RoomVisit();
		roomVisit.setVisitor(visitor);
		
		return roomVisit;
	}
	
	private final BackReference<Room> parentRoom =
	BackReference.forEntityAndBackReferencedPropertyName(Room.class, "roomVisits");
	
	private final Reference<User> visitor = Reference.forEntity(User.class);
	
	private final OptionalValue<Double> estimateInStoryPoints = new OptionalValue<>();
	
	private final Value<Boolean> infiniteEstimateFlag = Value.withInitialValue(DEFAULT_INFINITE_ESTIMATE_FLAG);
	
	private RoomVisit() {
		initialize();
	}
	
	@Override
	public void deleteEstimate() {
		
		estimateInStoryPoints.clear();
		
		infiniteEstimateFlag.setValue(false);
	}
	
	@Override
	public double getEstimateInStoryPoints() {
		return estimateInStoryPoints.getOriValue();
	}
	
	@Override
	public IRoom getOriParentRoom() {
		return parentRoom.getBackReferencedEntity();
	}
	
	@Override
	public boolean hasEstimateInStorypoints() {
		return estimateInStoryPoints.containsAny();
	}
	
	@Override
	public IUser getOriVisitor() {
		return visitor.getReferencedEntity();
	}
	
	@Override
	public boolean hasInfiniteEstimate() {
		return infiniteEstimateFlag.getOriValue();
	}
	
	@Override
	public void setEstimateInStoryPoints(final double estimateInStoryPoints) {
		
		GlobalValidator.assertThat(estimateInStoryPoints).thatIsNamed("estimate in story points").isNotNegative();
		
		deleteEstimate();
		
		this.estimateInStoryPoints.setValue(estimateInStoryPoints);
	}
	
	@Override
	public void setInfiniteEstimate() {
		
		deleteEstimate();
		
		infiniteEstimateFlag.setValue(true);
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