package org.serge.ws.rs.hk2;

import org.jvnet.hk2.annotations.Contract;
import org.serge.ws.rs.hk2.dto.Text;

@Contract
public interface TextService {

    Text newText(String text);
}
