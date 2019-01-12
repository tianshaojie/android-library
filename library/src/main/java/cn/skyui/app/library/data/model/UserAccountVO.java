package cn.skyui.app.library.data.model;

import java.io.Serializable;

/**
 * Created by tiansj on 2018/4/8.
 */

public class UserAccountVO  implements Serializable {

    private Long id = 0L;

    private Integer availableIncomeCoin = 0;

    private Integer availableRechargeCoin = 0;

    private Integer freezeCoin = 0;

    private Integer minutes = 0;

    private String answerRate = "100%";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAvailableIncomeCoin() {
        return availableIncomeCoin;
    }

    public void setAvailableIncomeCoin(Integer availableIncomeCoin) {
        this.availableIncomeCoin = availableIncomeCoin;
    }

    public Integer getAvailableRechargeCoin() {
        return availableRechargeCoin;
    }

    public void setAvailableRechargeCoin(Integer availableRechargeCoin) {
        this.availableRechargeCoin = availableRechargeCoin;
    }

    public Integer getFreezeCoin() {
        return freezeCoin;
    }

    public void setFreezeCoin(Integer freezeCoin) {
        this.freezeCoin = freezeCoin;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getAnswerRate() {
        return answerRate;
    }

    public void setAnswerRate(String answerRate) {
        this.answerRate = answerRate;
    }

}
