package fcu.ms.data;

public class User {
    private int id;
    private String name;
    private String firebaseUid;
    private int point;

    public User() {

    }

    public User(int id, String name, String firebaseUid) {
        this.id = id;
        this.name = name;
        this.firebaseUid = firebaseUid;
    }

    public User(String name, String firebaseUid) {
        this.name = name;
        this.firebaseUid = firebaseUid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firebaseUid='" + firebaseUid + '\'' +
                ", point='" + point + '\'' +
                '}';
    }
}

