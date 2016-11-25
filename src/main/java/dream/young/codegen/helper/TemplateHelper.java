package dream.young.codegen.helper;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

/**
 * Desc: 自定义helper
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/4/25
 */
@Component
public class TemplateHelper {

    private final Handlebars handlebars = new Handlebars();

    private static final DateTimeFormatter DTF = DateTimeFormat.forPattern("yyyy-MM-dd");

    @PostConstruct
    public void init() {
        handlebars.registerHelper("now", new Helper<Object>() {
            @Override
            public CharSequence apply(Object o, Options options) throws IOException {
                return DateTime.now().toString(DTF);
            }
        });

        handlebars.registerHelper("notEmpty", new Helper<Object>() {
            @Override
            public CharSequence apply(Object o, Options options) throws IOException {
                return isEmpty(o) ? options.inverse() : options.fn();
            }
        });

        handlebars.registerHelper("brace", new Helper<Object>() {
            @Override
            public CharSequence apply(Object o, Options options) throws IOException {
                return o == null ? "" : "#{" + o + "}";
            }
        });

        handlebars.registerHelper("braceItem", new Helper<Object>() {
            @Override
            public CharSequence apply(Object o, Options options) throws IOException {
                return o == null ? "" : "#{item." + o + "}";
            }
        });

        //解决 {{ }} 会被handlebars识别的问题
        handlebars.registerHelper("left", new Helper<Object>() {
            @Override
            public CharSequence apply(Object o, Options options) throws IOException {
                return "{{";
            }
        });

        handlebars.registerHelper("right", new Helper<Object>() {
            @Override
            public CharSequence apply(Object o, Options options) throws IOException {
                return "}}";
            }
        });

        handlebars.registerHelper("of", new Helper<Object>() {
            @Override
            public CharSequence apply(Object o, Options options) throws IOException {
                if (o == null) {
                    return options.inverse();
                }

                String _source = o.toString();
                String param = options.param(0);
                if (Strings.isNullOrEmpty(param)) {
                    return options.inverse();
                }

                List<String> targets = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(param);
                if (targets.contains(_source)) {
                    return options.fn();
                }
                return options.inverse();
            }
        });

        handlebars.registerHelper("equal", new Helper<Object>() {
            @Override
            public CharSequence apply(Object o, Options options) throws IOException {
                return Objects.equal(o, options.param(0)) ? options.fn() : options.inverse();
            }
        });

    }

    private static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof CharSequence) {
            return ((CharSequence) value).length() == 0 || value.equals("[]");
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() == 0;
        }
        if (value instanceof Iterable) {
            return !((Iterable) value).iterator().hasNext();
        }
        if (value instanceof Boolean) {
            return !(Boolean) value;
        }
        if (value.getClass().isArray()) {
            return Array.getLength(value) == 0;
        }
        return value instanceof Number && ((Number) value).intValue() == 0;
    }

    public Template compile(String location) throws IOException {
        return handlebars.compile(location);
    }

    public Template compileInline(String input) throws IOException {
        return handlebars.compileInline(input);
    }
}
