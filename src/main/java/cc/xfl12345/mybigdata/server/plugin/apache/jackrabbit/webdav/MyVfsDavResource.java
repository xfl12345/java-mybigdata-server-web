package cc.xfl12345.mybigdata.server.plugin.apache.jackrabbit.webdav;

import cc.xfl12345.mybigdata.server.http.HttpRange;
import cc.xfl12345.mybigdata.server.http.HttpRangeParser;
import cc.xfl12345.mybigdata.server.utility.EncodeUtils;
import com.github.alanger.webdav.VfsDavResource;
import lombok.Getter;
import lombok.Setter;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * See org.apache.jackrabbit.webdav.simple.DavResourceImpl
 */
public class MyVfsDavResource extends VfsDavResource {
//    private static Method vfsLocalFileGetLocalFile;
//
//    static {
//        try {
//            vfsLocalFileGetLocalFile = LocalFile.class.getDeclaredMethod("getLocalFile");
//            vfsLocalFileGetLocalFile.setAccessible(true);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//    }

    protected DavServletRequest davServletRequest;
    protected FileObject fileObject;
    //    protected List<HttpRange> ranges = new ArrayList<>();
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
        while (remaining > 0) {
            nr = is.read(skipBuffer, 0, (int) Math.min(size, remaining));
            if (nr < 0) {
                break;
            }
            remaining -= nr;
        }

