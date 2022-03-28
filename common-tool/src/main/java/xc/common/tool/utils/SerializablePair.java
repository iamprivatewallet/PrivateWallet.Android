package xc.common.tool.utils;

import android.util.Pair;

import java.io.Serializable;
import java.util.Objects;

public class SerializablePair<F, S> implements Serializable {

    public final F first;
    public final S second;

    /**
     * Constructor for a SerializablePair.
     *
     * @param first  the first object in the SerializablePair
     * @param second the second object in the pair
     */
    public SerializablePair(F first, S second) {
        this.first = first;
        this.second = second;
    }


}
