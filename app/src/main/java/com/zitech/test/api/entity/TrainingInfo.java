package com.zitech.test.api.entity;

/**
 * Created by lu on 2016/3/25.
 */
public class TrainingInfo {
    private String trainingId;//训练id
    private String userTrainingId;//		用户训练id
    private String trainingName;//训练名称
    private int timeBall;//		可获得的时光球数量

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getUserTrainingId() {
        return userTrainingId;
    }

    public void setUserTrainingId(String userTrainingId) {
        this.userTrainingId = userTrainingId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public int getTimeBall() {
        return timeBall;
    }

    public void setTimeBall(int timeBall) {
        this.timeBall = timeBall;
    }
}
