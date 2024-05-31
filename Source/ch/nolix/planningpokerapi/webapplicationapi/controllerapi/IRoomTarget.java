package ch.nolix.planningpokerapi.webapplicationapi.controllerapi;

import ch.nolix.coreapi.netapi.targetapi.IApplicationInstanceTarget;

public interface IRoomTarget extends IApplicationInstanceTarget {

  String getRoomNumber();
}
