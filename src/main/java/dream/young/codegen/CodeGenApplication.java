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

    public static final String BANNER = "▄█▄    ████▄ ██▄   ▄███▄     ▄▀  ▄███▄      ▄   \n" +
            "█▀ ▀▄  █   █ █  █  █▀   ▀  ▄▀    █▀   ▀      █  \n" +
            "█   ▀  █   █ █   █ ██▄▄    █ ▀▄  ██▄▄    ██   █ \n" +
            "█▄  ▄▀ ▀████ █  █  █▄   ▄▀ █   █ █▄   ▄▀ █ █  █ \n" +
            "▀███▀        ███▀  ▀███▀    ███  ▀███▀   █  █ █ \n" +
            "                                         █   ██ \n" +
            "                                                \n";

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
