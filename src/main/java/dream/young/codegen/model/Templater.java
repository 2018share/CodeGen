package dream.young.codegen.model;

import com.github.jknack.handlebars.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Desc:
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/5/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Templater implements Serializable {

    private Template template;

    private String fileName;
}
