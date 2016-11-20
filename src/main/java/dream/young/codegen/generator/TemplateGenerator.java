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

    @Autowired
    public TemplateGenerator(JdbcHelper jdbcHelper, TemplateHelper templateHelper) {
        this.jdbcHelper = jdbcHelper;
        this.templateHelper = templateHelper;
    }

    @Override
    public void run(String... args) throws Exception {
        //backend service template
        Template mapper = templateHelper.compile(hbsBackPath("Mapper"));
        Template java = templateHelper.compile(hbsBackPath("Model"));
        Template dao = templateHelper.compile(hbsBackPath("Dao"));
        Template readService = templateHelper.compile(hbsBackPath("ReadService"));
        Template readImpl = templateHelper.compile(hbsBackPath("ReadImpl"));
        Template writeService = templateHelper.compile(hbsBackPath("WriteService"));
        Template writeImpl = templateHelper.compile(hbsBackPath("WriteImpl"));
        Template controller = templateHelper.compile(hbsBackPath("Controller"));

        //backend unit test template
        Template testSchema = templateHelper.compile(hbsBackPath("test-schema"));
        Template testDao = templateHelper.compile(hbsBackPath("test-Dao"));
        Template testService = templateHelper.compile(hbsBackPath("test-Service"));
        Template testBaseWeb = templateHelper.compile(hbsBackPath("test-BaseWeb"));
        Template testController = templateHelper.compile(hbsBackPath("test-Controller"));

        // TODO: 2016/11/20 frontend  template

        for (Table table : jdbcHelper.getTables()) {
            generateTemplate(Lists.newArrayList(
                    new Templater(testController, tableTestBackBath(table, table.getClassName() + "sTest.java")),
                    new Templater(testBaseWeb, tableTestBackBath(table, "BaseWebTest.java")),
                    new Templater(testService, tableTestBackBath(table, table.getClassName() + "ServiceTest.java")),
                    new Templater(testDao,tableTestBackBath(table,  table.getClassName() + "DaoTest.java")),
                    new Templater(testSchema,tableTestBackBath(table, "schema.sql")),
                    new Templater(mapper, tableBackBath(table, table.getClassLowerFirst() + "Mapper.xml")),
                    new Templater(java, tableBackBath(table, table.getClassName() + ".java")),
                    new Templater(dao, tableBackBath(table, table.getClassName() + "Dao.java")),
                    new Templater(readService, tableBackBath(table, table.getClassName() + "ReadService.java")),
                    new Templater(readImpl, tableBackBath(table, table.getClassName() + "ReadServiceImpl.java")),
                    new Templater(writeService, tableBackBath(table, table.getClassName() + "WriteService.java")),
                    new Templater(writeImpl, tableBackBath(table, table.getClassName() + "WriteServiceImpl.java")),
                    new Templater(controller, tableBackBath(table, table.getClassName() + "s.java"))
            ), table);
        }
    }

    private void generateTemplate(List<Templater> templaters, Table table) {
        try {
            for (Templater templater : templaters) {
                File file = new File(templater.getFileName());
                Files.createParentDirs(file);
                Files.write(templater.getTemplate().apply(table), file, Charsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("generate template failed, table:{}, cause:{}", table, Throwables.getStackTraceAsString(e));
        }
    }

    private static String hbsBackPath(String path) {
        return "/hbs/backend/" + path;
    }

    private static String hbsFrontPath(String path) {
        return "/hbs/frontend/" + path;
    }

    private static String tableBackBath(Table table, String fileName) {
        return table.getTableName() + "/backend/" + fileName;
    }

    private static String tableTestBackBath(Table table, String fileName) {
        return table.getTableName() + "/backend/test/" + fileName;
    }

    private static String tableFrontBath(Table table, String fileName) {
        return table.getTableName() + "/frontend/" + fileName;
    }
}
