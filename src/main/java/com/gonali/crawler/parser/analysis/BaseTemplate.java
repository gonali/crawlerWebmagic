package com.gonali.crawler.parser.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JYlsc on 2015/12/14.
 */
public class BaseTemplate implements Serializable{

    List<String> tokens = new ArrayList<>();
    String domain;


    public BaseTemplate(String domain, List<String> tokens) {
        this.domain = domain;
        this.tokens = tokens;
    }


    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
