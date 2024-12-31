package ch.nolix.planningpoker.datamodel.schema;

import ch.nolix.system.objectdata.schemamodel.Schema;
import ch.nolix.systemapi.objectdataapi.schemamodelapi.ISchema;

public final class SchemaCatalogue {

  public static final ISchema SCHEMA = Schema.withEntityType(User.class, Room.class, RoomVisit.class);

  private SchemaCatalogue() {
  }
}
