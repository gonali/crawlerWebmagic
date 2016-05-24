package com.gonali.crawler.parser.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JYlsc on 2015/12/14.
 */
public class Templates {

    /**
     * 获取以domain为域名的所有模版列表
     *
     * @param domain
     * @return
     */
    public static List<BaseTemplate> getTemplates(String domain, List<BaseTemplate> listTemplate) {
        List<BaseTemplate> ans = new ArrayList<>();
        for (BaseTemplate baseTemplate : listTemplate) {
            if (baseTemplate.getDomain().equals(domain))
                ans.add(baseTemplate);
        }
        return ans;
    }


}
