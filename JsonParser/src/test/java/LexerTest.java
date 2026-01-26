import com.vianavitor.Lexer;
import org.junit.Test;
import org.junit.runners.Parameterized;
import picocli.CommandLine;

public class LexerTest {

    @Test
    public void lexerText() {
        String[] samples = {
                "{}", "{ }}", "{ \"A\":\"abc\" }", "{ \"A\":\"abc\", \"B\":\"bcd\" }"
        };

        for (String sample : samples) {
            Lexer lexer = new Lexer();
            lexer.tokenization(sample);
        }
    }
}
