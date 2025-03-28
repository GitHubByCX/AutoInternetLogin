package org.cx.autointernetlogin.util;

import org.cx.autointernetlogin.config.ProjectConfig;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * YAML 配置文件帮助类
 */
public class YamlUtils {

    /**
     * 从指定文件路径获取项目配置
     *
     * @param filePath 项目配置文件路径
     * @return 项目配置
     * @throws FileNotFoundException 配置文件不存在
     */
    public static ProjectConfig getProjectConfig(String filePath) throws FileNotFoundException {
        LoaderOptions options = new LoaderOptions();
        TagInspector tagInspector = tag -> tag.getClassName().equals(ProjectConfig.class.getName());
        options.setTagInspector(tagInspector);
        Yaml yaml = new Yaml(new Constructor(ProjectConfig.class, options));
        FileReader fileReader = new FileReader(filePath);
        return yaml.load(fileReader);
    }

    /**
     * 保持当前项目配置到指定路径
     *
     * @param saveFilePath  要保存的目标文件路径
     * @param projectConfig 项目配置
     * @throws IOException 保存失败
     */
    public static void saveProjectConfig(String saveFilePath, ProjectConfig projectConfig) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        FileWriter fileWriter = new FileWriter(saveFilePath);
        yaml.dump(projectConfig, fileWriter);
    }

}
