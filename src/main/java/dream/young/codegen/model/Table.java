package dream.young.codegen.model;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/4/25
 */
public class Table implements Serializable {
    private static final long serialVersionUID = 1486537741435478791L;

    private String tableName;    //表名称: parana_order_items

    private String className;    //对应类名: ParanaOrderItem

    private String classLowerFirst; //对应类名首字母小写: paranaOrderItem

    private String paramName;    //对应类名的属性名(通常是去掉了项目名称前缀): orderItem

    private String paramUpperFirst; //属性名首字母大写: OrderItem

    private String comment;      //表注释

    private List<Field> fields;  //字段

    private String createSql;    //建表语句

    private String packagePath;  //后端包路径

    private String bundle;       //前端bundle

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassLowerFirst() {
        return classLowerFirst;
    }

    public void setClassLowerFirst(String classLowerFirst) {
        this.classLowerFirst = classLowerFirst;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamUpperFirst() {
        return paramUpperFirst;
    }

    public void setParamUpperFirst(String paramUpperFirst) {
        this.paramUpperFirst = paramUpperFirst;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getCreateSql() {
        return createSql;
    }

    public void setCreateSql(String createSql) {
        this.createSql = createSql;
    }
}
