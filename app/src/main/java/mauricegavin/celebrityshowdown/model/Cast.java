package mauricegavin.celebrityshowdown.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cast implements Parcelable {

    public String id;
	public String character;
    public String cast_id;
	public String name;
    public int order ;
    public String profile_path;

	/*
	 * Sample response
	 * 
	 {
      "cast_id": 4,
      "character": "The Narrator",
      "credit_id": "52fe4250c3a36847f80149f3",
      "id": 819,
      "name": "Edward Norton",
      "order": 0,
      "profile_path": "/iUiePUAQKN4GY6jorH9m23cbVli.jpg"
    },
	 * 
	 */
	public String getText() {
		return character+" - "+name;
	}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.character);
        dest.writeString(this.cast_id);
        dest.writeString(this.name);
        dest.writeInt(this.order);
        dest.writeString(this.profile_path);
    }

    public Cast() {
    }

    protected Cast(Parcel in) {
        this.id = in.readString();
        this.character = in.readString();
        this.cast_id = in.readString();
        this.name = in.readString();
        this.order = in.readInt();
        this.profile_path = in.readString();
    }

    public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>() {
        public Cast createFromParcel(Parcel source) {
            return new Cast(source);
        }

        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };
}
