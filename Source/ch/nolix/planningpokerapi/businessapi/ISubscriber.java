package ch.nolix.planningpokerapi.businessapi;

import ch.nolix.coreapi.functionapi.requestuniversalapi.CloseStateRequestable;

public interface ISubscriber extends CloseStateRequestable {
	
	void update();
}
