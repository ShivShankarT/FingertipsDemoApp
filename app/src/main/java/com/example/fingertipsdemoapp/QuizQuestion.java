package com.example.fingertipsdemoapp;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizQuestion implements Parcelable {

    private int id;
    private int point;
    private String question;
    private String questionStatus;
    private String questionImage;
    private String optionImage;
    private boolean isSpecialType;
    private boolean isExpSpecialType;
    private String answer;
    private QuestionOption[] options;
    private String selectedOption = null;
    private String questionExplaination;
    private String questionExplanationImage;
    private  boolean isNormalViewRendering;
    private  String source;


    public QuizQuestion() {

    }


    protected QuizQuestion(Parcel in) {
        id = in.readInt();
        point = in.readInt();
        question = in.readString();
        questionStatus = in.readString();
        questionImage = in.readString();
        optionImage = in.readString();
        isSpecialType = in.readByte() != 0;
        isExpSpecialType = in.readByte() != 0;
        answer = in.readString();
        options = in.createTypedArray(QuestionOption.CREATOR);
        selectedOption = in.readString();
        questionExplaination = in.readString();
        questionExplanationImage = in.readString();
        isNormalViewRendering = in.readByte() != 0;
        source = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(point);
        dest.writeString(question);
        dest.writeString(questionStatus);
        dest.writeString(questionImage);
        dest.writeString(optionImage);
        dest.writeByte((byte) (isSpecialType ? 1 : 0));
        dest.writeByte((byte) (isExpSpecialType ? 1 : 0));
        dest.writeString(answer);
        dest.writeTypedArray(options, flags);
        dest.writeString(selectedOption);
        dest.writeString(questionExplaination);
        dest.writeString(questionExplanationImage);
        dest.writeByte((byte) (isNormalViewRendering ? 1 : 0));
        dest.writeString(source);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public boolean isExpSpecialType() {
        return isExpSpecialType;
    }

    public void setExpSpecialType(boolean expSpecialType) {
        isExpSpecialType = expSpecialType;
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

    public boolean isNormalViewRendering() {
        return isNormalViewRendering;
    }

    public void setNormalViewRendering(boolean normalViewRendering) {
        isNormalViewRendering = normalViewRendering;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
        private String option;
        private String optionType;

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

        public String getOptionForwebview() {
            return MyApp.formateEscapeChar(option);
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
