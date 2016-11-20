package dream.young.codegen.generator;

import com.github.jknack.handlebars.Template;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import dream.young.codegen.helper.JdbcHelper;
import dream.young.codegen.helper.TemplateHelper;
import dream.young.codegen.model.Table;
import dream.young.codegen.model.Templater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Desc: 模板生成类
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/4/23
 */
@Component
public class TemplateGenerator implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TemplateGenerator.class);

    private final JdbcHelper jdbcHelper;

    private final TemplateHelper templateHelper;

    public static final String BACKEND = "/hbs/backend/";

    public static final String FRONTEND = "/hbs/frontend/";

    @Autowired
    public TemplateGenerator(JdbcHelper jdbcHelper, TemplateHelper templateHelper) {
        this.jdbcHelper = jdbcHelper;
        this.templateHelper = templateHelper;
    }

    @Override
    public void run(String... args) throws Exception {
        Template mapper = templateHelper.compile(backPath("Mapper"));
        Template java = templateHelper.compile(backPath("Model"));
        Template dao = templateHelper.compile(backPath("Dao"));
        Template readService = templateHelper.compile(backPath("ReadService"));
        Template readImpl = templateHelper.compile(backPath("ReadImpl"));
        Template writeService = templateHelper.compile(backPath("WriteService"));
        Template writeImpl = templateHelper.compile(backPath("WriteImpl"));
        Template controller = templateHelper.compile(backPath("Controller"));

        Template testDao = templateHelper.compile(backPath("test-Dao"));
        Template testService = templateHelper.compile(backPath("test-Service"));
        Template testBaseWeb = templateHelper.compile(backPath("test-BaseWeb"));
        Template testController = templateHelper.compile(backPath("test-Controller"));

        for (Table table : jdbcHelper.getTables()) {
            generateTemplate(Lists.newArrayList(
                    new Templater(testController, table.getClassName() + "sTest.java"),
                    new Templater(testBaseWeb, "BaseWebTest.java"),
                    new Templater(testService, table.getClassName() + "ServiceTest.java"),
                    new Templater(testDao, table.getClassName() + "DaoTest.java"),
                    new Templater(mapper, table.getClassLowerFirst() + "Mapper.xml"),
                    new Templater(java, table.getClassName() + ".java"),
                    new Templater(dao, table.getClassName() + "Dao.java"),
                    new Templater(readService, table.getClassName() + "ReadService.java"),
                    new Templater(readImpl, table.getClassName() + "ReadServiceImpl.java"),
                    new Templater(writeService, table.getClassName() + "WriteService.java"),
                    new Templater(writeImpl, table.getClassName() + "WriteServiceImpl.java"),
                    new Templater(controller, table.getClassName() + "s.java")
            ), table);
        }
    }

    private void generateTemplate(List<Templater> templaters, Table table) {
        try {
            for (Templater templater : templaters) {
                Files.write(templater.getTemplate().apply(table), new File(templater.getFileName()), Charsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("generate template failed, table:{}, cause:{}", table, Throwables.getStackTraceAsString(e));
        }
    }

    private static String backPath(String path) {
        return BACKEND + path;
    }

    private static String frontPath(String path) {
        return FRONTEND + path;
    }
}
