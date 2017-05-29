package org.serge.ws.rs.hk2.dto;

import java.util.List;

public class ResourceCollection<T> {

    private List<T> values;

    public ResourceCollection() {
    }

    public ResourceCollection(List<T> values) {
        this.values = values;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }
}
