package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.ObjectType;

import java.util.Objects;

public class UserTask extends Task {
    private final ObjectType<UserData> user;

    public UserTask() {
        user = new ObjectType<>("User of task", null, true);
    }

    public UserData getUser() {
        return user.getValue();
    }

    public void setUser(UserData user) {
        this.user.setValue(user);
        this.user.setAdjusted(true);
    }

    public ObjectType<UserData> getUserCover() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserTask) || !super.equals(o)) {
            return false;
        }
        UserTask userTask = (UserTask) o;
        return user.equals(userTask.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                super.toString() + "," +
                "user=" + user +
                '}';
    }

    @Override
    public UserTask replicate() {
        UserTask value = new UserTask();
        value.update(this);
        return value;
    }

    @Override
    public void update(Object object) {
        super.update(object);
        if(object instanceof UserTask) {
            UserTask value = (UserTask) object;
            if(value.getUserCover().isAdjusted()) {
                setUser(value.getUser());
            }
        }
    }
}
