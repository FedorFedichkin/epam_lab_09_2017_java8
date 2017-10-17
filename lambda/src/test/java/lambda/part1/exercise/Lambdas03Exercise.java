package lambda.part1.exercise;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Lambdas03Exercise {

    private static String stringProd(String s, int i) {
        String[] array = new String[i];
        Arrays.fill(array, s);
        return Stream.of(array).collect(Collectors.joining());
    }

    @Test
    public void generic0() {
        final GenericProduct<Integer> prod = new GenericProduct<Integer>() {
            @Override
            public Integer prod(Integer a, int i) {
                return (a * i);
            }
        };
        assertEquals(Integer.valueOf(4), prod.prod(2, 2));
    }

    @Test
    public void generic1() {
        // final GenericProduct<Integer> prod = null; // Use statement lambda
        final GenericProduct<Integer> prod = (Integer i1, int i2) -> {
            return i1 * i2;
        };
        assertEquals(Integer.valueOf(4), prod.prod(2, 2));
    }

    @Test
    public void generic2() {
        //final GenericProduct<Integer> prod = null; // Use expression lambda
        final GenericProduct<Integer> prod = (Integer i1, int i2) -> i1 * i2;
        assertEquals(Integer.valueOf(4), prod.prod(2, 2));
    }

    @Test
    public void strSum() {
        // final GenericProduct<String> prod = null; // use stringProd;
        final GenericProduct<String> prod = Lambdas03Exercise::stringProd;
        assertEquals("aa", prod.prod("a", 2));
    }

    private String stringSumWithDelimeter(String s, int i) {
        String[] array = new String[i];
        Arrays.fill(array, s);
        return Stream.of(array).collect(Collectors.joining("-"));
    }

    @Test
    public void strSum2() {
        //final GenericProduct<String> prod = null; // use stringSumWithDelimeter;
        final GenericProduct<String> prod = this::stringSumWithDelimeter;
        assertEquals("a-a-a", prod.prod("a", 3));
    }

    private interface GenericProduct<T> {
        T prod(T a, int t);

        default T twice(T t) {
            return prod(t, 2);
        }
    }

}



