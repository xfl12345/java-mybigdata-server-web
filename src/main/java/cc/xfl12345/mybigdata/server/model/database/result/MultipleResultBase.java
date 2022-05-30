package cc.xfl12345.mybigdata.server.model.database.result;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MultipleResultBase<DataType> extends ExecuteResultBase {
    @Getter
    private final Type dataType = ((ParameterizedType) (getClass().getGenericSuperclass())).getActualTypeArguments()[0];

    @Getter
    protected final LinkedHashSet<PackedData<DataType>> datas = new LinkedHashSet<>();

    public Spliterator<PackedData<DataType>> spliterator() {
        return datas.spliterator();
    }

    public Iterator<PackedData<DataType>> iterator() {
        return datas.iterator();
    }

    public int size() {
        return datas.size();
    }

    public boolean isEmpty() {
        return datas.isEmpty();
    }

    public boolean contains(Object o) {
        return datas.contains(o);
    }

    public boolean add(PackedData<DataType> tPackedData) {
        return datas.add(tPackedData);
    }

    public boolean remove(Object o) {
        return datas.remove(o);
    }

    public void clear() {
        datas.clear();
    }

    public Object[] toArray() {
        return datas.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return datas.toArray(a);
    }

    public boolean removeAll(Collection<?> c) {
        return datas.removeAll(c);
    }

    public boolean containsAll(Collection<?> c) {
        return datas.containsAll(c);
    }

    public boolean addAll(Collection<? extends PackedData<DataType>> c) {
        return datas.addAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return datas.retainAll(c);
    }

    public <T> T[] toArray(IntFunction<T[]> generator) {
        return datas.toArray(generator);
    }

    public boolean removeIf(Predicate<? super PackedData<DataType>> filter) {
        return datas.removeIf(filter);
    }

    public Stream<PackedData<DataType>> stream() {
        return datas.stream();
    }

    public Stream<PackedData<DataType>> parallelStream() {
        return datas.parallelStream();
    }

    public void forEach(Consumer<? super PackedData<DataType>> action) {
        datas.forEach(action);
    }
}
