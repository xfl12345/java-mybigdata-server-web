package cc.xfl12345.mybigdata.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;

@Component
@WebListener
@Slf4j
public class MySessionEventListener implements HttpSessionListener {
    /**
     * 会话缓存Map，支持直接通过session ID来获取session对象
     */
    private static final ConcurrentHashMap<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    // 当用户与服务器之间开始session时触发该方法
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        sessionMap.put(session.getId(), session);
    }

    // 当用户与服务器之间session断开时触发该方法
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
//        ServletContext application = session.getServletContext();
        //Do something else
        sessionMap.remove(session.getId());
        session.invalidate();
    }

    public HttpSession getSessionById(String sessionId) {
        return sessionMap.get(sessionId);
    }
}
