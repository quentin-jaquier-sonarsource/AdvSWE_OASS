package coms.w4156.moviewishlist.Services;

public class YesNo {

    private String answer;
    private String forced;
    private  String image;

    @Override
    public String toString() {
        return "YesNo{" +
                "answer='" + answer + '\'' +
                '}';
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getForced() {
        return forced;
    }

    public void setForced(String forced) {
        this.forced = forced;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
