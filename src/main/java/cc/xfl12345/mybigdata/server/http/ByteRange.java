/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.xfl12345.mybigdata.server.http;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Represents an HTTP/1.1 byte range, with a first and optional last position.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7233#section-2.1">Byte Ranges</a>
 * @see org.springframework.http.HttpRange#createByteRange(long)
 * @see org.springframework.http.HttpRange#createByteRange(long, long)
 */
public class ByteRange extends HttpRange {

    protected void parseRange(String range) {
        Assert.hasLength(range, "Range String must not be empty");
        int dashIdx = range.indexOf('-');
        if (dashIdx > 0) {
            this.firstPos = Long.parseLong(range.substring(0, dashIdx));
            if (dashIdx < range.length() - 1) {
                this.lastPos = Long.parseLong(range.substring(dashIdx + 1));
            }
        }
        else if (dashIdx == 0) {
            throw new IllegalArgumentException(
                "It may be an '" + SuffixByteRange.class.getCanonicalName()
                    + "' object.Certainly not an '" +
                    ByteRange.class.getCanonicalName()
                    + "' object."
            );
        } else {
            throw new IllegalArgumentException("Range '" + range + "' does not contain \"-\"");
        }
    }

    public ByteRange(String ranges) throws IllegalArgumentException {
        super(ranges);
        String byteRangePrefix = "bytes=";
        if (!StringUtils.hasLength(ranges) || !ranges.startsWith(byteRangePrefix)) {
            throw new IllegalArgumentException("Range '" + ranges + "' does not start with 'bytes='");
        }
        ranges = ranges.substring(byteRangePrefix.length());
        String[] tokens = StringUtils.tokenizeToStringArray(ranges, ",");
        if (tokens.length > 1) {
            throw new IllegalArgumentException("Too many ranges: " + tokens.length);
        }
        parseRange(tokens[0]);
    }

    public ByteRange(long firstPos, @Nullable Long lastPos) {
        super(toString(firstPos, lastPos));
        this.firstPos = firstPos;
        this.lastPos = lastPos;
    }

    private static void assertPositions(long firstBytePos, @Nullable Long lastBytePos) {
        if (firstBytePos < 0) {
            throw new IllegalArgumentException("Invalid first byte position: " + firstBytePos);
        }
        if (lastBytePos != null && lastBytePos < firstBytePos) {
            throw new IllegalArgumentException("firstBytePosition=" + firstBytePos +
                " should be less then or equal to lastBytePosition=" + lastBytePos);
        }
    }

    @Override
    public long getRangeStart(long length) {
        return this.firstPos;
    }

    @Override
    public long getRangeEnd(long length) {
        if (this.lastPos != null && this.lastPos < length) {
            return this.lastPos;
        } else {
            return length - 1;
        }
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ByteRange)) {
            return false;
        }
        ByteRange otherRange = (ByteRange) other;
        return (this.firstPos == otherRange.firstPos &&
            ObjectUtils.nullSafeEquals(this.lastPos, otherRange.lastPos));
    }

    @Override
    public int hashCode() {
        return (ObjectUtils.nullSafeHashCode(this.firstPos) * 31 +
            ObjectUtils.nullSafeHashCode(this.lastPos));
    }

    @Override
    public String toString() {
        return toString(firstPos, lastPos);
    }

    protected static String toString(long firstPos, Long lastPos) {
        assertPositions(firstPos, lastPos);
        StringBuilder builder = new StringBuilder();
        builder.append(firstPos);
        builder.append('-');
        if (lastPos != null) {
            builder.append(lastPos);
        }
        return builder.toString();
    }
}
