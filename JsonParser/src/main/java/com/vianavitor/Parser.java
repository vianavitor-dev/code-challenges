package com.vianavitor;

import com.vianavitor.utils.LexicalToken;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Parser {
    private final Iterator<LexicalToken> token;
    private LexicalToken current;

    public Parser(List<LexicalToken> _token) {
        this.token = _token.iterator();
    }

    private boolean is(Lexer.TokenName _tokenName) {
        if (token.hasNext()) {
            current = token.next();
        }
        return current.name() == _tokenName;
    }

    private boolean missing(Lexer.TokenName _tokenName) {
        return !is(_tokenName);
    }

    private Object validateType(String value) {
        Object type = null;

        // CONSTRAINTS
        String numberConstraint    =    "^[-+]?(\\d*[.]?\\d*)$";
        String nullConstraint      =    "^([Nn]ull|NULL|[Nn]one)?$";
        String booleanConstraint   =    "^([Ff]alse|[Tt]rue)$";
        String stringConstraint    =    "^\"(\\w.*)\"$";

        // Example of parsing Value
        if (value.matches(nullConstraint)){
            System.out.println("- it's empty");
        }
        else if (value.matches(booleanConstraint)) {
            System.out.println("- it's a boolean");
            type = Boolean.class;
        }
        else if (value.matches(stringConstraint)) {
            System.out.println("- it's a string");
            type = String.class;
        }
        else if (value.matches(numberConstraint))  {
            System.out.println("- it's a number");
            type = Number.class;
        }
        else {
            // TODO: throw an exception
            System.err.println("> undefined value type");
        }

        return type;
    }

    private void arrayValues(Object _type, String space) {
        if (current.name() != Lexer.TokenName.VALUE) {
            return;
        }
        
        String value = current.value().trim();
        Object currentType = validateType(value);
        
        if (is(Lexer.TokenName.NEXT)) {
            if (_type != null && _type != currentType) {
                System.err.println("> inconsistent type of array value: expected " + _type.toString() + ", returned " + currentType.toString());
            }
            arrayValues(currentType, space + "- ");
        }
    }
    
    private void array(String space) {
        if (missing(Lexer.TokenName.ARRAY_START)) {
            System.err.println(space + "> not found array start: '[' missing");
            return;
        }

        System.out.println(current.name());
        arrayValues(null, " -");

        if (missing(Lexer.TokenName.ARRAY_END)) {
            System.err.println(space + "> not found array end: ']' missing");
        }
        System.out.println(current.name());
    }
    
    private void keyValues(String space) {
        if (missing(Lexer.TokenName.KEY)) {
            return;
        }

        if (is(Lexer.TokenName.ARRAY)) {
            System.out.println("- it's an array");
            array(" *");
        } else if (current.name() == Lexer.TokenName.OBJ) {
            System.out.println("- it's an object");
            block(" #");
        } else if (current.name() != Lexer.TokenName.VALUE) {
            System.err.println(space + "> the key has no value: value missing");
            return;
        }

        if (current.name() == Lexer.TokenName.VALUE) {
            String value = current.value().trim();
            validateType(value);
        }

        if (current.name() == Lexer.TokenName.NEXT || is(Lexer.TokenName.NEXT)) {
            keyValues("->");
        }
    }

    private void block(String space) {
        if (missing(Lexer.TokenName.START)) {
            System.err.println(space + "> not found start: '{' missing");
            return;
        }

        System.out.println(space + current.name());
        keyValues(" ");

        if (current.name() != Lexer.TokenName.END && missing(Lexer.TokenName.END)) {
            System.err.println(space + "> not found end: '}' missing");
        }

        System.out.println(space + current.name());
    }

    public void analyse() {
        System.out.println("analysing...");
        block("#");

        if (token.hasNext()) {
            System.err.println("syntax error");
        }

        System.out.println("analyse completed!");
    }
}
