package com.vianavitor.utils;

import com.vianavitor.Lexer;

public record LexicalToken(
        Lexer.TokenName name,
        String value
) { }
