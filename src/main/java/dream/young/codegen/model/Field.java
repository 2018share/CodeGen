package dream.young.codegen.model;

import java.io.Serializable;

/**
 * Desc:
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/4/25
 */
public class Field implements Serializable {
    private static final long serialVersionUID = 8103693675851022158L;

    private String column;      //列名: created_at

    private String property;    //属性名: createdAt

    private String propertyUpperFirst;  //属性名首字母大写：CreateAt

    private String type;        //类型: Date

    private Integer length;     //长度：20

    private String comment;     //注释: 创建时间

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getPropertyUpperFirst() {
        return propertyUpperFirst;
    }

    public void setPropertyUpperFirst(String propertyUpperFirst) {
        this.propertyUpperFirst = propertyUpperFirst;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
