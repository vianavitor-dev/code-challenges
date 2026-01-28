package com.vianavitor;

import com.vianavitor.utils.LexicalToken;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
    // TODO: define the grammar
    /*
        <VOBJ>      -> { <KV> }
        <VOBJ>      -> { <KV> <NEXT> }
        <VOBJ>      -> {}
        <KV>        -> <K> : <VTYPE>
        <NEXT>      -> <KV>
        <NEXT>      -> <KV>, <NEXT>
        <K>         -> "..."
        <VYTPE>     -> <VINT>
        <VYTPE>     -> <VDOUBLE>
        <VYTPE>     -> "<VSTRING>"
        <VYTPE>     -> <VARRAY>
        <VYTPE>     -> <VOBJ>
        <VYTPE>     -> <VNULL>
        <VARRAY>    -> [ <VITEM> ]
        <VITEM>     -> <VTYPE>
        <VITEM>     -> <VTYPE>, <VITEM>

        # OBSERVATIONS:
        Possible <VTYPE>'s value => "", 0, 0.0, [], {} and NULL
     */

    private final Iterator<LexicalToken> token;
    private LexicalToken current;

    public Parser(List<LexicalToken> _token) {
        this.token = _token.iterator();
    }

    private boolean check(Lexer.TokenName _tokenName) {
        if (token.hasNext()) {
            current = token.next();
        }
        return current.name() == _tokenName;
    }

    private boolean expect(Lexer.TokenName _tokenName) {
        return check(_tokenName);
    }

    private void keyValues() {
        if (!check(Lexer.TokenName.KEY)) {
            return;
        }

        if (!expect(Lexer.TokenName.VALUE)) {
            System.err.println("> the key has no value: value missing");
            return;
        }

        // CONSTRAINTS
        String numberConstraint = "^[-+]?(\\d*[.]?\\d*)$";
        String nullConstraint = "^([Nn]ull|NULL|[Nn]one)?$";
        String booleanConstraint = "^([Ff]alse|[Tt]rue)$";
        String stringConstraint = "^\"(\\w.*)\"$";

        String value = current.value().trim();
        
        // Example of parsing Value
        if (value.matches(nullConstraint)){
            System.out.println("- it's empty");
        }
        else if (value.matches(booleanConstraint)) {
            System.out.println("- it's a boolean");
        }
        else if (value.matches(stringConstraint)) {
            System.out.println("- it's a string");
        }
        else if (value.matches(numberConstraint))  {
            System.out.println("- it's a number");
        }
        else {
            System.err.println("> undefined value type");
        }

        if (check(Lexer.TokenName.NEXT)) {
            keyValues();
        }
    }

    private void block() {
        if (!expect(Lexer.TokenName.START)) {
            System.err.println("> no start found: '{' missing");
            return;
        }

        keyValues();

        if (!expect(Lexer.TokenName.END)) {
            System.err.println("> no end found: '}' missing");
        }
    }

    public void analyse() {
        System.out.println("analysing...");
        block();
        System.out.println("analyse completed!");
    }
}
