package dream.young.codegen;

import com.google.common.collect.Maps;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Desc:
 * Mail: yangzl@terminus.io
 * author: DreamYoung
 * Date: 2016/11/25
 */
@ConfigurationProperties(prefix = "custom")
public class CustomTemplates {

    private Map<String, CustomTemplate> templates = Maps.newHashMap();

    public Map<String, CustomTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, CustomTemplate> templates) {
        this.templates = templates;
    }


    public static class CustomTemplate {
        private String fromHbs;
        private String fileName;

        public String getFromHbs() {
            return fromHbs;
        }

        public void setFromHbs(String fromHbs) {
            this.fromHbs = fromHbs;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
