package ch.nolix.planningpoker.backend.datamodel;

import ch.nolix.core.errorcontrol.invalidargumentexception.InvalidArgumentException;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IUser;
import ch.nolix.system.objectdata.model.BackReference;
import ch.nolix.system.objectdata.model.Entity;
import ch.nolix.system.objectdata.model.OptionalValue;
import ch.nolix.system.objectdata.model.Reference;
import ch.nolix.system.objectdata.model.Value;

public final class RoomVisit extends Entity implements IRoomVisit {

  private static final boolean DEFAULT_INFINITE_ESTIMATE_FLAG = false;

  private final BackReference<Room> parentRoom = //
  BackReference.forEntityAndBackReferencedFieldName(Room.class, "roomVisits");

  private final Reference<User> visitor = Reference.forEntity(User.class);

  private final OptionalValue<Double> estimateInStoryPoints = OptionalValue.withValueType(Double.class);

  private final Value<Boolean> infiniteEstimateFlag = Value.withInitialValue(DEFAULT_INFINITE_ESTIMATE_FLAG);

  private RoomVisit() {
    initialize();
  }

  public static RoomVisit forVisitor(final User visitor) {

    final var roomVisit = new RoomVisit();
    roomVisit.setVisitor(visitor);

    return roomVisit;
  }

  @Override
  public void deleteEstimate() {

    estimateInStoryPoints.clear();

    infiniteEstimateFlag.setValue(false);
  }

  @Override
  public double getEstimateInStoryPoints() {
    return estimateInStoryPoints.getStoredValue();
  }

  @Override
  public IRoom getStoredParentRoom() {
    return parentRoom.getStoredBackReferencedEntity();
  }

  @Override
  public boolean hasEstimateInStorypoints() {
    return estimateInStoryPoints.containsAny();
  }

  @Override
  public IUser getStoredVisitor() {
    return visitor.getStoredReferencedEntity();
  }

  @Override
  public boolean hasInfiniteEstimate() {
    return infiniteEstimateFlag.getStoredValue();
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
      throw InvalidArgumentException.forArgumentNameAndArgumentAndErrorPredicate(
        "visitor",
        visitor,
        "is in a room");
    }
  }

  private void setVisitor(final User visitor) {

    assertVisitorIsNotInARoom(visitor);

    this.visitor.setEntity(visitor);
  }
}
