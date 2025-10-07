import dataaccess.createUser;
import model.UserData;

public class RegisterTest{


    public boolean registerUserTest(createUser newestUser){
        int count = 0;
        for (UserData user: createUser.listofUsers){
            if (user.username() == newestUser.username){
                count ++; //user has been registered
            }
            if (count == 2){
                return false; // user has been registered twice  whcih is not ok.
            }


        }
        if (count == 0){
            return false; // user was never registered
        }
        return false;
    }

}