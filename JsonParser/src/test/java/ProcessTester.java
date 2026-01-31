import com.vianavitor.Lexer;
import com.vianavitor.Parser;
import com.vianavitor.utils.LexicalToken;
import org.junit.Test;

import java.util.List;

public class ProcessTester {

    @Test
    public void lexerText() {
        String[] samples = {
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
        };

        for (String sample : samples) {
            Lexer lexer = new Lexer();

            System.out.println("\n* NEW INPUT ANALYSES: ");

            List<LexicalToken> list = lexer.tokenization(sample);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Parser parser = new Parser(list);
            parser.analyse();
        }
    }
}
