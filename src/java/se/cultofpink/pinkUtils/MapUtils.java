package se.cultofpink.pinkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapUtils {

    /**
     * Utility to turn a list of objects into a map of those objects
     * Each object is keyed on the key determined by the supplied keyProvider function
     *
     * @param values      the objects to turn into a map
     * @param keyProvider functional interface to determin the key to be used for each object
     * @param <KEY>       Key type
     * @param <VALUE>     Value type
     * @return map representation of the objects in the list
     */
    public static <KEY, VALUE> Map<KEY, VALUE> asMap(final List<VALUE> values, final Function<VALUE, KEY> keyProvider) {
        final HashMap<KEY, VALUE> result = new HashMap<>();
        for (VALUE value : values) {
            result.put(keyProvider.apply(value), value);
        }
        return result;
    }
}