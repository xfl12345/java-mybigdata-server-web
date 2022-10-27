package cc.xfl12345.mybigdata.server.web.utility;

public class SynchronizeLock {
    protected volatile Boolean locked = Boolean.FALSE;

    protected final Object waitLock = new Object();

    /**
     * 任何线程，只要调用该函数，要么解封其它函数实现同步，要么先自我封印，然后等待其它函数解封自己进而实现同步。无视早晚。
     */
    public void justSynchronize() throws InterruptedException {
        // 先排他，防止同时进入 if-else 分支语句里的同一个分支
        synchronized (waitLock) {
            if (locked) {
                locked = Boolean.FALSE;
                waitLock.notify();
            } else {
                locked = Boolean.TRUE;
                waitLock.wait();
            }
        }
    }
}
