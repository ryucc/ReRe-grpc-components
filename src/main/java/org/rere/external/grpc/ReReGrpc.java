package org.rere.external.grpc;

import io.grpc.Metadata;
import io.grpc.Status;
import org.rere.api.ReReSettings;
import org.rere.external.grpc.serializers.MetadataSerde;
import org.rere.external.grpc.serializers.StatusSerde;

public class ReReGrpc {
    public static ReReSettings defaultSettings() {
        return new ReReSettings().withParameterModding(true)
                .registerSerializer(Status.class, StatusSerde.class)
                .registerSerializer(Metadata.class, MetadataSerde.class);
    }
}