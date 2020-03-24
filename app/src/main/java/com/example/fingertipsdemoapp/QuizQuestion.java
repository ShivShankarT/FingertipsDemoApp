package com.example.fingertipsdemoapp;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizQuestion implements Parcelable {

    // "id": "2461",
    private int id;
    private int point;
    //"question": "Which law of Newton is called the law of<span> equilibrium?</span>",
    private String question;
    private String questionStatus;
    private String questionImage;
    private String optionImage;
    private boolean isSpecialType;
    private String answer;
    private String ansImageA;
    private String ansImageB;
    private String ansImageC;

    private String ansImageD;
    private QuestionOption[] options;
    private int selectedOptionPos = -1;
    private String questionExplaination;
    private String questionExplanationImage;


    public QuizQuestion() {

    }


    protected QuizQuestion(Parcel in) {
        id = in.readInt();
        point = in.readInt();
        question = in.readString();
        questionImage = in.readString();
        questionStatus=in.readString();
        optionImage = in.readString();
        isSpecialType = in.readByte() != 0;
        answer = in.readString();
        ansImageA = in.readString();
        ansImageB = in.readString();
        ansImageC = in.readString();
        ansImageD = in.readString();
        options = in.createTypedArray(QuestionOption.CREATOR);
        selectedOptionPos = in.readInt();
        questionExplaination = in.readString();
        questionExplanationImage = in.readString();
    }

    public static final Creator<QuizQuestion> CREATOR = new Creator<QuizQuestion>() {
        @Override
        public QuizQuestion createFromParcel(Parcel in) {
            return new QuizQuestion(in);
        }

        @Override
        public QuizQuestion[] newArray(int size) {
            return new QuizQuestion[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(String questionStatus) {
        this.questionStatus = questionStatus;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getOptionImage() {
        return optionImage;
    }

    public void setOptionImage(String optionImage) {
        this.optionImage = optionImage;
    }

    public boolean isSpecialType() {
        return isSpecialType;
    }

    public void setSpecialType(boolean specialType) {
        isSpecialType = specialType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QuestionOption[] getOptions() {
        return options;
    }

    public void setOptions(QuestionOption[] options) {
        this.options = options;
    }

    public int getSelectedOptionPos() {
        return selectedOptionPos;
    }

    public void setSelectedOptionPos(int selectedOptionPos) {
        this.selectedOptionPos = selectedOptionPos;
    }

    public String optionPositionToString() {
        int pos = selectedOptionPos;
        if (pos == 0) {
            return "A";

        } else if (pos == 1) {
            return "B";

        } else if (pos == 2) {
            return "C";
        } else if (pos == 3) {
            return "D";
        } else if (pos == 4) {
            return "E";
        }

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(point);
        dest.writeString(question);
        dest.writeString(questionImage);
        dest.writeString(optionImage);
        dest.writeByte((byte) (isSpecialType ? 1 : 0));
        dest.writeString(answer);
        dest.writeString(ansImageA);
        dest.writeString(questionStatus);
        dest.writeString(ansImageB);
        dest.writeString(ansImageC);
        dest.writeString(ansImageD);
        dest.writeTypedArray(options, flags);
        dest.writeInt(selectedOptionPos);
        dest.writeString(questionExplaination);
        dest.writeString(questionExplanationImage);
    }


    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getQuestionExplanationImage() {
        return questionExplanationImage;
    }

    public void setQuestionExplanationImage(String questionExplanationImage) {
        this.questionExplanationImage = questionExplanationImage;
    }

    public void setQuestionExplaination(String questionExplaination) {
        this.questionExplaination = questionExplaination;
    }

    public String getQuestionExplaination() {
        return questionExplaination;
    }

    public static class QuestionOption implements Parcelable {
        public static final Creator<QuestionOption> CREATOR = new Creator<QuestionOption>() {
            @Override
            public QuestionOption createFromParcel(Parcel in) {
                return new QuestionOption(in);
            }

            @Override
            public QuestionOption[] newArray(int size) {
                return new QuestionOption[size];
            }
        };
        String option;
        String optionType;

        public QuestionOption(Parcel in) {
            option = in.readString();
            optionType = in.readString();
        }

        public QuestionOption(String option, String optionType) {
            this.option = option;
            this.optionType = optionType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(option);
            dest.writeString(optionType);
        }

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public String getOptionType() {
            return optionType;
        }

        public void setOptionType(String optionType) {
            this.optionType = optionType;
        }
    }
}
