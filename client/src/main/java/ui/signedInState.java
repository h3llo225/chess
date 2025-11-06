package ui;

public class signedInState {
    private static boolean signedIn = false;

    public static boolean getSignedIn(){
        return signedIn;
    }

    public static void editSignedIn(boolean val){
        signedIn = val;
    }
}
