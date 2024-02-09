package ch.nolix.planningpoker.datamodel.schema;

import ch.nolix.system.objectdata.schema.Schema;
import ch.nolix.systemapi.objectdataapi.schemaapi.ISchema;

public final class SchemaCatalogue {

  public static final ISchema SCHEMA = Schema.withEntityType(User.class, Room.class, RoomVisit.class);

  private SchemaCatalogue() {
  }
}
