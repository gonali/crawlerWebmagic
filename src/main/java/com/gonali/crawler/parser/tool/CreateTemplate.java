package com.gonali.crawler.parser.tool;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Leishichi on 2015/10/15.
 */
public class CreateTemplate {
    static List<String> templateTokens = new ArrayList<String>();
    static List<String> tempTokens = new ArrayList<String>();
    static List<String> newTokens = new ArrayList<String>();
    static String domain;


    static final int MAX_LCS_NUM = 8000;
    static int[][] LCS = new int[MAX_LCS_NUM][MAX_LCS_NUM];
    static String[][] flag = new String[MAX_LCS_NUM][MAX_LCS_NUM];
    private  static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");


    public static void main(String[] arg) throws IOException {
        System.out.println("begin");
        readNewHtml();

    }


    public static void initial() {
        templateTokens.clear();
        tempTokens.clear();
        newTokens.clear();
    }


    /**
     * 读取需要制作模版的列表并制作模版
     *
     * @throws IOException
     */
    public static void readNewHtml() throws IOException {
        String html;
        String url;

//        File inputFile = new File("D:\\IdeaProjects\\webmagicDemo\\data\\tempin.txt");
        File inputFile = new File("/home/TianyuanPan/IdeaProjects/crawlerWebmagic/data/tempin.txt");
        BufferedReader htmlReader = new BufferedReader(new FileReader(inputFile));
        while ((url = htmlReader.readLine()) != null) {
            System.out.println(url);
            initial();
            html = GetHtml.getHtmlFromUrl(url);

            newTokens = JsoupHtml.getHtmlTokens(html);
            domain = AnalysisTool.getDomain(url);
            AnalysisByTemplate();
        }
        htmlReader.close();


    }


    static void updateTemplate(List<String> Template, File templateFile, String domain) throws IOException {

        File file = templateFile;
        BufferedWriter templateWriter = new BufferedWriter(new FileWriter(file));
        templateWriter.write(domain + "\n");
        for (int j = 0; j < Template.size(); j++)
            templateWriter.write(Template.get(j) + "\n");
        templateWriter.close();

    }


