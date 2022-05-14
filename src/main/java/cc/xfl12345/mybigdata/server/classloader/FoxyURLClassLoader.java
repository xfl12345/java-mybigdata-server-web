package cc.xfl12345.mybigdata.server.classloader;

import cc.xfl12345.mybigdata.server.utility.UnreentrantLock;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.provider.ram.RamFileObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.SecureClassLoader;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FoxyURLClassLoader extends SecureClassLoader {

    @Getter
    protected String byteCodeFileFolderName;

    protected ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected volatile FileSystem fileSystem;


    public FileSystem getFileSystem() {
        readWriteLock.readLock().lock();
        FileSystem result = this.fileSystem;
        readWriteLock.readLock().unlock();
        return result;
    }

    public void setFileSystem(FileSystem fileSystem) {
        readWriteLock.writeLock().lock();
        this.fileSystem = fileSystem;
        readWriteLock.writeLock().unlock();
    }

    protected URLStreamHandlerFactory factory;

    public FoxyURLClassLoader() {
        this(null, Thread.currentThread().getContextClassLoader());
    }

    public FoxyURLClassLoader(FileSystem fileSystem) {
        this(fileSystem, Thread.currentThread().getContextClassLoader());
    }

    public FoxyURLClassLoader(FileSystem fileSystem, ClassLoader parent) {
        this(fileSystem, null, parent);
    }

    public FoxyURLClassLoader(FileSystem fileSystem, String name, ClassLoader parent) {
        super(name, parent);
        setFileSystem(fileSystem);
        setByteCodeFileFolderName(null);
    }

    protected void setByteCodeFileFolderName(String name) {
        this.byteCodeFileFolderName = (name == null || "".equals(name)) ?
            FoxyURLClassLoader.class.getSimpleName() : name;
    }

    public boolean removeClass() {


        return false;
    }

    public Class<?> addClass(String name, byte[] b, int off, int len) throws IOException {
        Class<?> cls = defineClass(name, b, off, len);
        RamFileObject ramFileObject = (RamFileObject) fileSystem.resolveFile(
            "ram:/jvm/bytecode/" + byteCodeFileFolderName + '/'
            + cls.getCanonicalName() + ".class"
        );
        OutputStream outputStream = ramFileObject.getOutputStream();
        outputStream.write(b, off, len);
        outputStream.close();
//        addURL(ramFileObject.getURL());
        return cls;
    }
}
