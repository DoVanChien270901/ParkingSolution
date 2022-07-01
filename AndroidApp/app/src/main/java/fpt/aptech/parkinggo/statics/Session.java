package fpt.aptech.parkinggo.statics;

public class Session {
    static Object Session = null;

    public static Object getSession() {
        return Session;
    }

    public static void setSession(Object session) {
        Session = session;
    }
}
