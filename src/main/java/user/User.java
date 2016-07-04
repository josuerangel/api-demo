/**
 * Created by joshua on 7/3/16.
 */
package user;

public class User {
    private long id;
    private String login;
    private int groupid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public User(long id, String login) {
        this.id = id;
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    //dummy
    public User(){
        //dummy
        this.id = 0;
        this.login = "";
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
