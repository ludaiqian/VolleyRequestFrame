package com.zitech.test.api.request;

/**
 * Created by lu on 2016/3/25.
 */
public class TrainingListRequest extends Request {
    private String subjectId;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
