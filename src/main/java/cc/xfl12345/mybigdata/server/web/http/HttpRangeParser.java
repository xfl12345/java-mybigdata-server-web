package cc.xfl12345.mybigdata.server.web.http;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

public class HttpRangeParser {
    public static int DEFAULT_MAX_RANGES = 100;
    public static String DEFAULT_BYTE_RANGE_PREFIX = "bytes=";

    /** Maximum ranges per request. */
    protected int maxRanges;
    protected String byteRangePrefix;

    public HttpRangeParser() {
        this(DEFAULT_BYTE_RANGE_PREFIX, DEFAULT_MAX_RANGES);
    }

    public HttpRangeParser(int maxRanges) {
        this(DEFAULT_BYTE_RANGE_PREFIX, maxRanges);
    }

    public HttpRangeParser(String byteRangePrefix) {
        this(byteRangePrefix, DEFAULT_MAX_RANGES);
    }

    public HttpRangeParser(String byteRangePrefix, int maxRanges) {
        this.byteRangePrefix = byteRangePrefix;
        this.maxRanges = maxRanges;
    }

    /**
     * Create an {@code HttpRange} from the given position to the end.
     * @param firstBytePos the first byte position
     * @return a byte range that ranges from {@code firstPos} till the end
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-2.1">Byte Ranges</a>
     */
    public HttpRange createByteRange(long firstBytePos) {
        return new ByteRange(firstBytePos, null);
    }

    /**
     * Create a {@code HttpRange} from the given fist to last position.
     * @param firstBytePos the first byte position
     * @param lastBytePos the last byte position
     * @return a byte range that ranges from {@code firstPos} till {@code lastPos}
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-2.1">Byte Ranges</a>
     */
    public HttpRange createByteRange(long firstBytePos, long lastBytePos) {
        return new ByteRange(firstBytePos, lastBytePos);
    }

    /**
     * Create an {@code HttpRange} that ranges over the last given number of bytes.
     * @param suffixLength the number of bytes for the range
     * @return a byte range that ranges over the last {@code suffixLength} number of bytes
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-2.1">Byte Ranges</a>
     */
    public HttpRange createSuffixRange(long suffixLength) {
        return new SuffixByteRange(suffixLength);
    }

    /**
     * Parse the given, comma-separated string into a list of {@code HttpRange} objects.
     * <p>This method can be used to parse an {@code Range} header.
     * @param ranges the string to parse
     * @return the list of ranges
     * @throws IllegalArgumentException if the string cannot be parsed
     * or if the number of ranges is greater than 100
     */
    public List<HttpRange> parseRanges(@Nullable String ranges) {
        if (!StringUtils.hasLength(ranges)) {
            return Collections.emptyList();
        }
        if (!ranges.startsWith(byteRangePrefix)) {
            throw new IllegalArgumentException("Range '" + ranges + "' does not start with 'bytes='");
        }
        ranges = ranges.substring(byteRangePrefix.length());

        String[] tokens = StringUtils.tokenizeToStringArray(ranges, ",");
        if (tokens.length > maxRanges) {
            throw new IllegalArgumentException("Too many ranges: " + tokens.length);
        }
        List<HttpRange> result = new ArrayList<>(tokens.length);
        for (String token : tokens) {
            result.add(parseRange(token));
        }
        return result;
    }

    protected HttpRange parseRange(String range) {
        Assert.hasLength(range, "Range String must not be empty");
        int dashIdx = range.indexOf('-');
        if (dashIdx > 0) {
            long firstPos = Long.parseLong(range.substring(0, dashIdx));
            if (dashIdx < range.length() - 1) {
                Long lastPos = Long.parseLong(range.substring(dashIdx + 1));
                return new ByteRange(firstPos, lastPos);
            }
            else {
                return new ByteRange(firstPos, null);
            }
        }
        else if (dashIdx == 0) {
            long suffixLength = Long.parseLong(range.substring(1));
            return new SuffixByteRange(suffixLength);
        }
        else {
            throw new IllegalArgumentException("Range '" + range + "' does not contain \"-\"");
        }
    }

    /**
     * Return a string representation of the given list of {@code HttpRange} objects.
     * <p>This method can be used to for an {@code Range} header.
     * @param ranges the ranges to create a string of
     * @return the string representation
     */
    public String toString(Collection<HttpRange> ranges) {
        Assert.notEmpty(ranges, "Ranges Collection must not be empty");
        StringJoiner builder = new StringJoiner(", ", byteRangePrefix, "");
        for (HttpRange range : ranges) {
            builder.add(range.toString());
        }
        return builder.toString();
    }
}
