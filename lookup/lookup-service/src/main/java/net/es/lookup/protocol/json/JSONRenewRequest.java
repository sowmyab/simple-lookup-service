package net.es.lookup.protocol.json;

import net.es.lookup.common.*;
import java.util.Map;

public class JSONRenewRequest extends RenewRequest {
	public JSONRenewRequest() {
        super();
    }

	public JSONRenewRequest(Map<String,Object> map) {
        super(map);
    }
}