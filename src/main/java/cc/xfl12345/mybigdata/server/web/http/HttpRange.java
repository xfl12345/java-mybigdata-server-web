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

/**
 * Represents an HTTP (byte) range for use with the HTTP {@code "Range"} header.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 4.2
 * @see <a href="https://tools.ietf.org/html/rfc7233">HTTP/1.1: Range Requests</a>
 */
public abstract class HttpRange {
    protected String originRangeInString;

    protected Long firstPos = null;

    @Nullable
    protected Long lastPos = null;

    protected Long suffixLength = null;

    public Long getRangeStart() {
        return firstPos;
    }

    public Long getRangeEnd() {
        return lastPos;
    }

    public Long getSuffixLength() {
        return suffixLength;
    }

    public HttpRange(String originRangeInString) throws IllegalArgumentException{
        this.originRangeInString = originRangeInString;
    }

    /**
     * Return the start of the range given the total length of a representation.
     * @param length the length of the representation
     * @return the start of this range for the representation
     */
    public abstract long getRangeStart(long length);

    /**
     * Return the end of the range (inclusive) given the total length of a representation.
     * @param length the length of the representation
     * @return the end of the range for the representation
     */
    public abstract long getRangeEnd(long length);

    protected abstract void parseRange(String range);


}
