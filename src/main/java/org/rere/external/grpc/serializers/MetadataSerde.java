/*
 * Copyright © 2025 ReRe contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.rere.external.grpc.serializers;

import io.grpc.Metadata;
import io.grpc.Status;
import org.rere.core.serde.PrimitiveSerde;
import org.rere.core.serde.ReReSerde;
import org.rere.core.serde.exceptions.SerializationException;

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
    @Override
    public boolean accept(Class<?> clazz) {
        return Metadata.class.isAssignableFrom(clazz);
    }

    public String serialize(Object object) throws SerializationException {
        if (!(object instanceof Metadata)) {
            return "";
        }
        Metadata metadata = (Metadata) object;
        Map<String, String> m = new HashMap<>();
        Metadata.AsciiMarshaller<String> marshaller = new StringMarshaller();
        for (String key : metadata.keys()) {
            String val = metadata.get(Metadata.Key.of(key, marshaller));
            m.put(key, val);
        }
        return ps.serialize(m);
    }

    public Object deserialize(String serialization) {
        Map<String, String> m = (Map<String, String>) ps.deserialize(serialization);
        Metadata metadata = new Metadata();
        Metadata.AsciiMarshaller<String> marshaller = new StringMarshaller();
        for (String key : m.keySet()) {
            String val = m.get(key);
            metadata.put(Metadata.Key.of(key, marshaller), val);
        }
        return metadata;
    }
}
