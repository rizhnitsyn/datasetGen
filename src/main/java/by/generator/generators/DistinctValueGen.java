package by.generator.generators;

import au.com.anthonybruno.generator.Generator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
public class DistinctValueGen<T> implements Generator<T> {

    private Generator<T> generator;
    private ArrayList<T> distVals = new ArrayList<>();
    private Integer distValCnt;

    @Override
    public T generate() {
        T value;
        if (distVals.size() < distValCnt) {
            value = generator.generate();
            distVals.add(value);
        } else {
            value = distVals.get((new Random()).nextInt(distValCnt));
        }
        return value;
    }
}
