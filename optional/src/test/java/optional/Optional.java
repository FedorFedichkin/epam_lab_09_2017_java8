package optional;

import java.util.Objects;

public class Optional<T> {

    private  static  final Optional<?> EMPTY = new Optional<>(null);
    private final T value;

    private Optional(){
        value = null;
    }
    private  Optional(T value){
       this.value = Objects.requireNonNull(value);
    }

    public static <T> Optional<T> of(T value ){
        return new Optional<>(value);
    }
    public static <T> Optional<T> empty(){
        return (Optional<T>)EMPTY;
    }
    // empty
    // of
    // ofNullable
    // isPresent
    // ifPresent
    // get
    // orElse
    // orElseGet
    // orElseThrow
    // filter
    // map
    // flatMap
}









