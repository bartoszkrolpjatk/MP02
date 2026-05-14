package pl.edu.pjatk.mas.mp02.model.association;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(fluent = true)
class AssociationEntry {
    private final AssociatedObject target;
    private final Set<Payload> payload;

    public AssociationEntry(AssociatedObject target, Payload payload) {
        this.target = target;
        this.payload = new HashSet<>();
        this.addToPayload(payload);
    }

    public boolean addToPayload(Payload payload) {
        if (payload != null)
            return this.payload.add(payload);
        return false;
    }

    public boolean removeFromPayload(Payload payload) {
        if (payload != null)
            return this.payload.remove(payload);
        return false;
    }
}
