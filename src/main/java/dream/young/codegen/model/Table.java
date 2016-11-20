package dream.young.codegen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/4/25
 */
@Data
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
}
