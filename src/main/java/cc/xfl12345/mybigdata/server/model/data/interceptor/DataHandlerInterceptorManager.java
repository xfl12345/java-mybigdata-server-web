package cc.xfl12345.mybigdata.server.model.data.interceptor;

import cc.xfl12345.mybigdata.server.model.database.handler.FunctionWithException;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DataHandlerInterceptorManager {
    protected CopyOnWriteArrayList<DataHandlerInterceptor> interceptors = new CopyOnWriteArrayList<>();

    @Getter
    @Setter
    protected Class<?> paramType = Object.class;

    @Getter
    @Setter
    protected Class<?> returnType = Object.class;

    @Getter
    @Setter
    protected FunctionWithException<Object, Object> defaultAction = (value) -> null;

    public Object execute(Object param) throws Exception {
        boolean keepGoing = true;
        Object actionOutputData = null;
        int lastIndex = 0;
        Object castParam = paramType.cast(param);

        for (DataHandlerInterceptor interceptor : interceptors) {
            keepGoing = interceptor.beforeAction(castParam);
            if (!keepGoing) {
                if (interceptor.isExecuteBranch()) {
                    actionOutputData = interceptor.branchAction(castParam);
                    for (int i = 0; i <= lastIndex; i++) {
                        interceptors.get(i).afterAction(castParam, actionOutputData);
                    }
                }

                break;
            }

            lastIndex += 1;
        }

        if (keepGoing) {
            actionOutputData = defaultAction.apply(castParam);
            for (DataHandlerInterceptor interceptor : interceptors) {
                interceptor.afterAction(castParam, actionOutputData);
            }
        }

        return returnType.cast(actionOutputData);
    }


    public int size() {
        return interceptors.size();
    }

    public boolean isEmpty() {
        return interceptors.isEmpty();
    }

    public boolean contains(Object o) {
        return interceptors.contains(o);
    }

    public int indexOf(Object o) {
        return interceptors.indexOf(o);
    }

    public int indexOf(DataHandlerInterceptor interceptor, int index) {
        return interceptors.indexOf(interceptor, index);
    }

    public int lastIndexOf(Object o) {
        return interceptors.lastIndexOf(o);
    }

    public int lastIndexOf(DataHandlerInterceptor interceptor, int index) {
        return interceptors.lastIndexOf(interceptor, index);
    }

    public Object[] toArray() {
        return interceptors.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return interceptors.toArray(a);
    }

    public DataHandlerInterceptor get(int index) {
        return interceptors.get(index);
    }

    public DataHandlerInterceptor set(int index, DataHandlerInterceptor element) {
        return interceptors.set(index, element);
    }

    public boolean add(DataHandlerInterceptor interceptor) {
        return interceptors.add(interceptor);
    }

    public void add(int index, DataHandlerInterceptor element) {
        interceptors.add(index, element);
    }

    public DataHandlerInterceptor remove(int index) {
        return interceptors.remove(index);
    }

    public void clear() {
        interceptors.clear();
    }

    public boolean addAll(int index, Collection<? extends DataHandlerInterceptor> c) {
        return interceptors.addAll(index, c);
    }

    public boolean removeIf(Predicate<? super DataHandlerInterceptor> filter) {
        return interceptors.removeIf(filter);
    }

    public void replaceAll(UnaryOperator<DataHandlerInterceptor> operator) {
        interceptors.replaceAll(operator);
    }

    public void sort(Comparator<? super DataHandlerInterceptor> c) {
        interceptors.sort(c);
    }

    public ListIterator<DataHandlerInterceptor> listIterator() {
        return interceptors.listIterator();
    }

    public ListIterator<DataHandlerInterceptor> listIterator(int index) {
        return interceptors.listIterator(index);
    }

    public List<DataHandlerInterceptor> subList(int fromIndex, int toIndex) {
        return interceptors.subList(fromIndex, toIndex);
    }

    public Stream<DataHandlerInterceptor> stream() {
        return interceptors.stream();
    }

    public Stream<DataHandlerInterceptor> parallelStream() {
        return interceptors.parallelStream();
    }
}
