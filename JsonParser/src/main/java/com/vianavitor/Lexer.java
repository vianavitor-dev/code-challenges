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
        END,        // example: }

    }

    List<LexicalToken> tokenList = new ArrayList<>();

    public List<LexicalToken> tokenization(String text) {
        System.out.println(text);

//        if (!Pattern.matches("^([{]).*([}])$", text)) {
//            System.err.println("Either no start or end found");
//            System.exit(1);
//        }

        int nextCount = 0;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '{') {
                tokenList.add(new LexicalToken(TokenName.START, null));
            }

            if (ch == '}') {
                tokenList.add(new LexicalToken(TokenName.END, null));
            }

            if (ch == ',') {
                tokenList.add(new LexicalToken(TokenName.NEXT, String.valueOf(nextCount)));
                nextCount ++;
            }

            if (ch == '"') {
                boolean isKey = tokenList.getLast().name() == TokenName.START
                        || tokenList.getLast().name() == TokenName.NEXT;

                int j = 1;
                ch = text.charAt(i+j);
                StringBuilder sb = new StringBuilder();

                while (ch != '"') {
                    sb.append(ch);

                    j++;
                    ch = text.charAt(i+j);
                }

                TokenName current = isKey ? TokenName.KEY : TokenName.VALUE;
                tokenList.add(new LexicalToken(current, sb.toString()));

                i+= j;
            }
        }

        System.out.println(tokenList);
        return tokenList;
    }
}
