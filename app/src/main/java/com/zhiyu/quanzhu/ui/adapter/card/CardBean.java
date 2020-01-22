package com.zhiyu.quanzhu.ui.adapter.card;

/**
 * Created by Administrator on 2018/12/24.
 */

public class CardBean {

    private String pic;
    private String name;
    private String ballYear;
    private String team;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getBallYear() {
        return ballYear;
    }

    public void setBallYear(String ballYear) {
        this.ballYear = ballYear;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "CardBean{" +
                "pic=" + pic +
                ", title='" + name + '\'' +
                ", ballYear='" + ballYear + '\'' +
                ", team='" + team + '\'' +
                '}';
    }
}
