package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.IncorrectQualifierDeclaration;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationIsNotQualifiedException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationMultiplicityException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationsDoNotExistException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.PayloadOperationException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static pl.edu.pjatk.mas.mp02.model.association.AssociationsMetadataCacheHolder.resolveByTargetAndId;

public abstract class AssociatedObject {
    static final String DEFAULT_ASSOCIATION_ID = "";

    private final Map<AssociationMetadata, Map<Object, AssociationEntry>> associations = new ConcurrentHashMap<>();

    public void removePayload(AssociatedObject target, Payload payload) throws AssociationException {
        removePayload(target, payload, DEFAULT_ASSOCIATION_ID);
    }

    public void removePayload(AssociatedObject target, Payload payload, String id) throws AssociationException {
        validatePayloadExistsAndPayloadType(target, id, payload.getClass());
        var thisEntry = this.getEntry(target, id)
                .orElseThrow(() -> new PayloadOperationException(this, target));
        target.validatePayloadExistsAndPayloadType(this, id, payload.getClass());
        var targetEntry = target.getEntry(this, id)
                .orElseThrow(() -> new PayloadOperationException(target, this));

        if (!thisEntry.removeFromPayload(payload))
            throw new PayloadOperationException("Unable to remove payload from: '%s'".formatted(thisEntry));
        if (!targetEntry.removeFromPayload(payload)) {
            thisEntry.addToPayload(payload);
            throw new PayloadOperationException("Unable to remove payload from: '%s'".formatted(target));
        }
    }

    public void addPayload(AssociatedObject target, Payload payload) throws AssociationException {
        addPayload(target, payload, DEFAULT_ASSOCIATION_ID);
    }

    public void addPayload(AssociatedObject target, Payload payload, String id) throws AssociationException {
        validatePayloadExistsAndPayloadType(target, id, payload.getClass());
        var thisEntry = this.getEntry(target, id)
                .orElseThrow(() -> new PayloadOperationException(this, target));
        target.validatePayloadExistsAndPayloadType(this, id, payload.getClass());
        var targetEntry = target.getEntry(this, id)
                .orElseThrow(() -> new PayloadOperationException(target, this));

        if (!thisEntry.addToPayload(payload))
            throw new PayloadOperationException("Unable to add payload to: '%s'".formatted(thisEntry));
        if (!targetEntry.addToPayload(payload)) {
            thisEntry.removeFromPayload(payload);
            throw new PayloadOperationException("Unable to add payload to: '%s'".formatted(target));
        }
    }

    public void link(AssociatedObject target, Payload payload) throws AssociationException {
        link(target, payload, DEFAULT_ASSOCIATION_ID);
    }

    public void link(AssociatedObject target, Payload payload, String id) throws AssociationException {
        this.validatePayloadExistsAndPayloadType(target, id, payload.getClass());
        target.validatePayloadExistsAndPayloadType(this, id, payload.getClass());
        this.link(target, payload, id, 2);
    }

    public <T extends Payload> Set<T> getPayloads(AssociatedObject target) throws AssociationException {
        return getPayloads(target, DEFAULT_ASSOCIATION_ID);
    }

    @SuppressWarnings("unchecked")
    public <T extends Payload> Set<T> getPayloads(AssociatedObject target, String id) throws AssociationException {
        validatePayloadExists(target, id);
        return (Set<T>) getEntry(target, id)
                .map(AssociationEntry::payload)
                .orElse(Collections.emptySet());
    }

    private Optional<AssociationEntry> getEntry(AssociatedObject target, String id) {
        AssociationMetadata metadata = resolveByTargetAndId(this.getClass(), target.getClass(), id);
        var key = findQualifier(metadata, target).orElse(target);
        return Optional.ofNullable(associations.get(metadata))
                .flatMap(links -> Optional.ofNullable(links.get(key)));
    }

    private void validatePayloadExistsAndPayloadType(AssociatedObject target, String id, Class<? extends Payload> payloadType) throws AssociationException {
        AssociationMetadata metadata = resolveByTargetAndId(this.getClass(), target.getClass(), id);
        validatePayloadExists(target, id);
        if (!Objects.equals(metadata.payloadType().get(), payloadType))
            throw new PayloadOperationException(payloadType, metadata.payloadType().get(), this.getClass());
    }

    private void validatePayloadExists(AssociatedObject target, String id) throws AssociationException {
        AssociationMetadata metadata = resolveByTargetAndId(this.getClass(), target.getClass(), id);
        if (metadata.payloadType().isEmpty())
            throw new PayloadOperationException(this.getClass(), target.getClass());
    }

    public void link(AssociatedObject target) throws AssociationException {
        link(target, DEFAULT_ASSOCIATION_ID);
    }

    public void link(AssociatedObject target, String id) throws AssociationException {
        link(target, null, id, 2);
    }

