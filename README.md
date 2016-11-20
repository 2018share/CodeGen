# CodeGen 代码生成器

通过 schema 生成前后端代码

**使用方式:**

* 复制 application.yml.example 改名为 application.yml
* 修改相应数据源配置和表名
* 在根目录执行如下命令:
    
    ```
    mvn spring-boot:run
    ```
   
    会在根目录生成对应的前后端文件
    
**注意:**
 
 * 生成的Model需要自行生成 serialVersionUID !