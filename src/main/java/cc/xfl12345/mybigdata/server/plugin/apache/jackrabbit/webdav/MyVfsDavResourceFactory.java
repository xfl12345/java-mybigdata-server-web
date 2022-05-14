package cc.xfl12345.mybigdata.server.plugin.apache.jackrabbit.webdav;

import com.github.alanger.webdav.VfsDavResource;
import com.github.alanger.webdav.VfsDavResourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.jackrabbit.webdav.*;
import org.apache.jackrabbit.webdav.lock.LockManager;

/**
 * See org.apache.jackrabbit.webdav.simple.ResourceFactoryImpl
 */
@Slf4j
public class MyVfsDavResourceFactory extends VfsDavResourceFactory {

    public MyVfsDavResourceFactory(LockManager lockMgr, FileObject root) {
        super(lockMgr, root);
    }

    @Override
    protected DavResource createResource(DavResourceLocator locator, DavSession session, DavServletRequest request, FileObject fobj) throws DavException, FileSystemException {
        if (!fobj.exists()) {
            boolean isCollection = request != null && DavMethods.isCreateCollectionRequest(request);
            return new MyVfsDavResource(locator, this, session, isCollection);
        } else {
            return new MyVfsDavResource(locator, this, session, fobj, request);
        }
    }
}
