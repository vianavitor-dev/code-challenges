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
        ARRAY,      // example: [ ... ]
        OBJ,        // example: { ... }
        ARRAY_START,// example: [...
        ARRAY_END,  // example: ....]
        END,        // example: }

    }

    List<LexicalToken> tokenList = new ArrayList<>();

    public List<LexicalToken> tokenization(String text) {
        // TODO: the KEY VALUEs cannot have their spaces replaced
        text = text.replace(" ", "");
        text = text.replace("\n", "");
        System.out.println(text);

        int nextCount = 0;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '{') {
                tokenList.add(new LexicalToken(TokenName.OBJ, null));
                tokenList.add(new LexicalToken(TokenName.START, null));
            }

            boolean isKey = ch == '"' && (tokenList.getLast().name() == TokenName.START || tokenList.getLast().name() == TokenName.NEXT);

            if (isKey) {
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

                StringBuilder value = new StringBuilder();
                ch = text.charAt(i+j);

                if (ch == '"') {
                    value.append(ch);

                    j++;
                    ch = text.charAt(i+j);

                    while (ch != '}' && ch != ']' && ch != '"') {
                        value.append(ch);

                        j++;
                        ch = text.charAt(i+j);
                    }
                    value.append(ch);
                    tokenList.add(new LexicalToken(TokenName.VALUE, value.toString()));
                    i += j;
                } else if (ch == '[') {
                    tokenList.add(new LexicalToken(TokenName.ARRAY, null));
                    tokenList.add(new LexicalToken(TokenName.ARRAY_START, null));

                    j++;
                    ch = text.charAt(i+j);

                    while (ch != '}' && ch != ']') {
                        if (ch == ',') {
                            tokenList.add(new LexicalToken(TokenName.NEXT, null));
                        }
                        // insert the array values if it is a string
                        else if (ch == '"') {
                            value.append(ch);

                            j++;
                            char arrayValChar = text.charAt(i+j);

                            while (arrayValChar != '"' && arrayValChar != ']' && arrayValChar != '}') {
                                value.append(arrayValChar);

                                j++;
                                arrayValChar = text.charAt(i+j);
                            }

                            value.append(ch);
                            tokenList.add(new LexicalToken(TokenName.VALUE, value.toString()));
                        } else if (ch == '{') {
                            i -= 1;
                            break;
                        }else {
                            char arrayValIntOrBool = text.charAt(i+j);

                            while (arrayValIntOrBool != ',' && arrayValIntOrBool != ']' && arrayValIntOrBool != '}') {
                                value.append(arrayValIntOrBool);

                                j++;
                                arrayValIntOrBool = text.charAt(i+j);
                            }

                            tokenList.add(new LexicalToken(TokenName.VALUE, value.toString()));
                            j-=1;
                        }

                        j++;
                        ch = text.charAt(i+j);
                        value = new StringBuilder();
                    }

                    i += j;
                } else if (ch == '{') {
                    continue;
                } else {
                    ch = text.charAt(i+j);

                    while (ch != '}' && ch != ']' && ch != ',') {
                        value.append(ch);

                        j++;
                        ch = text.charAt(i+j);
                    }
                    tokenList.add(new LexicalToken(TokenName.VALUE, value.toString()));
                    i += j;
                }
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

//        System.out.println(tokenList);
        return tokenList;
    }
}
