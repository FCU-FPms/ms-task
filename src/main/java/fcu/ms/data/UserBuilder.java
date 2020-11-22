package fcu.ms.data;

public final class UserBuilder {
    private final int id;
    private String name;
    private String firebaseUid;
    private int point;

    private UserBuilder(int id) {
        this.id = id;
    }

    public static UserBuilder anUser(int id) {
        return new UserBuilder(id);
    }


    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
        return this;
    }

    public UserBuilder withPoint(int point) {
        this.point = point;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setFirebaseUid(firebaseUid);
        user.setPoint(point);
        return user;
    }
}
