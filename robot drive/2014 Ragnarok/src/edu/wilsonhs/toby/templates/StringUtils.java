/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates;

import com.sun.squawk.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author Dev
 */
public class StringUtils {
    
    public static String[] splitAt(String toSplit, String splitAt){
        StringTokenizer tokenizer = new StringTokenizer(toSplit, splitAt);
        String[] splitted = new String[tokenizer.countTokens()-1];
        for(int i = 0; i < splitted.length; i++){
            splitted[i] = tokenizer.nextToken();
        }
        return splitted;
    }
    
}
