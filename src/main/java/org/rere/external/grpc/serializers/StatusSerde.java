package org.rere.external.grpc.serializers;

import io.grpc.Status;
import org.rere.core.serde.PrimitiveSerde;
import org.rere.core.serde.ReReSerde;
import org.rere.core.serde.exceptions.SerializationException;

import java.io.Serializable;

public class StatusSerde implements ReReSerde {
    private static final PrimitiveSerde ps = new PrimitiveSerde();

    public String serialize(Object object) throws SerializationException {
        if (!(object instanceof Status)) {
            return "";
        }
        Status status = (Status) object;
        Status.Code code = status.getCode();
        Throwable cause = status.getCause();
        String description = status.getDescription();
        StatusWrap wrap = new StatusWrap(code, cause, description);
        return ps.serialize(wrap);

    }

    public Object deserialize(String serialization) {
        StatusWrap wrap = (StatusWrap) ps.deserialize(serialization);
        return Status.fromCode(wrap.code).withCause(wrap.cause).withDescription(wrap.description);
    }

    public static class StatusWrap implements Serializable {
        public Status.Code code;
        public Throwable cause;
        public String description;

        public StatusWrap(Status.Code code, Throwable cause, String des) {
            this.code = code;
            this.cause = cause;
            this.description = des;
        }
    }
}
