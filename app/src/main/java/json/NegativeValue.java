package json;

/**
 * Created by vignesh on 6/2/2016.
 */
public class NegativeValue {

    int result_code;
    String result_text;

    public String getText() {
        return result_text;
    }

    public void setText(String text) {
        this.result_text = text;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

}
