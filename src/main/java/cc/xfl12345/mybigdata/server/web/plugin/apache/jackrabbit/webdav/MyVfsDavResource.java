package cc.xfl12345.mybigdata.server.web.plugin.apache.jackrabbit.webdav;

import cc.xfl12345.mybigdata.server.web.http.HttpRange;
import cc.xfl12345.mybigdata.server.web.http.HttpRangeParser;
import com.github.alanger.webdav.VfsDavResource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.RandomAccessFileInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.RandomAccessContent;
import org.apache.commons.vfs2.provider.local.LocalFile;
import org.apache.commons.vfs2.util.RandomAccessMode;
import org.apache.http.HttpHeaders;
import org.apache.jackrabbit.webdav.*;
import org.apache.jackrabbit.webdav.io.OutputContext;
import org.apache.tika.Tika;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * See org.apache.jackrabbit.webdav.simple.DavResourceImpl
 */
@Slf4j
public class MyVfsDavResource extends VfsDavResource implements Closeable {
    protected volatile boolean keepGoing = true;

    protected ConcurrentHashMap<OutputStream, Object> spoolOutputStream = new ConcurrentHashMap<>();

    protected DavServletRequest davServletRequest;

    protected FileObject fileObject;

    protected static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

    protected static Tika tika = new Tika();

    @Getter
    @Setter
    protected int outputBufferSize = ((1 << 10) << 3); // ((1 << 10) << 3) == 8 KiB

    public MyVfsDavResource(DavResourceLocator locator, DavResourceFactory factory, DavSession session, boolean isCollection) throws DavException {
        super(locator, factory, session, isCollection);
    }

    public MyVfsDavResource(DavResourceLocator locator, DavResourceFactory factory, DavSession session, FileObject fileObject, DavServletRequest request) throws DavException {
        super(locator, factory, session, fileObject);
        this.fileObject = fileObject;
        davServletRequest = request;
    }

    public long skip(InputStream is, long n) throws IOException {
        long remaining = n;
        int nr;

        if (n <= 0) {
            return 0;
        }

        int size = (int) Math.min(outputBufferSize, remaining);
        byte[] skipBuffer = new byte[size];
        while (keepGoing && remaining > 0) {
            nr = is.read(skipBuffer, 0, (int) Math.min(size, remaining));
            if (nr < 0) {
                break;
            }
            remaining -= nr;
        }

        return n - remaining;
    }


    protected void sentRangeBytesLoop(InputStream is, OutputStream os, long rangeSize, boolean autoCloseInputStream) throws IOException {
        spoolOutputStream.put(os, new Object());

        try {
            byte[] buffer = new byte[outputBufferSize];
            int bytesRead;
            long toRead = rangeSize;
            while (keepGoing && (bytesRead = is.read(buffer)) > 0) {
                if (!keepGoing) {
                    break;
                }

                if ((toRead -= bytesRead) > 0) {
                    os.write(buffer, 0, bytesRead);
                    // os.flush();
                } else {
                    os.write(buffer, 0, (int) toRead + bytesRead);
                    // os.flush();
                    break;
                }
            }
        } finally {
            spoolOutputStream.remove(os);
            if (autoCloseInputStream) {
                is.close();
            }
        }
    }

    protected void sentRangeBytes(LocalFile file, OutputStream os, HttpRange httpRange, long fileSize) throws IOException {
        RandomAccessContent randomAccessContent = file.getRandomAccessContent(RandomAccessMode.READ);
        InputStream is = randomAccessContent.getInputStream();
        // 按 实际跳跃大小 作 起点
        long rangeStart = is.skip(httpRange.getRangeStart(fileSize));
        long rangeEnd = httpRange.getRangeEnd(fileSize);
        long rangeSize = rangeEnd - rangeStart + 1;

        sentRangeBytesLoop(is, os, rangeSize, true);
    }


    protected void sentRangeBytes(File file, OutputStream os, HttpRange httpRange, long fileSize) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        RandomAccessFileInputStream is = new RandomAccessFileInputStream(randomAccessFile, true);
        // 按 实际跳跃大小 作 起点
        long rangeStart = is.skip(httpRange.getRangeStart(fileSize));
        long rangeEnd = httpRange.getRangeEnd(fileSize);
        long rangeSize = rangeEnd - rangeStart + 1;

        sentRangeBytesLoop(is, os, rangeSize, true);