        return n - remaining;
    }


    protected void sentRangeBytes(LocalFile file, OutputStream os, HttpRange httpRange, long fileSize) throws IOException {
        RandomAccessContent randomAccessContent = file.getRandomAccessContent(RandomAccessMode.READ);
        long rangeStart = httpRange.getRangeStart(fileSize);
        long rangeEnd = httpRange.getRangeEnd(fileSize);
        long rangeSize = rangeEnd - rangeStart + 1;
        byte[] buffer = new byte[outputBufferSize];
        int bytesRead;
        randomAccessContent.seek(rangeStart);
        InputStream is = randomAccessContent.getInputStream();
        long toRead = rangeSize;
        while ((bytesRead = is.read(buffer)) > 0) {
            if ((toRead -= bytesRead) > 0) {
                os.write(buffer, 0, bytesRead);
//                os.flush();
            } else {
                os.write(buffer, 0, (int) toRead + bytesRead);
//                os.flush();
                break;
            }
        }
    }


    protected void sentRangeBytes(File file, OutputStream os, HttpRange httpRange, long fileSize) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        long rangeStart = httpRange.getRangeStart(fileSize);
        long rangeEnd = httpRange.getRangeEnd(fileSize);
        long rangeSize = rangeEnd - rangeStart + 1;
        byte[] buffer = new byte[outputBufferSize];
        int bytesRead;
        randomAccessFile.seek(rangeStart);
        long toRead = rangeSize;
        while ((bytesRead = randomAccessFile.read(buffer)) > 0) {
            if ((toRead -= bytesRead) > 0) {
                os.write(buffer, 0, bytesRead);
//                os.flush();
            } else {
                os.write(buffer, 0, (int) toRead + bytesRead);
//                os.flush();
                break;
            }
        }
    }


    protected void sentRangeBytes(URL url, OutputStream os, HttpRange httpRange, long fileSize) throws IOException {
        InputStream is = url.openStream();
        try {
            sentRangeBytes(is, os, httpRange, fileSize);
        } catch (Exception ignored) {
        }
        is.close();
    }


    protected void sentRangeBytes(InputStream is, OutputStream os, HttpRange httpRange, long fileSize) throws IOException {
        long rangeStart = httpRange.getRangeStart(fileSize);
        long rangeEnd = httpRange.getRangeEnd(fileSize);
        long rangeSize = rangeEnd - rangeStart + 1;
        byte[] buffer = new byte[outputBufferSize];
//        long bytesReadCount = 0;
        int bytesRead;
        is.reset();
        long actualSkipBytesCount = is instanceof FileInputStream ?
            is.skip(rangeStart) :
            skip(is, rangeStart);
        long toRead = rangeSize;
        while ((bytesRead = is.read(buffer)) > 0) {
            if ((toRead -= bytesRead) > 0) {
                os.write(buffer, 0, bytesRead);
//                os.flush();
            } else {
                os.write(buffer, 0, (int) toRead + bytesRead);
//                os.flush();
                break;
            }
        }
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
        if (exists() && !isCollection() && outputContext != null) {
            HttpServletResponse response = null;
            if (outputContext instanceof HttpServletResponseGetter) {
                response = ((HttpServletResponseGetter) outputContext).getHttpServletResponse();
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

            LocalFile localFile = fileObject instanceof LocalFile ?
                ((LocalFile) fileObject): null;
            InputStream is = localFile == null ?
                fileContent.getInputStream() :
                localFile.getInputStream() ;
            if (!ranges.isEmpty() && response != null) {
                OutputStream os = response.getOutputStream();
                // Cast back to ServletOutputStream to get the easy println methods.
                ServletOutputStream sos = (ServletOutputStream) os;
                try (is; os; sos) {
                    response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
                    String contentType = tika.detect(is);
                    if (contentType == null) {
                        contentType = "application/octet-stream";
                    }

                    if (ranges.size() == 1) { // 单范围请求
                        HttpRange httpRange = ranges.get(0);
                        long rangeStart = httpRange.getRangeStart(fileSize);
                        long rangeEnd = httpRange.getRangeEnd(fileSize);
//                        // 如果客户端没有指定范围终点，则默认不一次性发全部，尝试发一小部分。
//                        // 诱引客户端支持断点续传、流媒体拖拽播放
//                        if (httpRange.getRangeEnd() == null) {
//                            long tmpRangeEnd = rangeStart + ((long) outputBufferSize << 10);
//                            if(tmpRangeEnd > fileSize) {
//                                tmpRangeEnd = fileSize;
//                            }
//                            rangeEnd = tmpRangeEnd;
//                        }

                        long rangeSize = rangeEnd - rangeStart + 1;
                        response.setContentType(contentType);
                        response.setContentLengthLong(rangeSize);
//                        response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");
                        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
//                        response.setHeader(
//                            "Content-Disposition",
//                            "inline;filename=" + fileObject.getName()
//                        );
                        response.setHeader(
                            HttpHeaders.CONTENT_RANGE,
                            "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize
                        );
                        if(localFile != null) {
//                            File file = null;
//                            file = new File(URI.create("file:" + EncodeUtils.encodeBracketsOnly4URL(localFile.getURL().getFile())));
                            sentRangeBytes(localFile, os, httpRange, fileSize);
//                            if (vfsLocalFileGetLocalFile != null) {
//                                try {
//                                    file = (File) vfsLocalFileGetLocalFile.invoke(localFile);
//                                    sentRangeBytes(file, os, httpRange, fileSize);
//                                } catch (IllegalAccessException | InvocationTargetException e) {
//                                    sentRangeBytes(is, os, httpRange, fileSize);
//                                }
//                            } else {
//                                sentRangeBytes(is, os, httpRange, fileSize);
//                            }
                        } else {
                            sentRangeBytes(is, os, httpRange, fileSize);
                        }
                    } else { // 多范围请求
                        response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
                        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

                        for (HttpRange httpRange : ranges) {
                            long rangeStart = httpRange.getRangeStart(fileSize);
                            long rangeEnd = httpRange.getRangeEnd(fileSize);
//                            long rangeSize = rangeEnd - rangeStart + 1;
                            sos.println();
                            sos.println("--" + MULTIPART_BOUNDARY);
                            sos.println("Content-Type: " + contentType);
                            sos.println("Content-Range: bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);
                            if(localFile != null) {
                                sentRangeBytes(localFile, os, httpRange, fileSize);
                            } else {
                                sentRangeBytes(is, os, httpRange, fileSize);
                            }
                        }

                        // End with multipart boundary.
                        sos.println();
                        sos.println("--" + MULTIPART_BOUNDARY + "--");
                    }
                }
            } else {
                OutputStream os = outputContext.getOutputStream();
                try (is; os) {
                    outputContext.setProperty(HttpHeaders.ACCEPT_RANGES, "bytes");
                    outputContext.setContentLength(fileSize);
                    if (os != null) { // HEAD method
                        byte[] buffer = new byte[outputBufferSize];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }


        }
    }
}

