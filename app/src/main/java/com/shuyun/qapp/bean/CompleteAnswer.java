package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sunxiao on 2018/5/4.
 * 完成答题需要提交的数据
 */

public class CompleteAnswer {

    private String id;//试卷id,对应申请答题服务端返回的id
    private String exam;//试卷开始答题时间
    private String hand;//完成答题时间
    private int status;//答题状态:1-完成试卷;2-放弃试卷;如果最后都是超时,也按照完成处理
    private List<Answers> answers;

    private String deviceId;//设备id 数美sdk返回的设备id

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getHand() {
        return hand;
    }

    public void setHand(String hand) {
        this.hand = hand;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Answers> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answers> answers) {
        this.answers = answers;
    }

    public CompleteAnswer() {
    }

    public CompleteAnswer(String id, String exam, String hand, int status, List<Answers> answers) {
        this.id = id;
        this.exam = exam;
        this.hand = hand;
        this.status = status;
        this.answers = answers;
    }

    public static class Answers implements Parcelable{
        private int id;//题目id
        private String exam;//本题目的开始答题时间
        private String hand;//本题目的答题完成时间毫秒
        private int status;//题目回答状态：1-等待中;2-完成回答;3-超时回答;4-放弃回答;
        private int option;//本题目选择的选项id,多个用逗号分隔;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getExam() {
            return exam;
        }

        public void setExam(String exam) {
            this.exam = exam;
        }

        public String getHand() {
            return hand;
        }

        public void setHand(String hand) {
            this.hand = hand;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getOption() {
            return option;
        }

        public void setOption(int option) {
            this.option = option;
        }

        public static Creator<Answers> getCREATOR() {
            return CREATOR;
        }

        public Answers() {
        }

        public Answers(int id, String exam, String hand, int status, int option) {
            this.id = id;
            this.exam = exam;
            this.hand = hand;
            this.status = status;
            this.option = option;
        }

        protected Answers(Parcel in) {
            id = in.readInt();
            exam = in.readString();
            hand = in.readString();
            status = in.readInt();
            option = in.readInt();
        }

        public static final Creator<Answers> CREATOR = new Creator<Answers>() {
            @Override
            public Answers createFromParcel(Parcel in) {
                return new Answers(in);
            }

            @Override
            public Answers[] newArray(int size) {
                return new Answers[size];
            }
        };

        @Override
        public String toString() {
            return "Answers{" +
                    "id=" + id +
                    ", exam='" + exam + '\'' +
                    ", hand='" + hand + '\'' +
                    ", status=" + status +
                    ", option=" + option +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(exam);
            dest.writeString(hand);
            dest.writeInt(status);
            dest.writeInt(option);
        }
    }

    @Override
    public String toString() {
        return "CompleteAnswer{" +
                "id='" + id + '\'' +
                ", exam='" + exam + '\'' +
                ", hand='" + hand + '\'' +
                ", status=" + status +
                ", answers=" + answers +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
