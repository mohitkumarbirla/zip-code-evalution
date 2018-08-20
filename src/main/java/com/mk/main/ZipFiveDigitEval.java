package com.mk.main;

import com.mk.util.Utils;
import com.mk.model.StringZipRange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
 /**
 * Main class to to test the 5-digit ZIP code
 */
public class ZipFiveDigitEval {
     /**
     * Default runnable method.
     * @param {@code (see: inputZipCodeList.txt)}
     */
    public static void main(String[] args) {
        List<StringZipRange> inputRanges = new ArrayList<>();
        /*
         	ranges would be read from a text file
        */
         try (BufferedReader br = new BufferedReader(new InputStreamReader(ZipFiveDigitEval.class.getResourceAsStream("/inputZipCodeList.txt")))) {
            for (String line; (line = br.readLine()) != null;) {
                inputRanges.add(new StringZipRange(line));
            }
        }
        catch (IOException e) {
            // On an application, this will be written to error log file
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("\nZip Code input range:\n" + inputRanges);
        List<StringZipRange> excludes = Utils.consolidate(inputRanges);
        System.out.println("\nZip Code after exclusion range:\n" + excludes);        
    }
 }