package optional;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.zip.CheckedInputStream;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"Convert2MethodRef", "ExcessiveLambdaUsage", "ResultOfMethodCallIgnored", "OptionalIsPresent"})
public class OptionalExample {

    @Test
    public void get() {
        Optional<String> o1 = Optional.empty();

        o1.ifPresent(s -> System.out.println(s));

        o1.orElse("t");
        o1.orElseGet(() -> "t");
        o1.orElseThrow(() -> new UnsupportedOperationException());
    }

    @Test
    public void ifPresent() {
        Optional<String> o1 = getOptional();

        o1.ifPresent(System.out::println);

        if (o1.isPresent()) {
            System.out.println(o1.get());
        }
    }

    @Test
    public void map() {
        Optional<String> o1 = getOptional();

        Function<String, Integer> getLength = String::length;

        Optional<Integer> expected = o1.map(getLength);

        Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = Optional.ofNullable(getLength.apply(o1.get()));
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        Optional<String> optional = getOptional();
        Function<String, Optional<List<Character>>> function = string -> {
            char[] charsFromString = string.toCharArray();
            Optional<List<Character>> listOfCharacters = Optional.of(new ArrayList<>());
            if (listOfCharacters.isPresent()) {
                for (char character: charsFromString) {
                    listOfCharacters.get().add(character);
                }
            }
            return listOfCharacters;
        };
        Optional<List<Character>> expected = optional.flatMap(function);
        Optional<List<Character>> actual = optional.isPresent() ? function.apply(optional.get()) : Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        Optional<String> optional = getOptional();
        Predicate<String> predicate = string -> string.equals("abc");
        Optional<String> expected = optional.filter(predicate);
        Optional<String> actual;
        if (optional.isPresent()) {
            actual = predicate.test(optional.get()) ? optional : Optional.empty();
        } else {
            actual = Optional.empty();
        }
        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean() ? Optional.empty() : Optional.of("abc");
    }
}
