package dream.young.codegen.generator;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import dream.young.codegen.CustomTemplates;
import dream.young.codegen.helper.JdbcHelper;
import dream.young.codegen.helper.TemplateHelper;
import dream.young.codegen.model.Table;
import dream.young.codegen.model.Templater;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

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

    private final CustomTemplates customTemplates;

    @Autowired
    public TemplateGenerator(JdbcHelper jdbcHelper, TemplateHelper templateHelper, CustomTemplates customTemplates) {
        this.jdbcHelper = jdbcHelper;
        this.templateHelper = templateHelper;
        this.customTemplates = customTemplates;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("CodeGen START!, now: {}", DateTime.now());

        //backend service template
        Template mapper = templateHelper.compile(hbsBackPath("Mapper"));
        Template java = templateHelper.compile(hbsBackPath("Model"));
        Template dao = templateHelper.compile(hbsBackPath("Dao"));
        Template readService = templateHelper.compile(hbsBackPath("ReadService"));
        Template readImpl = templateHelper.compile(hbsBackPath("ReadImpl"));
        Template writeService = templateHelper.compile(hbsBackPath("WriteService"));
        Template writeImpl = templateHelper.compile(hbsBackPath("WriteImpl"));
        Template controller = templateHelper.compile(hbsBackPath("Controller"));
        Template messages = templateHelper.compile(hbsBackPath("messages_zh_CN"));

        //backend unit test template
        Template testSchema = templateHelper.compile(hbsBackPath("test-schema"));
        Template testDao = templateHelper.compile(hbsBackPath("test-Dao"));
        Template testService = templateHelper.compile(hbsBackPath("test-Service"));
        Template testBaseWeb = templateHelper.compile(hbsBackPath("test-BaseWeb"));
        Template testController = templateHelper.compile(hbsBackPath("test-Controller"));

        //frontend template
        Template template_backend_context = templateHelper.compile(hbsFrontPath("template_backend_context"));
        Template template_backend_operator = templateHelper.compile(hbsFrontPath("template_backend_operator"));
        Template template_frotend_update = templateHelper.compile(hbsFrontPath("template_frotend_update"));
        Template view_coffee = templateHelper.compile(hbsFrontPath("view_coffee"));
        Template view_hbs = templateHelper.compile(hbsFrontPath("view_hbs"));
        Template view_scss = templateHelper.compile(hbsFrontPath("view_scss"));
        Template yaml_config_back = templateHelper.compile(hbsFrontPath("yaml_config_back"));
        Template yaml_config_front = templateHelper.compile(hbsFrontPath("yaml_config_front"));
        Template yaml_locale_enUS = templateHelper.compile(hbsFrontPath("yaml_locale_enUS"));
        Template yaml_locale_zhCN = templateHelper.compile(hbsFrontPath("yaml_locale_zhCN"));

        //custom template
        //List<Templater> customs = Lists.newArrayList();


        for (Table table : jdbcHelper.getTables()) {
            List<Templater> templaters = Lists.newArrayList(
                    new Templater(template_backend_context, tableFrontBackendBath(table, "unit_context.hbs")),
                    new Templater(template_backend_operator, tableFrontBackendBath(table, "unit_operator.hbs")),
                    new Templater(template_frotend_update, tableFrontFrontendBath(table, "update.hbs")),
                    new Templater(view_coffee, tableFrontBath(table, "view.coffee")),
                    new Templater(view_hbs, tableFrontBath(table, "view.hbs")),
                    new Templater(view_scss, tableFrontBath(table, "view.scss")),
                    new Templater(yaml_config_back, tableFrontResourceBath(table, "back_config.yaml")),
                    new Templater(yaml_config_front, tableFrontResourceBath(table, "front_config.yaml")),
                    new Templater(yaml_locale_enUS, tableFrontLocaleBath(table, "en_US.yaml")),
                    new Templater(yaml_locale_zhCN, tableFrontLocaleBath(table, "zh_CN.yaml")),

                    new Templater(messages, tableBackBath(table, "messages_zh_CN.properties")),
                    new Templater(testController, tableBackTestBath(table, table.getClassName() + "sTest.java")),
                    new Templater(testBaseWeb, tableBackTestBath(table, "BaseWebTest.java")),
                    new Templater(testService, tableBackTestBath(table, table.getClassName() + "ServiceTest.java")),
                    new Templater(testDao,tableBackTestBath(table,  table.getClassName() + "DaoTest.java")),
                    new Templater(testSchema,tableBackTestBath(table, "schema.sql")),
                    new Templater(mapper, tableBackBath(table, table.getClassLowerFirst() + "Mapper.xml")),
                    new Templater(java, tableBackBath(table, table.getClassName() + ".java")),
                    new Templater(dao, tableBackBath(table, table.getClassName() + "Dao.java")),
                    new Templater(readService, tableBackBath(table, table.getClassName() + "ReadService.java")),
                    new Templater(readImpl, tableBackBath(table, table.getClassName() + "ReadServiceImpl.java")),
                    new Templater(writeService, tableBackBath(table, table.getClassName() + "WriteService.java")),
                    new Templater(writeImpl, tableBackBath(table, table.getClassName() + "WriteServiceImpl.java")),
                    new Templater(controller, tableBackBath(table, table.getClassName() + "s.java"))
            );
            templaters.addAll(getCustomTemplates(table));
            generateTemplate(templaters, table);
        }
        log.info("CodeGen OK!, now:{}", DateTime.now());
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

    private List<Templater> getCustomTemplates(Table table) {
        List<Templater> templaters = Lists.newArrayList();
        if (customTemplates == null || isEmpty(customTemplates.getTemplates())) {
            return templaters;
        }
        for (CustomTemplates.CustomTemplate custom : customTemplates.getTemplates().values()) {
            try {
                Templater templater = new Templater();
                templater.setFileName(tableRootBath(table, custom.getFileName()));
                templater.setTemplate(templateHelper.compileInline(Files.toString(new File(appendHbs(custom.getFromHbs())), Charsets.UTF_8)));
                templaters.add(templater);
            } catch (Exception e) {
                log.error("get custom templates failed, customTemplates:{}, cause:{}",
                        customTemplates.getTemplates(), Throwables.getStackTraceAsString(e));
            }
        }
        return templaters;
    }

    private static String appendHbs(String hbs) {
        if (!hbs.endsWith(".hbs")) {
            return hbs + ".hbs";
        }
        return hbs;
    }

    private static String hbsBackPath(String path) {
        return "/hbs/backend/" + path;
    }

    private static String hbsFrontPath(String path) {
        return "/hbs/frontend/" + path;
    }

    private static String tableRootBath(Table table, String fileName) {
        return table.getTableName() + "/" + fileName;
    }

    private static String tableBackBath(Table table, String fileName) {
        return table.getTableName() + "/backend/" + fileName;
    }

    private static String tableBackTestBath(Table table, String fileName) {
        return table.getTableName() + "/backend/test/" + fileName;
    }

    private static String tableFrontBath(Table table, String fileName) {
        return table.getTableName() + "/frontend/" + table.getBundle() + "/" + table.getParamName() + "/" + fileName;
    }

    private static String tableFrontBackendBath(Table table, String fileName) {
        return table.getTableName() + "/frontend/" + table.getBundle() + "/" + table.getParamName() + "/backend_templates/" + fileName;
    }

    private static String tableFrontFrontendBath(Table table, String fileName) {
        return table.getTableName() + "/frontend/" + table.getBundle() + "/" + table.getParamName() + "/frontend_templates/" + fileName;
    }

    private static String tableFrontResourceBath(Table table, String fileName) {
        return table.getTableName() + "/frontend/" + table.getBundle() + "/resources/" + fileName;
    }

    private static String tableFrontLocaleBath(Table table, String fileName) {
        return table.getTableName() + "/frontend/" + table.getBundle() + "/" + table.getParamName() + "/locales/" + fileName;
    }
}
