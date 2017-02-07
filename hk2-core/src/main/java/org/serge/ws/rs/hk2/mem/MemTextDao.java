package org.serge.ws.rs.hk2.mem;

import org.jvnet.hk2.annotations.Service;
import org.serge.ws.rs.hk2.TextDao;
import org.serge.ws.rs.hk2.dto.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MemTextDao implements TextDao {

    private Map<UUID, Text> mem = new ConcurrentHashMap<>();

    public Text create(Text text) {
        text.setId(UUID.randomUUID());
        mem.put(text.getId(), text);
        return text;
    }

    public Text get(UUID id) {
        return mem.get(id);
    }

    public Text update(Text text) {
        Text result = mem.computeIfPresent(text.getId(), (id, txt) -> {
            txt.setText(text.getText());
            return text;
        });
        if (result == null) {
            throw new IllegalArgumentException("Not found");
        }
        return result;
    }

    public Text delete(UUID id) {
        Text result = mem.remove(id);
        if (result == null) {
            throw new IllegalArgumentException("Not found");
        }
        return result;
    }

}
