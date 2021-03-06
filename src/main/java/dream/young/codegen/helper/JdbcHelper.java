package dream.young.codegen.helper;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import dream.young.codegen.model.Field;
import dream.young.codegen.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/4/25
 */
@Component
public class JdbcHelper {
    private static final Logger log = LoggerFactory.getLogger(JdbcHelper.class);

    @Value("${tableNames:}")
    private String tableNames;

    @Value("${projectPrefix:}")
    private String projectPrefix;

    @Value("${removeClassPrefix: false}")
    private boolean removeClassPrefix;

    @Value("${packagePath:io.terminus}")
    private String packagePath;

    @Value("${bundle:terminus}")
    private String bundle;

    private List<Table> tables = Lists.newArrayList();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Table> getTables() {
        return tables;
    }

    @PostConstruct
    public void init() throws Exception {
        if (!StringUtils.hasText(tableNames)) {
            throw new Exception("Please specify at least one table!");
        }
        List<String> names = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(tableNames);
        for (String name : names) {
            if (StringUtils.hasText(name)) {
                tables.add(getTable(name));
            }
        }
    }

    @SuppressWarnings("all")
    private Table getTable(String tableName) {
        try {
            //列出字段完整属性, 过滤掉id
            List<Map<String, Object>> fieldMaps = getFieldMap(tableName);

            //获取表注释
            String tableComment = getTableComment(tableName);

            //获取建表语句
            String createSql = getCreateSql(tableName);

            Table table = new Table();
            table.setTableName(tableName);      //表名
            table.setClassName(getClassName(tableName));      //类名
            table.setClassLowerFirst(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, table.getClassName()));
            table.setComment(tableComment);    //表注释
            table.setParamName(getParamName(tableName));  //属性名
            table.setParamUpperFirst(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table.getParamName()));

            table.setFields(Lists.transform(fieldMaps, new Function<Map<String, Object>, Field>() {
                @Override
                public Field apply(Map<String, Object> input) {
                    return mapToField(input);
                }
            }));

            table.setCreateSql(createSql);
            table.setPackagePath(packagePath);
            table.setBundle(bundle);
            return table;
        } catch (Exception e) {
            log.error("get table info fail, tableName:{}, cause:{}", tableName, Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    //表名转换为类名, 复数的情况就简单考虑吧 o(╯□╰)o
    private String getClassName(String tableName) {
        String upperCamel = getUpperCamel(tableName);
        if (!removeClassPrefix) {
            return upperCamel;
        }
        return upperCamel.toLowerCase().startsWith(projectPrefix.toLowerCase()) ? upperCamel.substring(projectPrefix.length(), upperCamel.length()) : upperCamel;
    }

    private String getUpperCamel(String tableName) {
        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
        return className.endsWith("s") ? className.substring(0, className.length() - 1) : className;
    }

    //类名转换为属性名, ParanaUser -> user
    private String getParamName(String tablename) {
        String upperCamel = getUpperCamel(tablename);
        upperCamel = upperCamel.toLowerCase().startsWith(projectPrefix.toLowerCase()) ? upperCamel.substring(projectPrefix.length(), upperCamel.length()) : upperCamel;
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, upperCamel);
    }

    //设置字段属性
    private static Field mapToField(Map<String, Object> fieldMap) {
        Field field = new Field();
        field.setColumn(String.valueOf(fieldMap.get("Field")));
        field.setProperty(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, field.getColumn()));
        field.setPropertyUpperFirst(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getProperty()));

        String type = String.valueOf(fieldMap.get("Type"));
        field.setType(getJavaType(type));
        field.setLength(getFieldLength(type));
        field.setComment(String.valueOf(fieldMap.get("Comment")));
        return field;
    }

    //转换下类型
    private static String getJavaType(String jdbcType) {
        jdbcType = jdbcType.toLowerCase();
        if (jdbcType.contains("bigint")) return "Long";
        if (jdbcType.contains("tinyint") || jdbcType.contains("bit")) return "Boolean";
        if (jdbcType.contains("int")) return "Integer";
        if (jdbcType.contains("double")) return "Double";
        if (jdbcType.contains("float")) return "Float";
        if (jdbcType.contains("date") || jdbcType.contains("time") || jdbcType.contains("year")) return "Date";
        return "String";
    }

    //截取出类型中的字段长度 比如：bigint(20) -> 20
    private static Integer getFieldLength(String type) {
        try {
            String length = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
            return StringUtils.hasText(length) ? Integer.valueOf(length) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private List<Map<String, Object>> getFieldMap(String tableName) {
        String sql = "SHOW FULL FIELDS FROM " + tableName + " WHERE Field != 'id'";
        return jdbcTemplate.queryForList(sql);
    }

    private String getTableComment(String tableName) {
        String sql = "SHOW TABLE STATUS WHERE NAME = '" + tableName + "'";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return String.valueOf(results.get(0).get("Comment"));
    }

    private String getCreateSql(String tableName) {
        String sql = "SHOW CREATE TABLE " + tableName;
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return "DROP TABLE IF EXISTS `" + tableName + "`;\n" + results.get(0).get("Create Table") + ";\n";
    }
}
