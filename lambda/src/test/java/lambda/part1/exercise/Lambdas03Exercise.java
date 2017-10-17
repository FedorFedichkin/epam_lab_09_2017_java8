package lambda.part1.exercise;

import org.junit.Test;

import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;

public class Lambdas03Exercise {

    private interface GenericProduct<T> {
        T prod(T a, int i);

        default T twice(T t) {
            return prod(t, 2);
        }
    }

    @Test
    public void generic0() {
        // Use anonymous class
        final GenericProduct<Integer> prod = new GenericProduct<Integer>() {
            @Override
            public Integer prod(Integer a, int i) {
                return a * i;
            }
        };
        assertEquals(Integer.valueOf(6), prod.prod(3, 2));
    }

    @Test
    public void generic1() {
        // Use statement lambda
        final GenericProduct<Integer> prod = (Integer a, int i) -> {
            System.out.println("before multiplication");
            return a * i;
        };
        assertEquals(Integer.valueOf(6), prod.prod(3, 2));

    }

    @Test
    public void generic2() {
        // Use expression lambda
        final GenericProduct<Integer> prod = (Integer a, int i) -> a * i;
        assertEquals(Integer.valueOf(6), prod.prod(3, 2));
    }

    private static String stringProd(String s, int i) {
        final StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            sb.append(s);
        }
        return sb.toString();
    }

    @Test
    public void strSum() {
        // use stringProd (method-reference);
        final GenericProduct<String> prod = Lambdas03Exercise::stringProd;
        assertEquals("aa", prod.prod("a", 2));
    }

    private final String delimeter = "-";

    private String stringSumWithDelimeter(String s, int i) {
        final StringJoiner sj = new StringJoiner(delimeter);
        for (int j = 0; j < i; j++) {
            sj.add(s);
        }
        return sj.toString();
    }

    @Test
    public void strSum2() {
        // use stringSumWithDelimeter;
        final GenericProduct<String> prod = this::stringSumWithDelimeter;

        assertEquals("a-a-a", prod.prod("a", 3));
    }


}
