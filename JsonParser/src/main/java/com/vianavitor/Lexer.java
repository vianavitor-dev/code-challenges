package com.vianavitor;

import com.vianavitor.utils.LexicalToken;

import java.util.*;
import java.util.regex.Pattern;

public class Lexer {
    private String text;

    public enum TokenName {
        START,      // example: {
        KEY,        // example: "A: .....
        VALUE,      // example: ..: "abc"
        NEXT,       // example: ,
        ARRAY_START,// example: [...
        ARRAY_END,  // example: ....]
        END,        // example: }

    }

    List<LexicalToken> tokenList = new ArrayList<>();

    public List<LexicalToken> tokenization(String text) {
        text = text.replace(" ", "");
        System.out.println(text);

        int nextCount = 0;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '{') {
                tokenList.add(new LexicalToken(TokenName.START, null));
            }

            boolean isKey = tokenList.getLast().name() == TokenName.START || tokenList.getLast().name() == TokenName.NEXT;
            if (ch == '"' && isKey) {
                int j = 1;
                ch = text.charAt(i+j);
                StringBuilder sb = new StringBuilder();

                while (ch != '"') {
                    sb.append(ch);

                    j++;
                    ch = text.charAt(i+j);
                }

                tokenList.add(new LexicalToken(TokenName.KEY, sb.toString()));

                i += j;
            }

            if (ch == ':') {
                int j = 1;
                char end = ',';

                if (ch == '[') {
                    tokenList.add(new LexicalToken(TokenName.ARRAY_START, null));

                    end = ']';
                    j++;
                } else if (ch == '"') {

                    end = '"';
                    j++;
                }

                StringBuilder value = new StringBuilder();
                ch = text.charAt(i+j);

                while (ch != end && ch != '}') {
                    value.append(ch);

                    j++;
                    ch = text.charAt(i+j);
                }

                tokenList.add(new LexicalToken(TokenName.VALUE, value.toString()));
                i += j;
            }

            if (ch == ']') {
                tokenList.add(new LexicalToken(TokenName.ARRAY_END, null));
            }

            if (ch == ',') {
                tokenList.add(new LexicalToken(TokenName.NEXT, String.valueOf(nextCount)));
                nextCount ++;
            }

            if (ch == '}') {
                tokenList.add(new LexicalToken(TokenName.END, null));
            }
        }

        System.out.println(tokenList);
        return tokenList;
    }
}
