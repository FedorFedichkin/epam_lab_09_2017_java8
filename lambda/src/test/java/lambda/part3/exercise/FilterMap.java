package lambda.part3.exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class FilterMap {

    public static class Container<T, R> {
        private final Predicate<T> predicate;
        private final Function<T, R> function;

        public Container(Predicate<T> predicate) {
            this.predicate = predicate;
            this.function = null;
        }

        public Container(Function<T, R> function) {
            this.function = function;
            this.predicate = null;
        }

        public Predicate<T> getPredicate() {
            return predicate;
        }

        public Function<T, R> getFunction() {
            return function;
        }
    }

    public static class LazyCollectionHelper<T> {

        private final List<Container<Object, Object>> actions;

        private final List<T> list;

        public LazyCollectionHelper(List<T> list, List<Container<Object, Object>> actions) {
            this.actions = actions;
            this.list = list;
        }

        public LazyCollectionHelper(List<T> list) {
            this(list, new ArrayList<>());
        }

        public LazyCollectionHelper<T> filter(Predicate<T> condition) {
            List<Container<Object, Object>> actions = new ArrayList<>(this.actions);
            actions.add(new Container<>((Predicate<Object>) condition));
            return new LazyCollectionHelper<>(list, actions);
        }

        public <R> LazyCollectionHelper<R> map(Function<T, R> function) {
            List<Container<Object, Object>> newActions = new ArrayList<>(actions);
            newActions.add(new Container<>((Predicate<Object>) function));
            return new LazyCollectionHelper<R>((List<R>) list, newActions);
        }

        public List<T> force() {
            if (actions.isEmpty()){
                return new ArrayList<>(list);
            }
            List<T> result = new ArrayList<>();

            nextValue : for (Object value: list) {
                for (Container<Object, Object> action : actions) {
                    Predicate<Object> predicate = action.getPredicate();
                    if (predicate != null){
                        if (!predicate.test(value)){
                            continue nextValue;
                        }
                    }
                    else {
                        Function<Object, Object> function = action.getFunction();
                        value = function.apply(value);
                    }
                }
                result.add((T)value);
            }
           return result;
        }
    }
}
