package dream.young.codegen.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Desc:
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/4/25
 */
@Data
public class Field implements Serializable {
    private static final long serialVersionUID = 8103693675851022158L;

    private String column;      //列名: created_at

    private String property;    //属性名: createdAt

    private String propertyUpperFirst;  //属性名首字母大写：CreateAt

    private String type;        //类型: Date

    private String comment;     //注释: 创建时间
}
