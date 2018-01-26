package com.morganwalkup.support;

import com.morganwalkup.networks.Constants;

/**
 * Container for handy methods used across multiple classes
 *
 * @author Morgan Walkup
 * @version 1.0
 * @since 1/11/18
 */
public class Utilities {

    /**
     * Prepends an inputString of hex characters with the proper
     * number of zeros to conform to the provided byteCount
     * @param inputString - The unpadded hex string
     * @param byteCount - The desired byteCount represented by the padded hex string
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

    /**
     * Converts a hex string to the ASCII equivalent
     * @param hexString - The hex string to be converted
     * @return The ASCII equivalent of a series of hex characters
     */
    static public String convertHexToASCII(String hexString) {
        StringBuilder asciiString = new StringBuilder();
        for (int i = 0; i < hexString.length(); i+=2) {
            String byteString = hexString.substring(i, i+2);
            asciiString.append((char)Integer.parseInt(byteString, Constants.HEX_BASE));
        }

        return asciiString.toString();
    }

    /**
     * Converts an ASCII string to a string of the Hex equivalents
     * @param asciiString - The string of ASCII characters to be converted
     * @return The Hex equivalent of a series of ASCII characters
     */
    static public String convertASCIIToHEX(String asciiString) {
        char[] chars = asciiString.toCharArray();

        // Add hex string equivalents to a new string buffer
        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
    }

}
