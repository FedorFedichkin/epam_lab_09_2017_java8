package part2.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {

    private final OfDouble inner;
    private int currentIndex;

    public ZipWithIndexDoubleSpliterator(OfDouble inner) {
        this(0, inner);
    }

    private ZipWithIndexDoubleSpliterator(int firstIndex, OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        currentIndex = firstIndex;
        this.inner = inner;
    }

    // TODO // done
    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    // TODO // done
    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance((DoubleConsumer) doubleConsumer -> {
            IndexedDoublePair pair = new IndexedDoublePair(currentIndex, doubleConsumer);
            currentIndex = currentIndex++;
            action.accept(pair);
        });
    }

    // TODO // done
    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((DoubleConsumer) doubleConsumer -> {
            IndexedDoublePair pair = new IndexedDoublePair(currentIndex, doubleConsumer);
            currentIndex = currentIndex++;
            action.accept(pair);
        });
    }

    // TODO // done
    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            OfDouble splitResult = inner.trySplit();
            if (splitResult == null) {
                return null;
            }
            ZipWithIndexDoubleSpliterator spliterator = new ZipWithIndexDoubleSpliterator(currentIndex, splitResult);
            currentIndex += splitResult.estimateSize();
            return spliterator;
        } else {
            return super.trySplit();
        }
    }

    // TODO // done
    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
