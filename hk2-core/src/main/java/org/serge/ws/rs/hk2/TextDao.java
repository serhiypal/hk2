package org.serge.ws.rs.hk2;

import org.jvnet.hk2.annotations.Contract;
import org.serge.ws.rs.hk2.dto.Text;

import java.util.UUID;

@Contract
public interface TextDao {

    Text create(Text text);

    Text get(UUID id);

    Text update(Text text);

    Text delete(UUID id);

}
