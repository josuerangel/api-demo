package user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * Created by joshua on 7/3/16.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/")
    public List users(@RequestParam(value="name", defaultValue="") String name){
        return getData(name);
        //return  new User(counter.incrementAndGet(), String.format(template, name));
    }

    @RequestMapping(value="/{userId}", method = RequestMethod.GET)
    public List readUser(@PathVariable("userId") String userId){
        return getData(userId);
    }

    @RequestMapping(value="/", method = RequestMethod.POST)
    ResponseEntity<List> addUser(@RequestBody User user){
        List arr = insertUpdateUser(user);
        if(arr.size() > 0) return new ResponseEntity(arr,null,HttpStatus.CREATED);
        else return new ResponseEntity(arr,null,HttpStatus.CONFLICT);
    }

    protected List<User> getData(String userId){
        String url = "jdbc:mysql://localhost:3306/rmmiapps";
        String username = "rmmiapps";
        String password = "rmmiapps";
        String query = "Select * from entuser ";
        List<User> users = null;
        User user;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try{
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();
            if (userId != "") {
                query += "where login LIKE '%" + userId + "%' ";
            }
            query += "limit 10";
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            users = new ArrayList<User>();
            while(rs.next()){
                user = new User();
                user.setId(Long.parseLong(rs.getString("userid")));
                user.setName(rs.getString("name"));
                user.setLogin(rs.getString("login"));
                user.setGroupid(Integer.parseInt(rs.getString("groupid")));
                users.add(user);
            }
            //System.out.println(scores.toString());
            rs.close();
            stmt.close();
            stmt = null;
            conn.close();
            conn = null;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return users;
    }

    protected List insertUpdateUser(User user){
        List result = null;
        String url = "jdbc:mysql://localhost:3306/rmmiapps";
        String username = "rmmiapps";
        String password = "rmmiapps";
        String query = "";
        int resultQuery;
        PreparedStatement updateData = null;

        List users = getData(user.getLogin());
        System.out.println(users);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try{
            Connection conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            if (users.size() == 0){
                //query = "insert into entuser(companyid, groupid, name, login, status)" +
                //        "values(322, "+ user.getGroupid() +", '" + user.getName() + "','" + user.getLogin() + "',0)";
                query = "insert into rmmiapps.entuser(companyid, groupid, name, login, status)" +
                        "values(322, ?, ?, ?, 0)";
                updateData = conn.prepareStatement(query);
                updateData.setInt(1, user.getGroupid());
                updateData.setString(2, user.getName());
                updateData.setString(3, user.getLogin());
                users.add(user);
            }
            else{
                query = "update rmmiapps.entuser set groupid = ? where login = ?";
                updateData = conn.prepareStatement(query);
                updateData.setInt(1, user.getGroupid());
                updateData.setString(2, user.getLogin());
            }

            System.out.println(updateData.toString());
            resultQuery = updateData.executeUpdate();
            System.out.println("ejecuto query:" + resultQuery);
            //stmt.close();
            //stmt = null;
            if (resultQuery > 0) {
                result = users;
                conn.commit();
                stmt.close();
                stmt = null;
            }
            conn.close();
            conn = null;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