        is.close();
    }


    protected void sentRangeBytes(URL url, OutputStream os, HttpRange httpRange, long fileSize) throws IOException {
        sentRangeBytes(url.openStream(), os, httpRange, fileSize);
    }


    protected void sentRangeBytes(InputStream is, OutputStream os, HttpRange httpRange, long fileSize) throws IOException {
        is.reset();
        long httpRangeStart = httpRange.getRangeStart(fileSize);
        // 按 实际跳跃大小 作 起点
        long rangeStart = is instanceof FileInputStream ?
            is.skip(httpRangeStart) :
            skip(is, httpRangeStart);
        long rangeEnd = httpRange.getRangeEnd(fileSize);
        long rangeSize = rangeEnd - rangeStart + 1;

        sentRangeBytesLoop(is, os, rangeSize, true);
    }

    @Override
    public boolean exists() {
        try {
            return fileObject != null && fileObject.exists();
        } catch (FileSystemException e) {
            return false;
        }
    }

    @Override
    public void spool(OutputContext outputContext) throws IOException {
        if (!keepGoing || !exists() || isCollection() || outputContext == null) {
            return;
        }

        HttpServletResponse response = null;
        if (outputContext instanceof HttpServletResponseGetter responseGetter) {
            response = responseGetter.getHttpServletResponse();
        }

        FileContent fileContent = fileObject.getContent();
        long fileSize = fileContent.getSize();

        List<HttpRange> ranges = new ArrayList<>();
        String range = davServletRequest.getHeader("Content-Range");
        if (range == null) {
            range = davServletRequest.getHeader("Range");
        }
        if (!StringUtils.isEmpty(range)) {
            try {
                ranges = new HttpRangeParser().parseRanges(range);
            } catch (IllegalArgumentException exception) {
                if (response != null) {
                    response.setHeader("Content-Range", "bytes */" + fileSize); // Required in 416.
                    response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    return;
                } else {
                    throw new IOException(exception.getMessage());
                }
            }
        }

        LocalFile localFile = fileObject instanceof LocalFile ? ((LocalFile) fileObject) : null;
        URL fileURL = fileObject.getURL();

        if (keepGoing) {
            if (!ranges.isEmpty() && response != null) {
                ServletOutputStream os = response.getOutputStream();
                try (os) {
                    response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
                    String contentType = null;
                    try (InputStream inputStream = fileURL.openStream()) {
                        contentType = tika.detect(inputStream);
                    }
                    if (contentType == null) {
                        contentType = "application/octet-stream";
                    }

                    if (ranges.size() == 1) { // 单范围请求
                        HttpRange httpRange = ranges.get(0);
                        long rangeStart = httpRange.getRangeStart(fileSize);
                        long rangeEnd = httpRange.getRangeEnd(fileSize);
                        long rangeSize = rangeEnd - rangeStart + 1;
                        response.setContentType(contentType);
                        response.setContentLengthLong(rangeSize);
                        // response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");
                        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
                        // response.setHeader(
                        //     "Content-Disposition",
                        //     "inline;filename=" + fileObject.getName()
                        // );
                        response.setHeader(
                            HttpHeaders.CONTENT_RANGE,
                            "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize
                        );
                        if (localFile != null) {
                            sentRangeBytes(localFile, os, httpRange, fileSize);
                        } else {
                            sentRangeBytes(fileURL, os, httpRange, fileSize);
                        }
                    } else { // 多范围请求
                        response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
                        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

                        for (HttpRange httpRange : ranges) {
                            // 停机
                            if (!keepGoing) {
                                response.setStatus(HttpServletResponse.SC_GONE);
                                break;
                            }

                            long rangeStart = httpRange.getRangeStart(fileSize);
                            long rangeEnd = httpRange.getRangeEnd(fileSize);
//                            long rangeSize = rangeEnd - rangeStart + 1;
                            os.println();
                            os.println("--" + MULTIPART_BOUNDARY);
                            os.println("Content-Type: " + contentType);
                            os.println("Content-Range: bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);
                            if (localFile != null) {
                                sentRangeBytes(localFile, os, httpRange, fileSize);
                            } else {
                                sentRangeBytes(fileURL, os, httpRange, fileSize);
                            }
                        }

                        // End with multipart boundary.
                        os.println();
                        os.println("--" + MULTIPART_BOUNDARY + "--");
                    }
                }
            } else {
                OutputStream os = outputContext.getOutputStream();
                InputStream is = localFile == null ? fileContent.getInputStream() : localFile.getInputStream();
                try (is; os) {
                    outputContext.setProperty(HttpHeaders.ACCEPT_RANGES, "bytes");
                    outputContext.setContentLength(fileSize);
                    sentRangeBytesLoop(is, os, fileSize, true);
                }
            }
        }
    }

    @Override
    public void close() {
        keepGoing = false;
        OutputStream[] outputStreams = spoolOutputStream.keySet().toArray(OutputStream[]::new);
        try {
            log.debug("The living output stream of [" + fileObject.getURL().toString() + "] in total " + outputStreams.length);
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }
        for (OutputStream os : outputStreams) {
            try {
                log.debug("Closing the output stream of [" + fileObject.getURL().toString() + "] <---> " + os.toString());
                os.close();
            } catch (IOException ignored) {
            }
            spoolOutputStream.remove(os);
        }
    }
}

