package com.zhiyu.quanzhu.model.bean;

public class VersionApp {
    private String version;
    private boolean isforce;
    private String url;
    private String changelog;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isIsforce() {
        return isforce;
    }

    public void setIsforce(boolean isforce) {
        this.isforce = isforce;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }
}
