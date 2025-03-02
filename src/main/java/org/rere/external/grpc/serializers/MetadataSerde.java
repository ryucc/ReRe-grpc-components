package org.rere.external.grpc.serializers;

import io.grpc.Status;
import org.rere.core.serde.PrimitiveSerde;
import org.rere.core.serde.ReReSerde;
import org.rere.core.serde.exceptions.SerializationException;
import io.grpc.Metadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MetadataSerde implements ReReSerde {
    private static final PrimitiveSerde ps = new PrimitiveSerde();

    class StringMarshaller implements Metadata.AsciiMarshaller<String> {
        @Override
        public String toAsciiString(String s) {
            return s;
        }

        @Override
        public String parseAsciiString(String s) {
            return s;
        }
    }

    public String serialize(Object object) throws SerializationException {
        if (!(object instanceof Metadata)) {
            return "";
        }
        Metadata metadata = (Metadata) object;
        Map<String, String> m = new HashMap<>();
        Metadata.AsciiMarshaller<String> marshaller = new StringMarshaller();
        for(String key: metadata.keys()) {
            String val = metadata.get(Metadata.Key.of(key, marshaller));
            m.put(key, val);
        }
        return ps.serialize(m);
    }

    public Object deserialize(String serialization) {
        Map<String, String> m = (Map<String, String>) ps.deserialize(serialization);
        Metadata metadata = new Metadata();
        Metadata.AsciiMarshaller<String> marshaller = new StringMarshaller();
        for(String key: m.keySet()) {
            String val = m.get(key);
            metadata.put(Metadata.Key.of(key, marshaller), val);
        }
        return metadata;
    }
}
