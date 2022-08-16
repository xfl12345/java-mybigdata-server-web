package cc.xfl12345.mybigdata.server.model.database.handler;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DataHandlerInterceptorManager<ParamType, ReturnType> {
    protected CopyOnWriteArrayList<DataHandlerInterceptor<ParamType, ReturnType>> interceptors = new CopyOnWriteArrayList<>();

    @Getter
    @Setter
    protected FunctionWithException<ParamType, ReturnType> defaultAction = (value) -> null;

    public Object execute(ParamType params) throws Exception {
        boolean keepGoing = true;
        ReturnType actionOutputData = null;
        int lastIndex = 0;

        for (DataHandlerInterceptor<ParamType, ReturnType> interceptor : interceptors) {
            keepGoing = interceptor.beforeAction(params);
            if (!keepGoing) {
                if (interceptor.isExecuteBranch()) {
                    actionOutputData = interceptor.branchAction(params);
                    for (int i = 0; i <= lastIndex; i++) {
                        interceptors.get(i).afterAction(params, actionOutputData);
                    }
                }

                break;
            }

            lastIndex += 1;
        }

        if (keepGoing) {
            actionOutputData = defaultAction.apply(params);
            for (DataHandlerInterceptor<ParamType, ReturnType> interceptor : interceptors) {
                interceptor.afterAction(params, actionOutputData);
            }
        }

        return actionOutputData;
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

    public int indexOf(DataHandlerInterceptor<ParamType, ReturnType> paramTypeReturnTypeDataHandlerInterceptor, int index) {
        return interceptors.indexOf(paramTypeReturnTypeDataHandlerInterceptor, index);
    }

    public int lastIndexOf(Object o) {
        return interceptors.lastIndexOf(o);
    }

    public int lastIndexOf(DataHandlerInterceptor<ParamType, ReturnType> paramTypeReturnTypeDataHandlerInterceptor, int index) {
        return interceptors.lastIndexOf(paramTypeReturnTypeDataHandlerInterceptor, index);
    }

    public Object[] toArray() {
        return interceptors.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return interceptors.toArray(a);
    }

    public DataHandlerInterceptor<ParamType, ReturnType> get(int index) {
        return interceptors.get(index);
    }

    public DataHandlerInterceptor<ParamType, ReturnType> set(int index, DataHandlerInterceptor<ParamType, ReturnType> element) {
        return interceptors.set(index, element);
    }

    public boolean add(DataHandlerInterceptor<ParamType, ReturnType> paramTypeReturnTypeDataHandlerInterceptor) {
        return interceptors.add(paramTypeReturnTypeDataHandlerInterceptor);
    }

    public void add(int index, DataHandlerInterceptor<ParamType, ReturnType> element) {
        interceptors.add(index, element);
    }

    public DataHandlerInterceptor<ParamType, ReturnType> remove(int index) {
        return interceptors.remove(index);
    }

    public boolean remove(Object o) {
        return interceptors.remove(o);
    }

    public boolean addIfAbsent(DataHandlerInterceptor<ParamType, ReturnType> paramTypeReturnTypeDataHandlerInterceptor) {
        return interceptors.addIfAbsent(paramTypeReturnTypeDataHandlerInterceptor);
    }

    public boolean containsAll(Collection<?> c) {
        return interceptors.containsAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return interceptors.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return interceptors.retainAll(c);
    }

    public int addAllAbsent(Collection<? extends DataHandlerInterceptor<ParamType, ReturnType>> c) {
        return interceptors.addAllAbsent(c);
    }

    public void clear() {
        interceptors.clear();
    }

    public boolean addAll(Collection<? extends DataHandlerInterceptor<ParamType, ReturnType>> c) {
        return interceptors.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends DataHandlerInterceptor<ParamType, ReturnType>> c) {
        return interceptors.addAll(index, c);
    }

    public boolean removeIf(Predicate<? super DataHandlerInterceptor<ParamType, ReturnType>> filter) {
        return interceptors.removeIf(filter);
    }

    public void replaceAll(UnaryOperator<DataHandlerInterceptor<ParamType, ReturnType>> operator) {
        interceptors.replaceAll(operator);
    }

    public void sort(Comparator<? super DataHandlerInterceptor<ParamType, ReturnType>> c) {
        interceptors.sort(c);
    }

    public List<DataHandlerInterceptor<ParamType, ReturnType>> subList(int fromIndex, int toIndex) {
        return interceptors.subList(fromIndex, toIndex);
    }

    public <T> T[] toArray(IntFunction<T[]> generator) {
        return interceptors.toArray(generator);
    }

    public Stream<DataHandlerInterceptor<ParamType, ReturnType>> stream() {
        return interceptors.stream();
    }

    public Stream<DataHandlerInterceptor<ParamType, ReturnType>> parallelStream() {
        return interceptors.parallelStream();
    }
}
