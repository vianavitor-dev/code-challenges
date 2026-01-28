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
                "{", "{{}", "{ \"C\": abc }"
        };

        for (String sample : samples) {
            Lexer lexer = new Lexer();

            List<LexicalToken> list = lexer.tokenization(sample);

            Parser parser = new Parser(list);
            parser.analyse();
        }
    }
}
