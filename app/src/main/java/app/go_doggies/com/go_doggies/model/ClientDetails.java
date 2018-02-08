package app.go_doggies.com.go_doggies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anto004 on 1/9/18.
 */

public class ClientDetails {

    @SerializedName("client_id")
    private String clientId;
    @SerializedName("client_type")
    private String clientType;
    @SerializedName("comment")
    private Object comment;
    @SerializedName("client_name")
    private String clientName;
    @SerializedName("client_img")
    private String clientImg;
    @SerializedName("client_phone")
    private String clientPhone;

    public ClientDetails(String clientId, String clientType, Object comment, String clientName, String clientImg, String clientPhone) {
        this.clientId = clientId;
        this.clientType = clientType;
        this.comment = comment;
        this.clientName = clientName;
        this.clientImg = clientImg;
        this.clientPhone = clientPhone;
    }

    public ClientDetails(String clientName){
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientImg() {
        return clientImg;
    }

    public void setClientImg(String clientImg) {
        this.clientImg = clientImg;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    @Override
    public String toString() {
        return "ClientDetails{" +
                "clientId='" + clientId + '\'' +
                ", clientType='" + clientType + '\'' +
                ", comment=" + comment +
                ", clientName='" + clientName + '\'' +
                ", clientImg='" + clientImg + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                '}';
    }
}
