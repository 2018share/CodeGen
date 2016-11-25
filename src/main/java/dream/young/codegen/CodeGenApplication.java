package dream.young.codegen;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * Desc:
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/4/27
 */
@SpringBootApplication
public class CodeGenApplication {

    public static final String BANNER = "   _____          _       _____            \n" +
            "  / ____|        | |     / ____|           \n" +
            " | |     ___   __| | ___| |  __  ___ _ __  \n" +
            " | |    / _ \\ / _` |/ _ \\ | |_ |/ _ \\ '_ \\ \n" +
            " | |___| (_) | (_| |  __/ |__| |  __/ | | |\n" +
            "  \\_____\\___/ \\__,_|\\___|\\_____|\\___|_| |_|\n" +
            "                                           \n" +
            "                                           \n";

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CodeGenApplication.class);
        application.setBanner(new Banner() {
            @Override
            public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
                out.print(BANNER);
            }
        });
        application.run(args);
    }
}
