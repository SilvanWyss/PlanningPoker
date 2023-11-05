package ch.nolix.planningpoker.datamodel.schema;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.core.programatom.name.LowerCaseCatalogue;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.system.objectdatabase.database.Entity;
import ch.nolix.system.objectdatabase.database.MultiReference;
import ch.nolix.system.objectdatabase.database.Reference;
import ch.nolix.system.objectdatabase.database.Value;

public final class Room extends Entity implements IRoom {

  public static final boolean DEFAULT_ESTIMATES_VISIBLE_VALUE = false;

  private final Value<String> number = new Value<>();

  private final Reference<User> parentCreator = Reference.forEntity(User.class);

  private final Value<Boolean> estimatesVisible = Value.withInitialValue(DEFAULT_ESTIMATES_VISIBLE_VALUE);

  private final MultiReference<RoomVisit> roomVisits = MultiReference.forEntity(RoomVisit.class);

  private Room() {

    initialize();

    setInsertAction(this::setNumber);
  }

  public static Room fromParentCreator(final User parentCreator) {

    final var room = new Room();
    room.setParentCreator(parentCreator);

    return room;
  }

  @Override
  public void addRoomVisit(final IRoomVisit roomVisit) {
    roomVisits.addEntity(roomVisit);
  }

  @Override
  public String getNumber() {
    return number.getStoredValue();
  }

  @Override
  public IUser getStoredParentCreator() {
    return parentCreator.getReferencedEntity();
  }

  @Override
  public IContainer<? extends IRoomVisit> getStoredRoomVisits() {
    return roomVisits.getReferencedEntities();
  }

  @Override
  public boolean hasSetEstimatesInvisible() {
    return !hasSetEstimatesVisible();
  }

  @Override
  public boolean hasSetEstimatesVisible() {
    return estimatesVisible.getStoredValue();
  }

  @Override
  public void removeRoomVisit(final IRoomVisit roomVisit) {
    roomVisits.removeEntity((RoomVisit) roomVisit);
  }

  @Override
  public void setEstimatesInvisible() {
    estimatesVisible.setValue(false);
  }

  @Override
  public void setEstimatesVisible() {
    estimatesVisible.setValue(true);
  }

  private String createNumber() {

    final var roomCount = getStoredParentTable().getEntityCount();
    final var localNumber = 100_000 + (56 * (roomCount + 2));

    return String.valueOf(localNumber);
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
