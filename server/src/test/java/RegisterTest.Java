//import dataaccess.createUser;
//import model.UserData;
//
//import java.util.Objects;
//
//public class RegisterTest{
//
//    public boolean registerUserTest(createUser newestUser){
//        int count = 0;
//        for (UserData user: createUser.listofUsers){
//            if (Objects.equals(user.username(), newestUser.username)){
//                count ++; //user has been registered
//            }
//            if (count == 2){
//                return false; // user has been registered twice  whcih is not ok.
//            }
//
//
//        }
//        if (count == 0){
//            return false; // user was never registered
//        }
//        // add logic for error 400 and 500?
//        return true;
//    }
//
//
//}