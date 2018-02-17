package app.go_doggies.com.go_doggies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anto004 on 1/9/18.
 */

public class ClientDetails implements Parcelable {

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
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(this.getClass() != o.getClass()){
            return false;
        }
        ClientDetails c = (ClientDetails) o;
        if(clientId != null ? !clientId.equals(c.clientId) : c.clientId != null){
            return false;
        }
        if(clientName != null ? !clientName.equals(c.clientName) : c.clientName != null){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(clientId);
    }


    @Override
    public String toString() {
        return "ClientDetailsActivity{" +
                "clientId='" + clientId + '\'' +
                ", clientType='" + clientType + '\'' +
                ", comment=" + comment +
                ", clientName='" + clientName + '\'' +
                ", clientImg='" + clientImg + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clientId);
        dest.writeString(this.clientType);
        dest.writeString(this.comment == null ? "" : String.valueOf(this.comment));
        dest.writeString(this.clientName);
        dest.writeString(this.clientImg);
        dest.writeString(this.clientPhone);
    }

    protected ClientDetails(Parcel in) {
        this.clientId = in.readString();
        this.clientType = in.readString();
        this.comment = (Object)in.readString();
        this.clientName = in.readString();
        this.clientImg = in.readString();
        this.clientPhone = in.readString();
    }

    public static final Parcelable.Creator<ClientDetails> CREATOR = new Parcelable.Creator<ClientDetails>() {
        @Override
        public ClientDetails createFromParcel(Parcel source) {
            return new ClientDetails(source);
        }

        @Override
        public ClientDetails[] newArray(int size) {
            return new ClientDetails[size];
        }
    };
}
