package users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {

    private final Set<String> usersSet;
    private final Set<String> uBoatUsers;
    private final Set<String> alliesUsers;
    private final Set<String> agentsUsers;

    public UserManager() {
        usersSet = new HashSet<>();
        uBoatUsers = new HashSet<>();
        alliesUsers = new HashSet<>();
        agentsUsers = new HashSet<>();
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
    }
    public synchronized void addUBoatUser(String username) {
        uBoatUsers.add(username);
    }
    public synchronized void addAlliesUser(String username) {
        alliesUsers.add(username);
    }
    public synchronized void addAgentUser(String username) {
        agentsUsers.add(username);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }
    public synchronized void removeUserAgent(String username) {
        agentsUsers.remove(username);
    }
    public synchronized void removeUserAllies(String username) {
        alliesUsers.remove(username);
    }
    public synchronized void removeUserUBoat(String username) {
        uBoatUsers.remove(username);
    }

    public synchronized Set<String> getUsersAgent() {
        return Collections.unmodifiableSet(agentsUsers);
    }
    public synchronized Set<String> getUsersUBoat() {
        return Collections.unmodifiableSet(uBoatUsers);
    }
    public synchronized Set<String> getUsersAllies() {
        return Collections.unmodifiableSet(alliesUsers);
    }
    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }
}
