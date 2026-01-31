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
        if (!token.hasNext()) {
            System.err.println("hasn't next");
            return current.name() == _tokenName;
        }

        if (current != null) {
            Lexer.TokenName previous = current.name();
            current = token.next();

            if (current.name() == previous) {
                System.err.println("duplicate token: " + current.name());
            }
        } else {
            current = token.next();
        }

        return current.name() == _tokenName;
    }

    private boolean missing(Lexer.TokenName _tokenName) {
        return !is(_tokenName);
    }

    public enum keyValueType {
        EMPTY(0), BOOL(1), STR(2), NUM(3), UNDEFINED(4);

        private final int val;

        keyValueType(int _val) {
            this.val = _val;
        }
    }

    /*
        validate the type of the value, if the type valid returns it else throw an exception
     */
    private keyValueType validateType(String value) {
        keyValueType type = keyValueType.UNDEFINED;

        // CONSTRAINTS
        String numberConstraint    =    "^[-+]?(\\d*[.]?\\d*)$";
        String nullConstraint      =    "^([Nn]ull|NULL|[Nn]one)?$";
        String booleanConstraint   =    "^([Ff]alse|[Tt]rue)$";
        String stringConstraint    =    "^\"(\\w.*)\"$";

        // Example of parsing Value
        if (value.matches(nullConstraint)){
            System.out.println("- it's empty");
            type = keyValueType.EMPTY;
        }
        else if (value.matches(booleanConstraint)) {
            System.out.println("- it's a boolean");
            type = keyValueType.BOOL;
        }
        else if (value.matches(stringConstraint)) {
            System.out.println("- it's a string");
            type = keyValueType.STR;
        }
        else if (value.matches(numberConstraint))  {
            System.out.println("- it's a number");
            type = keyValueType.NUM;
        }
        else {
            // TODO: throw an exception
            System.err.println("> undefined value type");
        }

        return type;
    }

    private void arrayValues(keyValueType _type, String space) {
        if (current.name() != Lexer.TokenName.VALUE) {
            return;
        }
        
        String value = current.value().trim();
        keyValueType currentType = validateType(value);

        // TODO: throw exception
        if (_type.compareTo(keyValueType.UNDEFINED) != 0 && currentType.compareTo(_type) != 0) {
            System.err.println(space + "> inconsistent type of array value: expected " + _type + ", returned " + currentType);
        }

        /*
            if there is any other value within this array it checks if the types match, if not it returns an exception
         */
        if (current.name() == Lexer.TokenName.NEXT || is(Lexer.TokenName.NEXT)) {
            if (is(Lexer.TokenName.VALUE)) {
                arrayValues(currentType, space + "- ");
            }
        }
    }

    private void array(String space) {
        if (missing(Lexer.TokenName.ARRAY_START)) {
            System.err.println(space + "> not found array start: '[' missing");
            return;
        }

        System.out.println(space + current.name());

        if (is(Lexer.TokenName.VALUE)) {
            System.out.println(space + current.name());
            arrayValues(keyValueType.UNDEFINED, " -");
        }
        if (current.name() == Lexer.TokenName.ARRAY_END) {
            System.out.println(space + current.name());
            return;
        }

        System.err.println(space + "> not found array end: ']' missing");
        System.out.println(space + current.name());
    }
    
    private void keyValues(String space) {
        if (current.name() != Lexer.TokenName.KEY) {
            return;
        }

        /*
            check the possible types of this KEYs VALUE
         */
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

        // if there is an NEXT it calls the keyValues() again
        if (current.name() == Lexer.TokenName.NEXT || is(Lexer.TokenName.NEXT)) {
            if (is(Lexer.TokenName.KEY)) {
                keyValues("->");
            }
        }
    }

    /*
        check the syntax related to the object type: { ... }
     */
    private void block(String space) {
        if (missing(Lexer.TokenName.START)) {
            System.err.println(space + "> not found start: '{' missing");
            return;
        }

        System.out.println(space + current.name());

        if (is(Lexer.TokenName.KEY)) {
            System.out.println(space + current.name());
            keyValues(" ");

            // check if this object is null
        }
        if (current.name() == Lexer.TokenName.END ) {
            System.out.println(space + current.name());
            return;
        }

        System.err.println(space + "> not found end: '}' missing");
    }

    public void analyse() {
        System.out.println("analysing...");
        block("#");

        if (token.hasNext()) {
            System.err.println("identified character after end of object!");
        }

        System.out.println("analyse completed!");
    }
}
