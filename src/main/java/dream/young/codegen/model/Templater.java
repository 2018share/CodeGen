package dream.young.codegen.model;

import com.github.jknack.handlebars.Template;

import java.io.Serializable;

/**
 * Desc:
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 16/5/20
 */
public class Templater implements Serializable {

    private Template template;

    private String fileName;

    public Templater() {
    }

    public Templater(Template template, String fileName) {
        this.template = template;
        this.fileName = fileName;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