    private void link(AssociatedObject target, Payload payload, String id, int callCounter) throws AssociationException {
        if (callCounter <= 0)
            return;

        AssociationMetadata metadata = resolveByTargetAndId(this.getClass(), target.getClass(), id);
        Map<Object, AssociationEntry> links = associations.computeIfAbsent(metadata, amd -> new ConcurrentHashMap<>());

        var key = findQualifier(metadata, target).orElse(target);
        if (links.containsKey(key))
            throw new AssociationAlreadyExistsException(this, target, key);
        if (links.size() == metadata.max())
            throw new AssociationMultiplicityException("Cannot link '%s' with '%s'! Max limit '%s' hit".formatted(this, target, metadata.max()));

        target.link(this, payload, metadata.id(), --callCounter);
        links.put(key, new AssociationEntry(target, payload));
    }

    public void unlink(AssociatedObject target) throws AssociationException {
        unlink(target, DEFAULT_ASSOCIATION_ID);
    }

    public void unlink(AssociatedObject target, String id) throws AssociationException {
        unlink(target, id, 2);
    }

    private void unlink(AssociatedObject target, String id, int callCounter) throws AssociationException {
        if (callCounter <= 0)
            return;

        AssociationMetadata metadata = resolveByTargetAndId(this.getClass(), target.getClass(), id);
        Map<Object, AssociationEntry> links = Optional.ofNullable(associations.get(metadata))
                .orElseThrow(() -> new AssociationsDoNotExistException(this.getClass(), target.getClass(), id));

        var key = findQualifier(metadata, target).orElse(target);
        if (!links.containsKey(key))
            throw new AssociationsDoNotExistException(this.getClass(), target.getClass(), id);
        if (links.size() == metadata.min())
            throw new AssociationMultiplicityException("Cannot unlink '%s' with '%s'! Min limit '%s' hit".formatted(this, target, metadata.min()));

        target.unlink(this, metadata.id(), --callCounter);
        links.remove(key);
    }

    public <T extends AssociatedObject> List<T> getLinks(Class<T> targetType) {
        return getLinks(targetType, DEFAULT_ASSOCIATION_ID);
    }

    public <T extends AssociatedObject> List<T> getLinks(Class<T> targetType, String id) {
        AssociationMetadata metadata = resolveByTargetAndId(this.getClass(), targetType, id);
        return Optional.ofNullable(associations.get(metadata))
                .map(links -> links.values().stream()
                        .map(AssociationEntry::target)
                        .map(targetType::cast)
                        .toList())
                .orElse(Collections.emptyList());
    }

    public <T extends AssociatedObject> Optional<T> findByQualifier(Class<T> targetType, Object qualifier) throws AssociationException {
        return findByQualifier(targetType, DEFAULT_ASSOCIATION_ID, qualifier);
    }

    public <T extends AssociatedObject> Optional<T> findByQualifier(Class<T> targetType, String id, Object qualifier) throws AssociationException {
        AssociationMetadata metadata = resolveByTargetAndId(this.getClass(), targetType, id);
        if (metadata.qualifier().isEmpty())
            throw new AssociationIsNotQualifiedException(targetType, id);

        return Optional.ofNullable(associations.get(metadata))
                .map(links -> links.get(qualifier))
                .map(AssociationEntry::target)
                .map(targetType::cast);
    }

    private Optional<Object> findQualifier(AssociationMetadata metadata, AssociatedObject target) {
        return metadata.qualifier()
                .flatMap(QualifierMetadata::qualifierField)
                .map(f -> {
                    try {
                        return f.get(target);
                    } catch (IllegalAccessException e) {
                        throw new IncorrectQualifierDeclaration(f);
                    }
                });
    }

    public void printAssociations() {
        System.out.println("\n=== Asocjacje obiektu: " + this.getClass().getSimpleName() + " ===");
        System.out.println("Stan: " + this);

        if (associations.isEmpty() || associations.values().stream().allMatch(Map::isEmpty)) {
            System.out.println("  (Brak aktywnych powiązań)");
            System.out.println("=============================================================\n");
            return;
        }

        associations.forEach((metadata, links) -> {
            if (links.isEmpty()) return;

            String idInfo = metadata.id().isEmpty() ? "domyślne ID" : "ID: '" + metadata.id() + "'";
            String payloadInfo = metadata.payloadType().isPresent() ? " | Payload: " + metadata.payloadType().get().getSimpleName() : "";

            System.out.printf(">> Relacja do: [%s] (%s%s)%n",
                    metadata.targetType().getSimpleName(), idInfo, payloadInfo);

            links.forEach((qualifierKey, entry) -> {
                String keyStr = (!qualifierKey.equals(entry.target())) ? " [Klucz/Kwalifikator: " + qualifierKey + "]" : "";

                System.out.println("   -> Cel" + keyStr + ": " + entry.target());

                if (!entry.payload().isEmpty()) {
                    System.out.println("      Klasa asocjacji (" + entry.payload().size() + "):");
                    entry.payload().forEach(payload -> System.out.println("       - " + payload));
                }
            });
        });
        System.out.println("=============================================================\n");
    }
}
