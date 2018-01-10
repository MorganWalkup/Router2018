package com.morganwalkup.router2018.support;

/**
 * Created by morganwalkup on 1/9/18.
 */

// Container for handy methods used across multiple classes
public class Utilities {

    /* padHexString - this function prepends an inputString of hex characters
     * with the proper number of zeros to conform to the provided byteCount
     * @return - a hex string representing the requested number of bytes
     */
    static public String padHexString(String inputString, int byteCount) {
        String outputString = inputString; //The properly formatted string to return
        int hexCharCount = byteCount * 2; //The number of hex characters needed in the output string
        int inputCharCount = inputString.length(); //The number of characters in the input string
        int missingHexCharCount = hexCharCount - inputCharCount; //The remaining number of hex characters needed for the output string

        // Prepend appropriate number of zeros to output string
        for(int i = 0; i < missingHexCharCount; i++) {
            outputString = "0" + outputString;
        }

        return outputString;
    }

}
