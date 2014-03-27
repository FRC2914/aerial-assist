/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates;

import com.sun.squawk.util.StringTokenizer;
/**
 *
 * @author Toby
 */
public class StringUtils {
    
    public static String[] splitAt(String toSplit, String splitAt){
        StringTokenizer tokenizer = new StringTokenizer(toSplit, splitAt);
        if(toSplit.indexOf(splitAt) != -1){
        String[] splitted = new String[tokenizer.countTokens()];
        for(int i = 0; i < splitted.length; i++){
            splitted[i] = tokenizer.nextToken();
        }
        return splitted;
        }
        String[] splitted = {toSplit};
        return splitted;
    }
    
}
