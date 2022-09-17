/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.xfl12345.mybigdata.server.web.service;

import cc.xfl12345.mybigdata.server.common.utility.StringEscapeUtils;
import cc.xfl12345.mybigdata.server.web.plugin.apache.jackrabbit.webdav.MyOutputContextImpl;
import cc.xfl12345.mybigdata.server.web.plugin.apache.jackrabbit.webdav.MyVfsDavResource;
import cc.xfl12345.mybigdata.server.web.plugin.apache.jackrabbit.webdav.MyVfsDavResourceFactory;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import com.github.alanger.webdav.Text;
import com.github.alanger.webdav.VfsDavLocatorFactory;
import com.github.alanger.webdav.VfsDavSessionProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.jackrabbit.webdav.*;
import org.apache.jackrabbit.webdav.bind.BindInfo;
import org.apache.jackrabbit.webdav.bind.BindableResource;
import org.apache.jackrabbit.webdav.bind.RebindInfo;
import org.apache.jackrabbit.webdav.bind.UnbindInfo;
import org.apache.jackrabbit.webdav.header.CodedUrlHeader;
import org.apache.jackrabbit.webdav.io.InputContext;
import org.apache.jackrabbit.webdav.io.InputContextImpl;
import org.apache.jackrabbit.webdav.io.OutputContext;
import org.apache.jackrabbit.webdav.lock.*;
import org.apache.jackrabbit.webdav.observation.EventDiscovery;
import org.apache.jackrabbit.webdav.observation.ObservationResource;
import org.apache.jackrabbit.webdav.observation.Subscription;
import org.apache.jackrabbit.webdav.observation.SubscriptionInfo;
import org.apache.jackrabbit.webdav.ordering.OrderPatch;
import org.apache.jackrabbit.webdav.ordering.OrderingResource;
import org.apache.jackrabbit.webdav.property.*;
import org.apache.jackrabbit.webdav.search.SearchConstants;
import org.apache.jackrabbit.webdav.search.SearchInfo;
import org.apache.jackrabbit.webdav.search.SearchResource;
import org.apache.jackrabbit.webdav.security.AclProperty;
import org.apache.jackrabbit.webdav.security.AclResource;
import org.apache.jackrabbit.webdav.transaction.TransactionInfo;
import org.apache.jackrabbit.webdav.transaction.TransactionResource;
import org.apache.jackrabbit.webdav.util.HttpDateTimeFormatter;
import org.apache.jackrabbit.webdav.version.*;
import org.apache.jackrabbit.webdav.version.report.Report;
import org.apache.jackrabbit.webdav.version.report.ReportInfo;
import org.apache.jackrabbit.webdav.xml.DomUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.text.DateFormat;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class VfsWebDavService implements ApplicationListener<ContextClosedEvent>, InitializingBean, DavConstants, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final String VERSION = HttpServlet.class.getPackage().getImplementationVersion() != null
        ? HttpServlet.class.getPackage().getImplementationVersion()
        : "unknown";
    public static final String INIT_PARAM_ROOTPATH = "rootPath";

    String initFailedMessage = "Service '" + this + "' initialization error";

    /**
     * Create per default absolute URI hrefs
     */
    @Setter
    protected boolean createAbsoluteURI = true;

    @Getter
    @Setter
    protected boolean listingsDirectory = true;

    @Getter
    @Setter
    protected boolean includeContextPath = true;

    @Getter
    protected List<String> auditMethods = null;

    @Getter
    @Setter
    protected DavSessionProvider davSessionProvider = null;

    @Getter
    @Setter
    protected DavLocatorFactory locatorFactory = null;

    @Getter
    @Setter
    protected LockManager lockManager = new SimpleLockManager();

    @Getter
    protected DavResourceFactory resourceFactory = null;

    @Getter
    @Setter
    protected FileSystemOptions fileSystemOptions = null;

    @Getter
    @Setter
    protected FileSystemManager fileSystemManager;

    protected FileObject rootPathFileObject;

    @Getter
    @Setter
    protected boolean enableServer = true;

    @Getter
    protected String rootPath = null;

    public void setRootPath(String rootPath) throws Exception {
        if (rootPath == null)
            throw new IllegalArgumentException(initFailedMessage + ", init parameter '" + INIT_PARAM_ROOTPATH + "' is required.");

        // 限制只能使用本地文件系统
        URI rootPathURI = new File(rootPath).toURI();

        FileObject root = getFileSystemManager().resolveFile(
            rootPathURI.toString(),
            getFileSystemOptions()
        );
        if (root == null || !root.exists())
            throw new Exception(initFailedMessage + ", VFS root file not exist or null");
        this.rootPath = rootPath;
        this.rootPathFileObject = root;
        resourceFactory = new MyVfsDavResourceFactory(getLockManager(), this.rootPathFileObject);
    }

    public void setAuditMethods(List<String> auditMethods) {
        this.auditMethods = auditMethods;
    }

    public void setAuditMethods(@NonNull String auditMethodsStr) {
        this.auditMethods = Arrays.asList(auditMethodsStr.split(","));
    }

    public void afterPropertiesSet() throws Exception {
        if (rootPath == null) {
            setRootPath(System.getProperty("java.io.tmpdir"));
        }

        if (fileSystemOptions == null) {
            throw new IllegalArgumentException("The 'fileSystemOptions' param is required.");
        }

        if (davSessionProvider == null) {
            davSessionProvider = new VfsDavSessionProvider();
        }

        if (locatorFactory == null) {
//            locatorFactory = new VfsDavLocatorFactory(getServletName());
            locatorFactory = new VfsDavLocatorFactory("");
        }

        if (lockManager == null) {
            lockManager = new SimpleLockManager();
        }


        if (fileSystemManager == null) {
            fileSystemManager = VFS.getManager();
        }

        log.info("Init service: {}, rootpath: {}, listingsDirectory: {}, version: {}",
            this,
            rootPathFileObject.getPublicURIString(),
            listingsDirectory,
            VERSION
        );
    }


    protected NoArgGenerator uuidGenerator = Generators.timeBasedGenerator();

    protected ConcurrentHashMap<MyVfsDavResource, ConcurrentHashMap<UUID, Thread>> spoolThreads = new ConcurrentHashMap<>();

    protected volatile boolean keepGoing = true;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        keepGoing = false;

        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }

        log.info("Start to close davResource.");

        for (MyVfsDavResource davResource : spoolThreads.keySet().parallelStream().toList()) {
            davResource.close();
            ConcurrentHashMap<UUID, Thread> threadMap = spoolThreads.get(davResource);
            if (threadMap != null) {
                // threadMap.values().forEach(item -> {
                //     try {
                //         item.join();
                //     } catch (InterruptedException e) {
                //         throw new RuntimeException(e);
                //     }
                // });
                threadMap.clear();
            }
            spoolThreads.remove(davResource);
        }

        spoolThreads.clear();

        log.info("Destroy service: {}, rootpath: {}, listingsDirectory: {}, version: {}",
            this,
            rootPathFileObject != null ? rootPathFileObject.getPublicURIString() : rootPathFileObject,
            listingsDirectory,
            VERSION
        );
    }


    protected boolean isPreconditionValid(WebdavRequest request, DavResource resource) {
        return resource != null && (!resource.exists() || request.matchesIfHeader(resource));
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!enableServer || !keepGoing) {
            response.setStatus(HttpServletResponse.SC_GONE);
            return;
        }

        try {
            // Include servlet path to prefix
            if (includeContextPath) {
                HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getContextPath() {
                        return request.getContextPath() + request.getServletPath();
                    }
                };
                service2(wrapper, response);
            } else {
                service2(request, response);
            }
        } finally {
            if (auditMethods != null && auditMethods.contains(request.getMethod())) {
                String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName()
                    : request.getRemoteUser();
                String id = request.getSession(false) != null ? request.getSession(false).getId() : null;
                String requestURI = Text.unescape(request.getRequestURI());
                String dest = request.getHeader("Destination");
                dest = (dest != null) ? Text.unescape(dest.replaceAll("^http.*://[^/]*+", "")) : null;
                String msg = response.getStatus() + " " + request.getMethod() + " : " + requestURI
                    + (dest != null ? ", Destination: " + dest : "") + ", User: " + user + ", ID: " + id
                    + ", Addr: " + request.getRemoteAddr();
                log.info(msg);
            }
        }
    }

    protected String getProperty(String key) {
        if (key != null && key.startsWith("${") && key.endsWith("}")) {
            key = System.getProperty(key.substring(2, key.length() - 1));
        }
        return key;
    }

    private static final DateFormat shortDF = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
    private static final String CSS;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("h1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} ");
        sb.append("h2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} ");
        sb.append("h3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} ");
        sb.append("body {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} ");
        sb.append("b {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} ");
        sb.append("p {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;} ");
        sb.append("a {color:black;} a.name {color:black;} ");
        sb.append(".line {height:1px;background-color:#525D76;border:none;}");
        CSS = sb.toString();
    }

    protected void printDirectory(WebdavRequest request, WebdavResponse response, DavResource resource)
        throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>").append(request.getServletContext().getServletContextName()).append("</title>");
        sb.append("<link rel=\"SHORTCUT ICON\" href=\"data:image/png;base64,XXXXX\">");
        sb.append("<style type=\"text/css\">");
        sb.append(getCSS());
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");

        String resourcePath = resource.getResourcePath().endsWith("/") ? resource.getResourcePath()
            : resource.getResourcePath() + "/";
        sb.append("<h2>Content of folder: " + resourcePath + "</h2>");

        sb.append("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"5\" align=\"center\">\r\n");

        final String tdHeadStart = "<td align=\"left\"><font size=\"+1\"><strong>";
        final String tdHeadEnd = "</strong></font></td>\r\n";

        // Head table
        sb.append("<tr>\r\n");
        sb.append(tdHeadStart);
        sb.append("Name");
        sb.append(tdHeadEnd);
        sb.append(tdHeadStart);
        sb.append("Size");
        sb.append(tdHeadEnd);
        sb.append(tdHeadStart);
        sb.append("Type");
        sb.append(tdHeadEnd);
        sb.append(tdHeadStart);
        sb.append("Modified");
        sb.append(tdHeadEnd);
        sb.append("</tr>");

        sb.append("<tr><td colspan=\"4\"><a href=\"../\"><tt>[Parent]</tt></a></td></tr>");

        String baseDir = request.getRequestURI().endsWith("/") ? request.getRequestURI()
            : request.getRequestURI() + "/";

        boolean isEven = false;
        Iterator<DavResource> resources = resource.getMembers();

        // Sort list
        ArrayList<DavResource> list = new ArrayList<>();
        resources.forEachRemaining(list::add);

        Collections.sort(list, (d1, d2) -> {
            return d1.getDisplayName().compareTo(d2.getDisplayName());
        });

        for (DavResource res : list) {
            isEven = !isEven;
            long lastModified = res.getModificationTime();
            boolean isDir = res.isCollection();
            String name = !isDir ? res.getDisplayName() : res.getDisplayName() + "/";

            // Striped table
            sb.append("<tr " + (isEven ? "bgcolor=\"#eeeeee\"" : "") + "\">");

            // Name column
            sb.append("<td>").append("<a href=\"");
            sb.append(StringEscapeUtils.escapeBracketsOnly4URL(baseDir + name));
            sb.append("\"><tt>");
            sb.append(name);
            sb.append("</tt></a></td>");

            final String tdStart = "<td><tt>";
            final String tdEnd = "</tt></td>";

            // Size column
            if (isDir) {
                sb.append(tdStart);
                sb.append("Folder");
                sb.append(tdEnd);
            } else {
                DavProperty<?> prop = res.getProperty(DavPropertyName.GETCONTENTLENGTH);
                String value = prop != null ? (String) prop.getValue() : null;
                long length = value != null ? Long.valueOf(value) : 0L;
                sb.append(tdStart).append(renderSize(length)).append(tdEnd);
            }

            // MIME type column
            if (isDir) {
                sb.append(tdStart);
                sb.append("-");
                sb.append(tdEnd);
            } else {
                sb.append(tdStart);
                DavProperty<?> prop = res.getProperty(DavPropertyName.GETCONTENTTYPE);
                String mimeType = prop != null ? (String) prop.getValue() : "Unknown type";
                sb.append(mimeType);
                sb.append(tdEnd);
            }

            // Date column
            sb.append(tdStart);
            sb.append(shortDF.format(lastModified));
            sb.append(tdEnd);

            sb.append("</tr>");
        }

        sb.append("</table>");
        sb.append("<h3 id=\"version\" value=\"" + VERSION + "\">Application version: " + VERSION + "</h3>");
        sb.append("</body></html>");

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().print(sb.toString());
    }

    protected String getCSS() {
        return CSS;
    }

    protected String renderSize(long size) {
        long leftSide = size / 1024;
        long rightSide = (size % 1024) / 103; // Makes 1 digit
        if ((leftSide == 0) && (rightSide == 0) && (size > 0))
            rightSide = 1;
        return ("" + leftSide + "." + rightSide + " kb");
    }

    protected void doGet(WebdavRequest request, WebdavResponse response, DavResource resource)
        throws IOException, DavException {
        if (listingsDirectory && resource.exists() && resource.isCollection()) {
            printDirectory(request, response, resource);
            return;
        }
        spoolResource(request, response, resource, true);
    }


    /**
     * Returns if a absolute URI should be created for hrefs.
     *
     * @return absolute URI hrefs
     */
    protected boolean isCreateAbsoluteURI() {
        return createAbsoluteURI;
    }

    /**
     * Service the given request.
     */
    protected void service2(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        WebdavRequest webdavRequest = new WebdavRequestImpl(request, getLocatorFactory(), isCreateAbsoluteURI());
        // DeltaV requires 'Cache-Control' header for all methods except 'VERSION-CONTROL' and 'REPORT'.
        int methodCode = DavMethods.getMethodCode(request.getMethod());
        boolean noCache = DavMethods.isDeltaVMethod(webdavRequest) && !(DavMethods.DAV_VERSION_CONTROL == methodCode || DavMethods.DAV_REPORT == methodCode);
        WebdavResponse webdavResponse = new WebdavResponseImpl(response, noCache);

        try {

            // make sure there is a authenticated user
            if (!getDavSessionProvider().attachSession(webdavRequest)) {
                return;
            }

            // check matching if=header for lock-token relevant operations
            DavResource resource = getResourceFactory().createResource(webdavRequest.getRequestLocator(), webdavRequest, webdavResponse);
            if (!isPreconditionValid(webdavRequest, resource)) {
                webdavResponse.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
                return;
            }
            if (!execute(webdavRequest, webdavResponse, methodCode, resource)) {
                webdavResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (DavException e) {
            handleDavException(webdavRequest, webdavResponse, e);
        } catch (IOException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof DavException) {
                handleDavException(webdavRequest, webdavResponse, (DavException) cause);
            } else {
                throw ex;
            }
        } finally {
            getDavSessionProvider().releaseSession(webdavRequest);
        }
    }

    private void handleDavException(WebdavRequest webdavRequest, WebdavResponse webdavResponse, DavException ex)
        throws IOException {
        Element condition = ex.getErrorCondition();
        if (DomUtil.matches(condition, ContentCodingAwareRequest.PRECONDITION_SUPPORTED)) {
            if (webdavRequest instanceof ContentCodingAwareRequest) {
                webdavResponse.setHeader("Accept-Encoding", ((ContentCodingAwareRequest) webdavRequest).getAcceptableCodings());
            }
        }
        webdavResponse.sendError(ex);
    }

    /**
     * If request payload was uncompressed, hint about acceptable content codings (RFC 7694)
     */
    private void addHintAboutPotentialRequestEncodings(WebdavRequest webdavRequest, WebdavResponse webdavResponse) {
        if (webdavRequest instanceof ContentCodingAwareRequest) {
            ContentCodingAwareRequest ccr = (ContentCodingAwareRequest) webdavRequest;
            List<String> ces = ccr.getRequestContentCodings();
            if (ces.isEmpty()) {
                webdavResponse.setHeader("Accept-Encoding", ccr.getAcceptableCodings());
            }
        }
    }


    /**
     * Executes the respective method in the given webdav context
     *
     * @param request
     * @param response
     * @param method
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected boolean execute(WebdavRequest request, WebdavResponse response,
                              int method, DavResource resource)
        throws IOException, DavException {

        switch (method) {
            case DavMethods.DAV_GET:
                doGet(request, response, resource);
                break;
            case DavMethods.DAV_HEAD:
                doHead(request, response, resource);
                break;
            case DavMethods.DAV_PROPFIND:
                doPropFind(request, response, resource);
                break;
            case DavMethods.DAV_PROPPATCH:
                doPropPatch(request, response, resource);
                break;
            case DavMethods.DAV_POST:
                doPost(request, response, resource);
                break;
            case DavMethods.DAV_PUT:
                doPut(request, response, resource);
                break;
            case DavMethods.DAV_DELETE:
                doDelete(request, response, resource);
                break;
            case DavMethods.DAV_COPY:
                doCopy(request, response, resource);
                break;
            case DavMethods.DAV_MOVE:
                doMove(request, response, resource);
                break;
            case DavMethods.DAV_MKCOL:
                doMkCol(request, response, resource);
                break;
            case DavMethods.DAV_OPTIONS:
                doOptions(request, response, resource);
                break;
            case DavMethods.DAV_LOCK:
                doLock(request, response, resource);
                break;
            case DavMethods.DAV_UNLOCK:
                doUnlock(request, response, resource);
                break;
            case DavMethods.DAV_ORDERPATCH:
                doOrderPatch(request, response, resource);
                break;
            case DavMethods.DAV_SUBSCRIBE:
                doSubscribe(request, response, resource);
                break;
            case DavMethods.DAV_UNSUBSCRIBE:
                doUnsubscribe(request, response, resource);
                break;
            case DavMethods.DAV_POLL:
                doPoll(request, response, resource);
                break;
            case DavMethods.DAV_SEARCH:
                doSearch(request, response, resource);
                break;
            case DavMethods.DAV_VERSION_CONTROL:
                doVersionControl(request, response, resource);
                break;
            case DavMethods.DAV_LABEL:
                doLabel(request, response, resource);
                break;
            case DavMethods.DAV_REPORT:
                doReport(request, response, resource);
                break;
            case DavMethods.DAV_CHECKIN:
                doCheckin(request, response, resource);
                break;
            case DavMethods.DAV_CHECKOUT:
                doCheckout(request, response, resource);
                break;
            case DavMethods.DAV_UNCHECKOUT:
                doUncheckout(request, response, resource);
                break;
            case DavMethods.DAV_MERGE:
                doMerge(request, response, resource);
                break;
            case DavMethods.DAV_UPDATE:
                doUpdate(request, response, resource);
                break;
            case DavMethods.DAV_MKWORKSPACE:
                doMkWorkspace(request, response, resource);
                break;
            case DavMethods.DAV_MKACTIVITY:
                doMkActivity(request, response, resource);
                break;
            case DavMethods.DAV_BASELINE_CONTROL:
                doBaselineControl(request, response, resource);
                break;
            case DavMethods.DAV_ACL:
                doAcl(request, response, resource);
                break;
            case DavMethods.DAV_REBIND:
                doRebind(request, response, resource);
                break;
            case DavMethods.DAV_UNBIND:
                doUnbind(request, response, resource);
                break;
            case DavMethods.DAV_BIND:
                doBind(request, response, resource);
                break;
            default:
                // any other method
                return false;
        }
        return true;
    }

    /**
     * The OPTION method
     *
     * @param request
     * @param response
     * @param resource
     */
    protected void doOptions(WebdavRequest request, WebdavResponse response,
                             DavResource resource) throws IOException, DavException {
        response.addHeader(DavConstants.HEADER_DAV, resource.getComplianceClass());
        response.addHeader("Allow", resource.getSupportedMethods());
        response.addHeader("MS-Author-Via", DavConstants.HEADER_DAV);
        if (resource instanceof SearchResource) {
            String[] langs = ((SearchResource) resource).getQueryGrammerSet().getQueryLanguages();
            for (String lang : langs) {
                response.addHeader(SearchConstants.HEADER_DASL, "<" + lang + ">");
            }
        }
        // with DeltaV the OPTIONS request may contain a Xml body.
        OptionsResponse oR = null;
        OptionsInfo oInfo = request.getOptionsInfo();
        if (oInfo != null && resource instanceof DeltaVResource) {
            oR = ((DeltaVResource) resource).getOptionResponse(oInfo);
        }
        if (oR == null) {
            response.setStatus(DavServletResponse.SC_OK);
        } else {
            response.sendXmlResponse(oR, DavServletResponse.SC_OK);
        }
    }

    /**
     * The HEAD method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     */
    protected void doHead(WebdavRequest request, WebdavResponse response,
                          DavResource resource) throws IOException {
        spoolResource(request, response, resource, false);
    }

    /**
     * @param request
     * @param response
     * @param resource
     * @param sendContent
     * @throws IOException
     */
    private void spoolResource(WebdavRequest request, WebdavResponse response,
                               DavResource resource, boolean sendContent)
        throws IOException {

        if (!resource.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        long modSince = UNDEFINED_TIME;
        try {
            // will throw if multiple field lines present
            String value = getSingletonField(request, "If-Modified-Since");
            if (value != null) {
                modSince = HttpDateTimeFormatter.parse(value);
            }
        } catch (IllegalArgumentException | DateTimeParseException ex) {
            log.debug("illegal value for if-modified-since ignored: " + ex.getMessage());
        }

        if (modSince > UNDEFINED_TIME) {
            long modTime = resource.getModificationTime();
            // test if resource has been modified. note that formatted modification
            // time lost the milli-second precision
            if (modTime != UNDEFINED_TIME && (modTime / 1000 * 1000) <= modSince) {
                // resource has not been modified since the time indicated in the
                // 'If-Modified-Since' header.

                DavProperty<?> etagProp = resource.getProperty(DavPropertyName.GETETAG);
                if (etagProp != null) {
                    // 304 response MUST contain Etag when available
                    response.setHeader("etag", etagProp.getValue().toString());
                }
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
        }


        Thread thread = Thread.currentThread();
        MyVfsDavResource davResource = (MyVfsDavResource) resource;
        ConcurrentHashMap<UUID, Thread> threadMap = spoolThreads.putIfAbsent(davResource, new ConcurrentHashMap<>());
        if (threadMap == null) {
            threadMap = spoolThreads.get(davResource);
        }

        UUID uuid;
        do {
            uuid = uuidGenerator.generate();
        } while (keepGoing && threadMap.putIfAbsent(uuid, thread) != null);

        if (!keepGoing) {
            threadMap.remove(uuid);
            return;
        }

        try {
            // spool resource properties and eventually resource content.
            OutputStream out = (sendContent) ? response.getOutputStream() : null;
            resource.spool(getOutputContext(response, out));
            response.flushBuffer();
        } finally {
            threadMap.remove(uuid);
        }
    }

    /**
     * The PROPFIND method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     */
    protected void doPropFind(WebdavRequest request, WebdavResponse response,
                              DavResource resource) throws IOException, DavException {

        if (!resource.exists()) {
            response.sendError(DavServletResponse.SC_NOT_FOUND);
            return;
        }

        int depth = request.getDepth(DEPTH_INFINITY);
        DavPropertyNameSet requestProperties = request.getPropFindProperties();
        int propfindType = request.getPropFindType();

        MultiStatus mstatus = new MultiStatus();
        mstatus.addResourceProperties(resource, requestProperties, propfindType, depth);

        addHintAboutPotentialRequestEncodings(request, response);

        response.sendMultiStatus(mstatus,
            acceptsGzipEncoding(request) ? Collections.singletonList("gzip") : Collections.emptyList());
    }

    /**
     * The PROPPATCH method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     */
    protected void doPropPatch(WebdavRequest request, WebdavResponse response,
                               DavResource resource)
        throws IOException, DavException {

        List<? extends PropEntry> changeList = request.getPropPatchChangeList();
        if (changeList.isEmpty()) {
            response.sendError(DavServletResponse.SC_BAD_REQUEST);
            return;
        }

        MultiStatus ms = new MultiStatus();
        MultiStatusResponse msr = resource.alterProperties(changeList);
        ms.addResponse(msr);

        addHintAboutPotentialRequestEncodings(request, response);

        response.sendMultiStatus(ms);
    }

    /**
     * The POST method. Delegate to PUT
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doPost(WebdavRequest request, WebdavResponse response,
                          DavResource resource) throws IOException, DavException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /**
     * The PUT method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doPut(WebdavRequest request, WebdavResponse response,
                         DavResource resource) throws IOException, DavException {

        if (request.getHeader("Content-Range") != null) {
            response.sendError(DavServletResponse.SC_BAD_REQUEST, "Content-Range in PUT request not supported");
            return;
        }

        DavResource parentResource = resource.getCollection();
        if (parentResource == null || !parentResource.exists()) {
            // parent does not exist
            response.sendError(DavServletResponse.SC_CONFLICT);
            return;
        }

        int status;
        // test if resource already exists
        if (resource.exists()) {
            status = DavServletResponse.SC_NO_CONTENT;
        } else {
            status = DavServletResponse.SC_CREATED;
        }

        parentResource.addMember(resource, getInputContext(request, request.getInputStream()));
        response.setStatus(status);
    }

    /**
     * The MKCOL method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doMkCol(WebdavRequest request, WebdavResponse response,
                           DavResource resource) throws IOException, DavException {

        DavResource parentResource = resource.getCollection();
        if (parentResource == null || !parentResource.exists() || !parentResource.isCollection()) {
            // parent does not exist or is not a collection
            response.sendError(DavServletResponse.SC_CONFLICT);
            return;
        }
        // shortcut: mkcol is only allowed on deleted/non-existing resources
        if (resource.exists()) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        if (request.getContentLength() > 0 || request.getHeader("Transfer-Encoding") != null) {
            parentResource.addMember(resource, getInputContext(request, request.getInputStream()));
        } else {
            parentResource.addMember(resource, getInputContext(request, null));
        }
        response.setStatus(DavServletResponse.SC_CREATED);
    }

    /**
     * The DELETE method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doDelete(WebdavRequest request, WebdavResponse response,
                            DavResource resource) throws IOException, DavException {
        DavResource parent = resource.getCollection();
        if (parent != null) {
            parent.removeMember(resource);
            response.setStatus(DavServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(DavServletResponse.SC_FORBIDDEN, "Cannot remove the root resource.");
        }
    }

    /**
     * The COPY method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doCopy(WebdavRequest request, WebdavResponse response,
                          DavResource resource) throws IOException, DavException {

        // only depth 0 and infinity is allowed
        int depth = request.getDepth(DEPTH_INFINITY);
        if (!(depth == DEPTH_0 || depth == DEPTH_INFINITY)) {
            response.sendError(DavServletResponse.SC_BAD_REQUEST);
            return;
        }

        DavResource destResource = getResourceFactory().createResource(request.getDestinationLocator(), request, response);
        int status = validateDestination(destResource, request, true);
        if (status > DavServletResponse.SC_NO_CONTENT) {
            response.sendError(status);
            return;
        }

        resource.copy(destResource, depth == DEPTH_0);
        response.setStatus(status);
    }

    /**
     * The MOVE method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doMove(WebdavRequest request, WebdavResponse response,
                          DavResource resource) throws IOException, DavException {

        DavResource destResource = getResourceFactory().createResource(request.getDestinationLocator(), request, response);
        int status = validateDestination(destResource, request, true);
        if (status > DavServletResponse.SC_NO_CONTENT) {
            response.sendError(status);
            return;
        }

        resource.move(destResource);
        response.setStatus(status);
    }

    /**
     * The BIND method
     *
     * @param request
     * @param response
     * @param resource the collection resource to which a new member will be added
     * @throws IOException
     * @throws DavException
     */
    protected void doBind(WebdavRequest request, WebdavResponse response,
                          DavResource resource) throws IOException, DavException {

        if (!resource.exists()) {
            response.sendError(DavServletResponse.SC_NOT_FOUND);
        }
        BindInfo bindInfo = request.getBindInfo();
        DavResource oldBinding = getResourceFactory().createResource(request.getHrefLocator(bindInfo.getHref()), request, response);
        if (!(oldBinding instanceof BindableResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        DavResource newBinding = getResourceFactory().createResource(request.getMemberLocator(bindInfo.getSegment()), request, response);
        int status = validateDestination(newBinding, request, false);
        if (status > DavServletResponse.SC_NO_CONTENT) {
            response.sendError(status);
            return;
        }
        ((BindableResource) oldBinding).bind(resource, newBinding);
        response.setStatus(status);
    }

    /**
     * The REBIND method
     *
     * @param request
     * @param response
     * @param resource the collection resource to which a new member will be added
     * @throws IOException
     * @throws DavException
     */
    protected void doRebind(WebdavRequest request, WebdavResponse response,
                            DavResource resource) throws IOException, DavException {

        if (!resource.exists()) {
            response.sendError(DavServletResponse.SC_NOT_FOUND);
        }
        RebindInfo rebindInfo = request.getRebindInfo();
        DavResource oldBinding = getResourceFactory().createResource(request.getHrefLocator(rebindInfo.getHref()), request, response);
        if (!(oldBinding instanceof BindableResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        DavResource newBinding = getResourceFactory().createResource(request.getMemberLocator(rebindInfo.getSegment()), request, response);
        int status = validateDestination(newBinding, request, false);
        if (status > DavServletResponse.SC_NO_CONTENT) {
            response.sendError(status);
            return;
        }
        ((BindableResource) oldBinding).rebind(resource, newBinding);
        response.setStatus(status);
    }

    /**
     * The UNBIND method
     *
     * @param request
     * @param response
     * @param resource the collection resource from which a member will be removed
     * @throws IOException
     * @throws DavException
     */
    protected void doUnbind(WebdavRequest request, WebdavResponse response,
                            DavResource resource) throws IOException, DavException {

        UnbindInfo unbindInfo = request.getUnbindInfo();
        DavResource srcResource = getResourceFactory().createResource(request.getMemberLocator(unbindInfo.getSegment()), request, response);
        resource.removeMember(srcResource);
    }

    /**
     * Validate the given destination resource and return the proper status
     * code: Any return value greater/equal than {@link DavServletResponse#SC_NO_CONTENT}
     * indicates an error.
     *
     * @param destResource destination resource to be validated.
     * @param request
     * @param checkHeader  flag indicating if the destination header must be present.
     * @return status code indicating whether the destination is valid.
     */
    protected int validateDestination(DavResource destResource, WebdavRequest request, boolean checkHeader)
        throws DavException {

        if (checkHeader) {
            String destHeader = request.getHeader(HEADER_DESTINATION);
            if (destHeader == null || "".equals(destHeader)) {
                return DavServletResponse.SC_BAD_REQUEST;
            }
        }
        if (destResource.getLocator().equals(request.getRequestLocator())) {
            return DavServletResponse.SC_FORBIDDEN;
        }

        int status;
        if (destResource.exists()) {
            if (request.isOverwrite()) {
                // matching if-header required for existing resources
                if (!request.matchesIfHeader(destResource)) {
                    return DavServletResponse.SC_PRECONDITION_FAILED;
                } else {
                    // overwrite existing resource
                    DavResource col;
                    try {
                        col = destResource.getCollection();
                    } catch (IllegalArgumentException ex) {
                        return DavServletResponse.SC_BAD_GATEWAY;
                    }
                    col.removeMember(destResource);
                    status = DavServletResponse.SC_NO_CONTENT;
                }
            } else {
                // cannot copy/move to an existing item, if overwrite is not forced
                return DavServletResponse.SC_PRECONDITION_FAILED;
            }
        } else {
            // destination does not exist >> copy/move can be performed
            status = DavServletResponse.SC_CREATED;
        }
        return status;
    }

    /**
     * The LOCK method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doLock(WebdavRequest request, WebdavResponse response,
                          DavResource resource) throws IOException, DavException {

        LockInfo lockInfo = request.getLockInfo();
        if (lockInfo.isRefreshLock()) {
            // refresh any matching existing locks
            ActiveLock[] activeLocks = resource.getLocks();
            List<ActiveLock> lList = new ArrayList<ActiveLock>();
            for (ActiveLock activeLock : activeLocks) {
                // adjust lockinfo with type/scope retrieved from the lock.
                lockInfo.setType(activeLock.getType());
                lockInfo.setScope(activeLock.getScope());

                DavProperty<?> etagProp = resource.getProperty(DavPropertyName.GETETAG);
                String etag = etagProp != null ? String.valueOf(etagProp.getValue()) : "";
                if (request.matchesIfHeader(resource.getHref(), activeLock.getToken(), etag)) {
                    lList.add(resource.refreshLock(lockInfo, activeLock.getToken()));
                }
            }
            if (lList.isEmpty()) {
                throw new DavException(DavServletResponse.SC_PRECONDITION_FAILED);
            }
            ActiveLock[] refreshedLocks = lList.toArray(new ActiveLock[lList.size()]);
            response.sendRefreshLockResponse(refreshedLocks);
        } else {
            int status = HttpServletResponse.SC_OK;
            if (!resource.exists()) {
                // lock-empty requires status code 201 (Created)
                status = HttpServletResponse.SC_CREATED;
            }

            // create a new lock
            ActiveLock lock = resource.lock(lockInfo);

            CodedUrlHeader header = new CodedUrlHeader(
                DavConstants.HEADER_LOCK_TOKEN, lock.getToken());
            response.setHeader(header.getHeaderName(), header.getHeaderValue());

            DavPropertySet propSet = new DavPropertySet();
            propSet.add(new LockDiscovery(lock));
            response.sendXmlResponse(propSet, status);
        }
    }

    /**
     * The UNLOCK method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     */
    protected void doUnlock(WebdavRequest request, WebdavResponse response,
                            DavResource resource) throws DavException {
        // get lock token from header
        String lockToken = request.getLockToken();
        TransactionInfo tInfo = request.getTransactionInfo();
        if (tInfo != null) {
            ((TransactionResource) resource).unlock(lockToken, tInfo);
        } else {
            resource.unlock(lockToken);
        }
        response.setStatus(DavServletResponse.SC_NO_CONTENT);
    }

    /**
     * The ORDERPATCH method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doOrderPatch(WebdavRequest request,
                                WebdavResponse response,
                                DavResource resource)
        throws IOException, DavException {

        if (!(resource instanceof OrderingResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        OrderPatch op = request.getOrderPatch();
        if (op == null) {
            response.sendError(DavServletResponse.SC_BAD_REQUEST);
            return;
        }
        // perform reordering of internal members
        ((OrderingResource) resource).orderMembers(op);
        response.setStatus(DavServletResponse.SC_OK);
    }

    /**
     * The SUBSCRIBE method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doSubscribe(WebdavRequest request,
                               WebdavResponse response,
                               DavResource resource)
        throws IOException, DavException {

        if (!(resource instanceof ObservationResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        SubscriptionInfo info = request.getSubscriptionInfo();
        if (info == null) {
            response.sendError(DavServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        Subscription subs = ((ObservationResource) resource).subscribe(info, request.getSubscriptionId());
        response.sendSubscriptionResponse(subs);
    }

    /**
     * The UNSUBSCRIBE method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doUnsubscribe(WebdavRequest request,
                                 WebdavResponse response,
                                 DavResource resource)
        throws IOException, DavException {

        if (!(resource instanceof ObservationResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        ((ObservationResource) resource).unsubscribe(request.getSubscriptionId());
        response.setStatus(DavServletResponse.SC_NO_CONTENT);
    }

    /**
     * The POLL method
     *
     * @param request
     * @param response
     * @param resource
     * @throws IOException
     * @throws DavException
     */
    protected void doPoll(WebdavRequest request,
                          WebdavResponse response,
                          DavResource resource)
        throws IOException, DavException {

        if (!(resource instanceof ObservationResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        EventDiscovery ed = ((ObservationResource) resource).poll(
            request.getSubscriptionId(), request.getPollTimeout());
        response.sendPollResponse(ed);
    }

    /**
     * The VERSION-CONTROL method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doVersionControl(WebdavRequest request, WebdavResponse response,
                                    DavResource resource)
        throws DavException, IOException {
        if (!(resource instanceof VersionableResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        ((VersionableResource) resource).addVersionControl();
    }

    /**
     * The LABEL method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doLabel(WebdavRequest request, WebdavResponse response,
                           DavResource resource)
        throws DavException, IOException {

        LabelInfo labelInfo = request.getLabelInfo();
        if (resource instanceof VersionResource) {
            ((VersionResource) resource).label(labelInfo);
        } else if (resource instanceof VersionControlledResource) {
            ((VersionControlledResource) resource).label(labelInfo);
        } else {
            // any other resource type that does not support a LABEL request
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    /**
     * The REPORT method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doReport(WebdavRequest request, WebdavResponse response,
                            DavResource resource)
        throws DavException, IOException {
        ReportInfo info = request.getReportInfo();
        Report report;
        if (resource instanceof DeltaVResource) {
            report = ((DeltaVResource) resource).getReport(info);
        } else if (resource instanceof AclResource) {
            report = ((AclResource) resource).getReport(info);
        } else {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        int statusCode = (report.isMultiStatusReport()) ? DavServletResponse.SC_MULTI_STATUS : DavServletResponse.SC_OK;
        addHintAboutPotentialRequestEncodings(request, response);
        response.sendXmlResponse(report, statusCode, acceptsGzipEncoding(request) ? Collections.singletonList("gzip") : Collections.emptyList());
    }

    /**
     * The CHECKIN method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doCheckin(WebdavRequest request, WebdavResponse response,
                             DavResource resource)
        throws DavException, IOException {

        if (!(resource instanceof VersionControlledResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String versionHref = ((VersionControlledResource) resource).checkin();
        response.setHeader(DeltaVConstants.HEADER_LOCATION, versionHref);
        response.setStatus(DavServletResponse.SC_CREATED);
    }

    /**
     * The CHECKOUT method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doCheckout(WebdavRequest request, WebdavResponse response,
                              DavResource resource)
        throws DavException, IOException {
        if (!(resource instanceof VersionControlledResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        ((VersionControlledResource) resource).checkout();
    }

    /**
     * The UNCHECKOUT method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doUncheckout(WebdavRequest request, WebdavResponse response,
                                DavResource resource)
        throws DavException, IOException {
        if (!(resource instanceof VersionControlledResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        ((VersionControlledResource) resource).uncheckout();
    }

    /**
     * The MERGE method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doMerge(WebdavRequest request, WebdavResponse response,
                           DavResource resource) throws DavException, IOException {

        if (!(resource instanceof VersionControlledResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        MergeInfo info = request.getMergeInfo();
        MultiStatus ms = ((VersionControlledResource) resource).merge(info);
        response.sendMultiStatus(ms);
    }

    /**
     * The UPDATE method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doUpdate(WebdavRequest request, WebdavResponse response,
                            DavResource resource) throws DavException, IOException {

        if (!(resource instanceof VersionControlledResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        UpdateInfo info = request.getUpdateInfo();
        MultiStatus ms = ((VersionControlledResource) resource).update(info);
        response.sendMultiStatus(ms);
    }

    /**
     * The MKWORKSPACE method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doMkWorkspace(WebdavRequest request, WebdavResponse response,
                                 DavResource resource) throws DavException, IOException {
        if (resource.exists()) {
            log.warn("Cannot create a new workspace. Resource already exists.");
            response.sendError(DavServletResponse.SC_FORBIDDEN);
            return;
        }

        DavResource parentResource = resource.getCollection();
        if (parentResource == null || !parentResource.exists() || !parentResource.isCollection()) {
            // parent does not exist or is not a collection
            response.sendError(DavServletResponse.SC_CONFLICT);
            return;
        }
        if (!(parentResource instanceof DeltaVResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        ((DeltaVResource) parentResource).addWorkspace(resource);
        response.setStatus(DavServletResponse.SC_CREATED);
    }

    /**
     * The MKACTIVITY method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doMkActivity(WebdavRequest request, WebdavResponse response,
                                DavResource resource) throws DavException, IOException {
        if (resource.exists()) {
            log.warn("Unable to create activity: A resource already exists at the request-URL " + request.getRequestURL());
            response.sendError(DavServletResponse.SC_FORBIDDEN);
            return;
        }

        DavResource parentResource = resource.getCollection();
        if (parentResource == null || !parentResource.exists() || !parentResource.isCollection()) {
            // parent does not exist or is not a collection
            response.sendError(DavServletResponse.SC_CONFLICT);
            return;
        }
        // TODO: improve. see http://issues.apache.org/jira/browse/JCR-394
        if (!parentResource.getComplianceClass().contains(DavCompliance.ACTIVITY)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        if (!(resource instanceof ActivityResource)) {
            log.error("Unable to create activity: ActivityResource expected");
            response.sendError(DavServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // try to add the new activity resource
        parentResource.addMember(resource, getInputContext(request, request.getInputStream()));

        // Note: mandatory cache control header has already been set upon response creation.
        response.setStatus(DavServletResponse.SC_CREATED);
    }

    /**
     * The BASELINECONTROL method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doBaselineControl(WebdavRequest request, WebdavResponse response,
                                     DavResource resource)
        throws DavException, IOException {

        if (!resource.exists()) {
            log.warn("Unable to add baseline control. Resource does not exist " + resource.getHref());
            response.sendError(DavServletResponse.SC_NOT_FOUND);
            return;
        }
        // TODO: improve. see http://issues.apache.org/jira/browse/JCR-394
        if (!(resource instanceof VersionControlledResource) || !resource.isCollection()) {
            log.warn("BaselineControl is not supported by resource " + resource.getHref());
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        // TODO : missing method on VersionControlledResource
        throw new DavException(DavServletResponse.SC_NOT_IMPLEMENTED);
        /*
        ((VersionControlledResource) resource).addBaselineControl(request.getRequestDocument());
        // Note: mandatory cache control header has already been set upon response creation.
        response.setStatus(DavServletResponse.SC_OK);
        */
    }

    /**
     * The SEARCH method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doSearch(WebdavRequest request, WebdavResponse response,
                            DavResource resource) throws DavException, IOException {

        if (!(resource instanceof SearchResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        Document doc = request.getRequestDocument();
        if (doc != null) {
            SearchInfo sR = SearchInfo.createFromXml(doc.getDocumentElement());
            response.sendMultiStatus(((SearchResource) resource).search(sR));
        } else {
            // request without request body is valid if requested resource
            // is a 'query' resource.
            response.sendMultiStatus(((SearchResource) resource).search(null));
        }
    }

    /**
     * The ACL method
     *
     * @param request
     * @param response
     * @param resource
     * @throws DavException
     * @throws IOException
     */
    protected void doAcl(WebdavRequest request, WebdavResponse response,
                         DavResource resource) throws DavException, IOException {
        if (!(resource instanceof AclResource)) {
            response.sendError(DavServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        Document doc = request.getRequestDocument();
        if (doc == null) {
            throw new DavException(DavServletResponse.SC_BAD_REQUEST, "ACL request requires a DAV:acl body.");
        }
        AclProperty acl = AclProperty.createFromXml(doc.getDocumentElement());
        ((AclResource) resource).alterAcl(acl);
    }

    /**
     * Return a new <code>InputContext</code> used for adding resource members
     *
     * @param request
     * @param in
     * @return
     * @see #spoolResource(WebdavRequest, WebdavResponse, DavResource, boolean)
     */
    protected InputContext getInputContext(DavServletRequest request, InputStream in) {
        return new InputContextImpl(request, in);
    }

    /**
     * Return a new <code>OutputContext</code> used for spooling resource properties and
     * the resource content
     *
     * @param response
     * @param out
     * @return
     * @see #doPut(WebdavRequest, WebdavResponse, DavResource)
     * @see #doMkCol(WebdavRequest, WebdavResponse, DavResource)
     */
    protected OutputContext getOutputContext(DavServletResponse response, OutputStream out) {
        return new MyOutputContextImpl(response, out);
    }

    /**
     * Obtain the (ordered!) list of content codings that have been used in the
     * request
     */
    public static List<String> getContentCodings(HttpServletRequest request) {
        return getListElementsFromHeaderField(request, "Content-Encoding");
    }

    /**
     * Check whether recipient accepts GZIP content coding
     */
    private static boolean acceptsGzipEncoding(HttpServletRequest request) {
        List<String> result = getListElementsFromHeaderField(request, "Accept-Encoding");
        for (String s : result) {
            s = s.replace(" ", "");
            int semi = s.indexOf(';');
            if ("gzip".equals(s)) {
                return true;
            } else if (semi > 0) {
                String enc = s.substring(0, semi);
                String parm = s.substring(semi + 1);
                if ("gzip".equals(enc) && parm.startsWith("q=")) {
                    float q = Float.valueOf(parm.substring(2));
                    return q > 0;
                }
            }
        }
        return false;
    }

    private static List<String> getListElementsFromHeaderField(HttpServletRequest request, String fieldName) {
        List<String> result = Collections.emptyList();
        for (Enumeration<String> ceh = request.getHeaders(fieldName); ceh.hasMoreElements(); ) {
            for (String h : ceh.nextElement().split(",")) {
                if (!h.trim().isEmpty()) {
                    if (result.isEmpty()) {
                        result = new ArrayList<String>();
                    }
                    result.add(h.trim().toLowerCase(Locale.ENGLISH));
                }
            }
        }

        return result;
    }

    /**
     * Get field value of a singleton field
     *
     * @param request   HTTP request
     * @param fieldName field name
     * @return the field value (when there is indeed a single field line) or {@code null} when field not present
     * @throws IllegalArgumentException when multiple field lines present
     */
    protected static String getSingletonField(HttpServletRequest request, String fieldName) {
        Enumeration<String> lines = request.getHeaders(fieldName);
        if (!lines.hasMoreElements()) {
            return null;
        } else {
            String value = lines.nextElement();
            if (!lines.hasMoreElements()) {
                return value;
            } else {
                List<String> v = new ArrayList<>();
                v.add(value);
                while (lines.hasMoreElements()) {
                    v.add(lines.nextElement());
                }
                throw new IllegalArgumentException("Multiple field lines for '" + fieldName + "' header field: " + v);
            }
        }
    }
}
