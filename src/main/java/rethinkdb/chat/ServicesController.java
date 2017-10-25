package rethinkdb.chat;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rethinkdb.RethinkDB;

import rethinkdb.db.RethinkDBConnectionFactory;

@RestController
@RequestMapping("/users")
public class ServicesController {

    protected final Logger log = LoggerFactory.getLogger(ServicesController.class);
    private static final RethinkDB r = RethinkDB.r;

    @Autowired
    private RethinkDBConnectionFactory connectionFactory;

    @RequestMapping(method = RequestMethod.POST)
    public User postMessage(@RequestBody User user) {
    	
//    	user.setTime(OffsetDateTime.now());
        HashMap run = r.db("chat").table("users").insert(user)
                .run(connectionFactory.createConnection());

        log.info("Insert {}", run);
        return user;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getMessages() {

        List<User> messages = r.db("chat").table("users")
                .orderBy().optArg("index", r.desc("id"))
                .limit(20)
                .orderBy("id")
                .run(connectionFactory.createConnection(), User.class);

        return messages;
    }
}
