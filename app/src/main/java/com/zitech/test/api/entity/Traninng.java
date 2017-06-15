package com.zitech.test.api.entity;

import java.util.List;

/**
 * Created by lu on 2016/3/25.
 */
public class Traninng {
    private String sceneId;    //	场景id
    private String sceneName;//		场景名称名称
    private String sceneIcon;//		场景图标
    private List<TrainingInfo> contents;//		训练简介

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneIcon() {
        return sceneIcon;
    }

    public void setSceneIcon(String sceneIcon) {
        this.sceneIcon = sceneIcon;
    }
}
