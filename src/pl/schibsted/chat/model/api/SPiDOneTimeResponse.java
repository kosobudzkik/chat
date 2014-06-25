package pl.schibsted.chat.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author krzysztof.kosobudzki
 */
public class SPiDOneTimeResponse {
    private String error;
    @SerializedName("error_code")
    private int errorCode;
    private String type;
    @SerializedName("error_description")
    private String errorDescription;
    private SPiDSimpleData data;

    public SPiDOneTimeResponse() {

    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public SPiDSimpleData getData() {
        return data;
    }

    public void setData(SPiDSimpleData data) {
        this.data = data;
    }
}
