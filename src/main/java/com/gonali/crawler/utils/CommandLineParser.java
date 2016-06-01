package com.gonali.crawler.utils;

/**
 * Created by TianyuanPan on 6/1/16.
 */
public class CommandLineParser extends LoggerUtil {

    private static final long execStartTime = System.currentTimeMillis();
    private static final int argsLength = 24;

    private static final String usage = "Usage:\n" +
            "\t  -depth <退出深度>\n" +
            "\t  -pass <遍数>\n" +
            "\t  -tid <实例ID>\n" +
            "\t  -startTime <实例启动时间>" +
            "\t  -seedPath <种子路径>\n" +
            "\t  -protocolDir <协议过滤目录>\n" +
            "\t  -type <实例类型>\n" +
            "\t  -recallDepth <回溯点击层数>\n" +
            "\t  -templateDir <模板目录>\n" +
            "\t  -clickRegexDir <点击正则表达式目录>\n" +
            "\t  -postRegexDir <后缀过滤目录>\n" +
            "\t -configPath <配置文件路径>";

    private int depth;
    private int pass;
    private String tid;
    private String startTime;
    private String seedPath;
    private String protocolDir;
    private String type;
    private int recallDepth;
    private String templateDir;
    private String clickRegexDir;
    private String postRegexDir;
    private String configPath;

    private CommandLineParser(){}
    private void parserArgs(String args[]) {

        for (int i = 0; i < args.length; i++) {
            if ("-depth".equals(args[i])) {
                depth = Integer.valueOf(args[i + 1]);
                i++;
            } else if ("-pass".equals(args[i])) {
                pass = Integer.valueOf(args[i + 1]);
                i++;
            } else if ("-tid".equals(args[i])) {
                tid = args[i + 1];
                i++;
            } else if ("-startTime".equals(args[i])) {
                startTime = args[i + 1];
                i++;
            } else if ("-seedPath".equals(args[i])) {
                seedPath = args[i + 1];
                i++;
            } else if ("-protocolDir".equals(args[i])) {
                protocolDir = args[i + 1];
                i++;
            } else if ("-type".equals(args[i])) {
                type = args[i + 1];
                i++;
            } else if ("-recallDepth".equals(args[i])) {
                recallDepth = Integer.valueOf(args[i + 1]);
                i++;
            } else if ("-templateDir".equals(args[i])) {
                templateDir = args[i + 1];
                i++;
            } else if ("-clickRegexDir".equals(args[i])) {
                clickRegexDir = args[i + 1];
                i++;
            } else if ("-postRegexDir".equals(args[i])) {
                postRegexDir = args[i + 1];
                i++;
            } else if ("-configPath".equals(args[i])) {
                configPath = args[i + 1];
                i++;
            }

        }

    }

    public static CommandLineParser getCommandLineParser(String args[]) {

        CommandLineParser parser = new CommandLineParser();

        if (args.length < argsLength) {

            System.out.println(getUsage());
            System.out.println("Current args length: " + args.length + ", args length should be: " + getArgsLength());
            parser.logger.error("Parameter ERROR!!\n" + getUsage());
            parser.logger.error("Current args length: " + args.length + ", args length should be: " + getArgsLength());

            System.exit(-1);
        }

        parser.parserArgs(args);

        return parser;
    }

    public static long getExecStartTime() {
        return execStartTime;
    }

    public static int getArgsLength() {
        return argsLength;
    }

    public static String getUsage() {
        return usage;
    }

    public int getDepth() {
        return depth;
    }

    public int getPass() {
        return pass;
    }

    public String getTid() {
        return tid;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getSeedPath() {
        return seedPath;
    }

    public String getProtocolDir() {
        return protocolDir;
    }

    public String getType() {
        return type;
    }

    public int getRecallDepth() {
        return recallDepth;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public String getClickRegexDir() {
        return clickRegexDir;
    }

    public String getPostRegexDir() {
        return postRegexDir;
    }

    public String getConfigPath() {

        return configPath;
    }
}
