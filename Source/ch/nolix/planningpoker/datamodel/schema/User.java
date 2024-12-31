package ch.nolix.planningpoker.datamodel.schema;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.programatomapi.variableapi.LowerCaseVariableCatalogue;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.system.objectdata.model.Entity;
import ch.nolix.system.objectdata.model.OptionalBackReference;
import ch.nolix.system.objectdata.model.Value;

public final class User extends Entity implements IUser {

  public static final int MAX_NAME_LENGTH = 20;

  private final Value<String> name = Value.withValueType(String.class);

  private final OptionalBackReference<RoomVisit> currentRoomVisit = //
  OptionalBackReference.forEntityAndBackReferencedFieldName(RoomVisit.class, "visitor");

  private User() {
    initialize();
  }

  public static User withName(final String name) {

    final var user = new User();
    user.setName(name);

    return user;
  }

  @Override
  public String getName() {
    return name.getStoredValue();
  }

  @Override
  public IRoomVisit getStoredCurrentRoomVisit() {
    return currentRoomVisit.getStoredBackReferencedEntity();
  }

  @Override
  public boolean isInARoom() {
    return currentRoomVisit.containsAny();
  }

  @Override
  public void setName(final String name) {

    GlobalValidator.assertThat(name).thatIsNamed(LowerCaseVariableCatalogue.NAME).isNotBlank();
    GlobalValidator.assertThat(name).thatIsNamed(LowerCaseVariableCatalogue.NAME).isNotLongerThan(MAX_NAME_LENGTH);
    GlobalValidator.assertThat(name).thatIsNamed(LowerCaseVariableCatalogue.NAME).matches("[a-zA-Z0-9[.]\s]*");

    this.name.setValue(name);
  }
}
