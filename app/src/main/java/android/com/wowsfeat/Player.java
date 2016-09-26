package android.com.wowsfeat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 595056078 on 2016/8/29.
 */

public class Player implements Parcelable{
    private String name;//账户的名字
    private int count;//查询的次数

    public Player(){}

    public Player(String name, int count){
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    protected Player(Parcel in) {
        name = in.readString();
        count = in.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeInt(count);
    }
}
