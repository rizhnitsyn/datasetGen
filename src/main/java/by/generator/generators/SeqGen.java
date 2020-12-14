package by.generator.generators;

import au.com.anthonybruno.generator.Generator;
import java.util.concurrent.atomic.AtomicLong;

public class SeqGen implements Generator<Long> {

    private AtomicLong value = new AtomicLong(1);

    public SeqGen(Integer startFrom) {
        value = new AtomicLong(startFrom);
    }

    @Override
    public Long generate() {
        return value.getAndIncrement();
    }
}