    static void AnalysisByTemplate() throws IOException {
        String str;
        String templateDomain;

        //从模版库中读取已有模版文件
//        File file = new File("D:\\IdeaProjects\\webmagicDemo\\templates\\");
        File file = new File("/home/TianyuanPan/IdeaProjects/crawlerWebmagic/templates/");
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }


//        File templatesParentFile = new File("D:\\IdeaProjects\\webmagicDemo\\templates\\");
        File templatesParentFile = new File("/home/TianyuanPan/IdeaProjects/crawlerWebmagic/templates/");
        File[] templateFiles = templatesParentFile.listFiles();
        if (templateFiles.length > 0) {
            for (File templateFile : templateFiles) {
                templateTokens.clear();
                BufferedReader reader = new BufferedReader(new FileReader(templateFile));
                templateDomain = reader.readLine();
                if (!templateDomain.equals(domain)) continue;

                while ((str = reader.readLine()) != null) templateTokens.add(str);
                reader.close();

                if (findTemplate() == 1) {
                    updateTemplate(tempTokens, templateFile, domain);
                    return;
                }

            }
        }
        //updateTemplate(newTokens, new File("D:\\IdeaProjects\\webmagicDemo\\templates\\" + domain + sdf.format(new Date()) + ".txt"), domain);
        updateTemplate(newTokens, new File("/home/TianyuanPan/IdeaProjects/webmagicDemo/templates/" + domain + sdf.format(new Date()) + ".txt"), domain);
        return;
    }


    //判断两个标签是否相等
    static boolean isEquals(String oldToken, String newToken) {
        String subOld, subNew;
        subNew = newToken.replaceAll(" ", "");
        subOld = oldToken.replaceAll(" ", "");
        if (getMarketSort(oldToken) != getMarketSort(newToken)) return false;
        if (oldToken.equals(newToken)) return true;
        if (subOld.equals(subNew)) {
            return true;
        }
        return false;
    }

    static String getMarket(String token) {
        String sub;
        if (token.contains(" ")) sub = token.split(" ")[0];
        else {
            sub = token.split(">")[0];
        }
        int i = getMarketSort(token);
        switch (i) {
            case -1:
                sub = sub.substring(2);
                break;
            case 0:
                return token;
            case 1:
            case 2:
                sub = sub.substring(1);
                break;

        }
        return sub;
    }


    static int getMarketSort(String token) {
        if (token.startsWith("<a")) return 0;
        if (token.startsWith("</")) return -1;
        if (token.startsWith("<")) return 1;
        return 0;
    }


    static void LCS(List<String> oldList, List<String> newList) {
        int oldLength = oldList.size();
        int newLength = newList.size();
        int i, j;

        LCS[0][0] = 0;
        for (i = 0; i <= oldLength; i++)
            LCS[i][0] = 0;
        for (i = 0; i <= newLength; i++)
            LCS[0][i] = 0;
        for (i = 0; i <= oldLength; i++)
            for (j = 0; j <= newLength; j++)
                flag[i][j] = "";

        for (i = 1; i <= oldLength; i++) {
            for (j = 1; j <= newLength; j++) {
                if (isEquals(oldList.get(i - 1), newList.get(j - 1))) {
                    LCS[i][j] = LCS[i - 1][j - 1] + 1;
                    flag[i][j] = "left_up";
                } else {
                    if (LCS[i - 1][j] >= LCS[i][j - 1]) {
                        LCS[i][j] = LCS[i - 1][j];
                        flag[i][j] = "left";
                    } else {
                        LCS[i][j] = LCS[i][j - 1];
                        flag[i][j] = "up";
                    }
                }
            }
        }
        return;
    }


    //��ȡ���������
    static List<String> getLCSList(List<String> oldList, List<String> newList) {
        List<String> listLCS = new ArrayList<String>();
        int i = oldList.size();
        int j = newList.size();
        while (true) {
            if (i == 0 || j == 0) break;
            if (flag[i][j].equals("left_up")) {
                listLCS.add(newList.get(j - 1));
                i--;
                j--;
                continue;
            } else {
                if (flag[i][j] == "up") {
                    j--;
                    continue;
                } else {
                    i--;
                    continue;
                }
            }
        }
        return listLCS;
    }


    /**
     * 获取列表中标签的数量
     *
     * @param list
     * @return
     */
    static int getMarketNum(List<String> list) {
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            if (getMarketSort(list.get(i)) != 0) num++;
        }
        return num;

    }


    static int findTemplate() {
        tempTokens.clear();
        int oldTag = 0;
        int newTag = 0;
        int nextTag, needNum;
        int oldLength = templateTokens.size();
        int newLength = newTokens.size();

        //栈：用于存放未匹到标签的列表
        List<String> oldArray = new ArrayList<String>();
        List<String> newArray = new ArrayList<String>();

        //临时列表：用于存放需要进行比对的标签队列
        List<String> aTokens = new ArrayList<String>();
        List<String> bTokens = new ArrayList<String>();

        //临时列表：用于存放需要添加进最后模版的标签队列
        List<String> tempList;


        String newToken, oldToken, tempToken;

        //通过死循环，同时遍历新网页及模版两个队列
        while (true) {

            //当一个队列遍历完成则跳出循环
            if ((oldTag == oldLength) || (newTag == newLength)) break;

            //获取当前遍历到的标签
            oldToken = templateTokens.get(oldTag);
            newToken = newTokens.get(newTag);

            if (isEquals(oldToken, newToken)) {
                tempTokens.add(oldToken);
                oldTag++;
                newTag++;
                if (getMarketSort(oldToken) == 1) {
                    oldArray.add(getMarket(oldToken));
                    newArray.add(getMarket(newToken));
                }
                if (getMarketSort(oldToken) == -1) {
                    if (oldArray.get(oldArray.size() - 1).equals(getMarket(oldToken)))
                        oldArray.remove(oldArray.size() - 1);
                    if (newArray.get(newArray.size() - 1).equals(getMarket(newToken)))
                        newArray.remove(newArray.size() - 1);
                }
            }
            //如果不相等
            else {

                nextTag = 0;
                tempToken = "";
                for (int i = 0; i < oldArray.size(); i++) {
                    if (!oldArray.get(i).equals(newArray.get(i))) break;
                    tempToken = oldArray.get(i);
                    nextTag = i;
                }
                if (nextTag == 0) break;

                for (int i = nextTag; i < oldArray.size() - 1; i++) {
                    oldArray.remove(i);
                    newArray.remove(i);
                }


                needNum = 0;
                aTokens.clear();
                while (oldTag < oldLength) {
                    oldToken = templateTokens.get(oldTag);
                    if (getMarket(oldToken).equals(tempToken)) {
                        if ((needNum == 0) && (getMarketSort(oldToken) == -1)) break;
                        needNum += getMarketSort(oldToken);
                    }
                    aTokens.add(oldToken);
                    oldTag++;
                }

                needNum = 0;
                bTokens.clear();
                while (newTag < newLength) {
                    newToken = newTokens.get(newTag);
                    if (getMarket(newToken).equals(tempToken)) {
                        if ((needNum == 0) && (getMarketSort(newToken) == -1)) break;
                        needNum += getMarketSort(newToken);
                    }
                    bTokens.add(newToken);
                    newTag++;
                }

                if (aTokens.size() > 1 && bTokens.size() > 1) {
                    LCS(aTokens, bTokens);
                    tempList = getLCSList(aTokens, bTokens);
                    for (int i = tempList.size() - 1; i >= 0; i--) {
                        tempTokens.add(tempList.get(i));
                    }
                }
            }
        }


        //
        System.out.println((getMarketNum(tempTokens) + 0.0) / getMarketNum(templateTokens));
        if ((getMarketNum(tempTokens) + 0.0) / getMarketNum(templateTokens) > 0.7) return 1;
        else return 0;
    }

}
