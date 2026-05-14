package pl.edu.pjatk.mas.mp02.model.association;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@RequiredArgsConstructor
@Accessors(fluent = true)
class AssociationMetadata {
    private final Class<? extends AssociatedObject> targetType;
    private final String id;
    private final int min;
    private final int max;
    private final boolean isComposition;
    private final QualifierMetadata qualifier;
    private final Class<? extends Payload> payloadType;

    public static AssociationMetadata map(Association association) {
        return AssociationMetadata
                .builder()
                .id(association.id())
                .targetType(association.target())
                .isComposition(association.isComposition())
                .min(association.min())
                .max(association.max())
                .qualifier(getQualifier(association.qualifier()))
                .payloadType(getPayloadType(association))
                .build();
    }

    private static Class<? extends Payload> getPayloadType(Association association) {
        return association.payload().equals(None.class) ? null : association.payload();
    }

    private static QualifierMetadata getQualifier(Qualifier qualifier) {
        if (Objects.equals(qualifier.fieldName(), "") && Objects.equals(qualifier.type(), Void.class)) {
            return null;
        }
        return QualifierMetadata.builder()
                .fieldName(qualifier.fieldName())
                .type(qualifier.type())
                .build();
    }

    public Optional<QualifierMetadata> qualifier() {
        return Optional.ofNullable(qualifier);
    }

    public Optional<Class<? extends Payload>> payloadType() {
        return Optional.ofNullable(payloadType);
    }
}
