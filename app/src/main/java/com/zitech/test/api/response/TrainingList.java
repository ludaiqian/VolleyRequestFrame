package com.zitech.test.api.response;

import com.zitech.test.api.entity.Traninng;

import java.util.List;

/**
 * Created by lu on 2016/3/25.
 */
public class TrainingList extends Response {

    String chapterName;//		当前章节简称
    String section;//		当前小节（1.1）
    String period;///		当前课时（第几讲的几）
    String periodId;///		课时id
    List<Traninng> contents;

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public List<Traninng> getContents() {
        return contents;
    }

    public void setContents(List<Traninng> contents) {
        this.contents = contents;
    }
}
