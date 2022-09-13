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
package cc.xfl12345.mybigdata.server.web.http;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Represents an HTTP/1.1 suffix byte range, with a number of suffix bytes.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7233#section-2.1">Byte Ranges</a>
 * @see org.springframework.http.HttpRange#createSuffixRange(long)
 */
public class SuffixByteRange extends HttpRange {

    public SuffixByteRange(String ranges) throws IllegalArgumentException {
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

    public SuffixByteRange(long suffixLength) {
        super(toString(suffixLength));
        if (suffixLength < 0) {
            throw new IllegalArgumentException("Invalid suffix length: " + suffixLength);
        }
        this.suffixLength = suffixLength;
    }

    @Override
    public long getRangeStart(long length) {
        if (this.suffixLength < length) {
            return length - this.suffixLength;
        } else {
            return 0;
        }
    }

    @Override
    public long getRangeEnd(long length) {
        return length - 1;
    }

    @Override
    protected void parseRange(String range) {
        Assert.hasLength(range, "Range String must not be empty");
        int dashIdx = range.indexOf('-');

        if (dashIdx > 0) {
            throw new IllegalArgumentException(
                "It may be an '" + ByteRange.class.getCanonicalName()
                    + "' object.Certainly not an '" +
                    SuffixByteRange.class.getCanonicalName()
                    + "' object."
            );
        } else if (dashIdx == 0) {
            suffixLength = Long.parseLong(range.substring(1));
        } else {
            throw new IllegalArgumentException("Range '" + range + "' does not contain \"-\"");
        }
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof SuffixByteRange otherRange) {
            return (this.suffixLength == otherRange.suffixLength);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.suffixLength);
    }

    @Override
    public String toString() {
        return toString(suffixLength);
    }

    protected static String toString(long suffixLength) {
        return "-" + suffixLength;
    }
}
