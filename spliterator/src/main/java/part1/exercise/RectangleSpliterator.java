package part1.exercise;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {
    private final int[][] array;
    private long startInclusive;
    private long endExclusive;


    // TODO заменить // done
    public RectangleSpliterator(int[][] array) {
        this(0L, checkArrayAndCalcEstimatedSize(array), array);
    }

    public RectangleSpliterator(long startInclusive, long endExclusive, int[][] array) {
        super(endExclusive - startInclusive,
                Spliterator.IMMUTABLE |
                        Spliterator.ORDERED |
                        Spliterator.SIZED |
                        Spliterator.SUBSIZED |
                        Spliterator.NONNULL);
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
        this.array = array;
    }

    // TODO // done
    private static long checkArrayAndCalcEstimatedSize(int[][] array) {
        if (Arrays.stream(array)
                .anyMatch(arr -> arr.length != array[0].length)) {
            throw new IllegalStateException("Array is not rectangular");
        }
        return array.length * array[0].length;
    }

    // TODO // done
    @Override
    public OfInt trySplit() {
        long length = endExclusive - startInclusive;
        if (length < 2) {
            return null;
        }
        long middle = length / 2 + startInclusive;
        RectangleSpliterator splitResult = new RectangleSpliterator(startInclusive, middle, array);
        startInclusive = middle;
        return splitResult;
    }

    // TODO // done
    @Override
    public long estimateSize() {
        return endExclusive - startInclusive;
    }

    // TODO  // done
    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive < endExclusive) {
            action.accept(array[(int) startInclusive / array[0].length][(int) startInclusive % array[0].length]);
            startInclusive++;
            return true;
        }
        return false;
    }
}


class A {

    protected String val;

    A() {
        setVal();
    }

    public void setVal() {
        val = "A";
    }
}

class B extends A {

    public static void main(String[] args) {
        System.out.println(new B().val);

    }

    @Override
    public void setVal() {
        val = "B";
    }
}