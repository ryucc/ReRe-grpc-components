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

    @Override
    public boolean accept(Class<?> clazz) {
        return Status.class.isAssignableFrom(clazz);
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
