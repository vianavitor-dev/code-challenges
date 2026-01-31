import com.vianavitor.Lexer;
import com.vianavitor.Parser;
import com.vianavitor.utils.LexicalToken;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParserProcessTest {

    private final static String[] jsonText = {
            "{}", "{ }}", "{ \"A\":\"abc\" }", "{ \"A\":\"abc\", \"B\":\"bcd\" }",
            "{", "{{}", "{ \"C\": abc }", "{ \"A\":\"abc\", \"B\":\"bcd\", \"C\":\"cde\"  }",
            "{\n \"key1\": true,\n  \"key2\": false,\n  \"key3\": null,\n  \"key4\": \"value\",\n  \"key5\": 101\n}",
            "{\n" + "  \"key\": \"value\",\n" +"  \"key-n\": 101,\n" + "  \"key-o\": {},\n" +"  \"key-l\": []\n" + "}\n",
            "{ \"key\" : [ 0 ] }",
            "{ \"key-arr\" : [ \"02\", \"1a\", \"2c\" ] }",
            "{ \"key-arr\" : [ 2, 1, 2 ] }",
            "{ \"key-arr\" : [ false, true ] }",
            "{ \"key-arr\" : [ false, 0 ] }",
            "{ \"key-obj\" : { \"key-1\": \"val1\", \"key\": null } }",
            "{ \"key-arr-obj\" : [ { \"key-11\": \"val11\", \"key-12\": null }, { \"key-21\": \"val21\", \"key22\": null } ] }"
    };

    public static Stream<String> valueProvider() {
        return Stream.of(jsonText);
    }

    @ParameterizedTest(name = "token #{index}")
    @MethodSource("valueProvider")
    @Order(1)
    public void lexerTest(String jsonText) {
        Lexer lexer = new Lexer();

        System.out.println("\n* LEXER TOKEN TEST: ");
        List<LexicalToken> list = lexer.tokenization(jsonText);

        for (LexicalToken token : list) {
            System.out.println(token.name() + " - " + token.value());
        }
    }

    @ParameterizedTest(name = "parser #{index}")
    @MethodSource("valueProvider")
    @Order(2)
    public void parserText(String jsonText) {
        Lexer lexer = new Lexer();

        System.out.println("\n* NEW INPUT ANALYSES: ");
        List<LexicalToken> list = lexer.tokenization(jsonText);
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Parser parser = new Parser(list);
        parser.analyse();
    }
}
