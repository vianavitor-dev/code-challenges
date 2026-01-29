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
                "{\n" + "  \"key\": \"value\",\n" +"  \"key-n\": 101,\n" + "  \"key-o\": {},\n" +"  \"key-l\": []\n" + "}\n"
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
