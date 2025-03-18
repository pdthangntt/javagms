package com.gms.entity.rabbit;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public class OnesignalMessage extends RabbitMessage {

    private String title;
    private String content;
    private String url;
    private List<String> playerIds;

    public OnesignalMessage() {
    }

    public OnesignalMessage(String title, String content, String url, List<String> playerIds) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.playerIds = playerIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<String> playerIds) {
        this.playerIds = playerIds;
    }

}
