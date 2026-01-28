package com.vianavitor;

import com.vianavitor.utils.LexicalToken;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
    // TODO: define the grammar
    /*
        <VOBJ>      -> { <KV> }
        <VOBJ>      -> { <KV2> }
        <VOBJ>      -> {}
        <KV>        -> <K> : <VTYPE>
        <KV2>       -> <KV>, <KV>
        <KV2>       -> <KV>, <KV2>
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
    // CONSTRAINTS
    private final String numberConstraint  = "^[-+]?\\d*[.]?\\d*$";

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
            System.err.println("the key has no value: value missing");
            return;
        }

        // Example of parsing Value
        if (current.value().matches(numberConstraint))  {
            System.out.println("is a number");
        } else {
            System.out.println("is a string");
        }

        if (check(Lexer.TokenName.NEXT)) {
            keyValues();
        }
    }

    private void block() {
        if (!expect(Lexer.TokenName.START)) {
            System.err.println("no start found: '{' missing");
            return;
        }

        if (!check(Lexer.TokenName.KEY)) {
            keyValues();
        }

        if (!expect(Lexer.TokenName.END)) {
            System.err.println("no end found: '}' missing");
        }
    }

    public void analyse() {
        System.out.println("analysing...");
        block();
        System.out.println("analyse completed!");
    }
}
