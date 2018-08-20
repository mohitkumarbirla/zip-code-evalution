package com.mk.util;

import com.mk.model.StringZipRange;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 /**
 * Generic utility methods for Zip Code
 */
public class Utils {
     /*
     * Private constructor.
     */
    private Utils() {
        // prevent instantiation
    }
     private static Pattern zipCodePattern = Pattern.compile("(\\d{5})");
     /**
     * Consolidates a list of StringZipRange objects into the shortest possible grouping of ranges.
     * @param ranges The list of StringZipRange objects to be processed
     * @return A List of sorted (ascending) StringZipRange objects
     */
    public static List<StringZipRange> consolidate(List<StringZipRange> ranges) {
        Set<StringZipRange> sortedRanges = new TreeSet<>(StringZipRange.COMPARATOR);
        if (ranges != null) {
            ranges.sort(StringZipRange.COMPARATOR);
            for (StringZipRange zcr : ranges) {
                // create a copy, so the original object is not change by a future merge
                StringZipRange merge = StringZipRange.copy(zcr);
                boolean didOverlap = false;
                for (StringZipRange existingRange : sortedRanges) {
                    if (existingRange.isMergeable(merge)) {
                        existingRange.merge(merge);
                        didOverlap = true;
                        break;
                    }
                }
                if (!didOverlap) {
                    sortedRanges.add(merge);
                }
            }
        }
        return new ArrayList<>(sortedRanges);
    }
     /**
     * Checks if the specified ZIP code should be excluded (contained) by any of the known ZIP code ranges.
     * @param zipCode The ZIP code to check
     * @param excludeRange The List of StringZipRange object to use for exclusion
     * @return true if the ZIP code is contained by the exclusion range; otherwise false
     */
    public static boolean isExcluded(String zipCode, List<StringZipRange> excludeRange) {
        boolean result = false;
        if (zipCode != null) {
            Matcher matcher = zipCodePattern.matcher(zipCode);
            if (matcher.matches()) {
                result = isExcluded(Integer.valueOf(matcher.group(1)), excludeRange);
            }
            else {
                throw new IllegalArgumentException("Invalid ZIP code: " + zipCode);
            }
        }
        return result;
    }
     /**
     * Checks if the specified ZIP code should be excluded (contained) by any of the known ZIP code ranges.
     * @param zipCode The ZIP code to check
     * @param excludeRange The List of StringZipRange object to use for exclusion
     * @return true if the specified ZIP code is contained by the exclusion range; otherwise false
     */
    public static boolean isExcluded(int zipCode, List<StringZipRange> excludeRange) {
        boolean result = false;
        if (zipCode < 0 || zipCode > 99999) {
            throw new IllegalArgumentException("Invalid ZIP code: " + zipCode);
        }
        if (excludeRange != null) {
            for (StringZipRange range : excludeRange) {
                if (zipCode >= range.getStart() && zipCode <= range.getEnd()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
     /**
     * Checks of the specified ZIP code is in the specific range. Both start and end ranges values are considered
     * to be inclusive, so:
     * <pre>
     * <code>10 in [1,10]  = true</code>
     * <code>10 in [10,20] = true</code>
     * <code>10 in [1,20]  = true</code>
     * <code>10 in [11,20] = false</code>
     * </pre>
     * @param zipCode The ZIP code to test against the range
     * @param range The range to be tested for the ZIP code
     * @return true if the specified ZIP code is in the specified range; otherwise false
     */
    public static boolean isInRange(int zipCode, StringZipRange range) {
        boolean result = false;
        if (range != null) {
            result = (range.getStart() <= zipCode && zipCode <= range.getEnd());
        }
        return result;
    }
}