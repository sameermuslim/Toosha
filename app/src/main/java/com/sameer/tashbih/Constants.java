package com.sameer.tashbih;

public  class Constants {
    public static final String IS_ONCE_GUID;
    public static final int R_GROUP_FJIR=1;
    public static final int R_GROUP_ZOHOR=2;
    public static final int R_GROUP_ASIR=3;
    public static final int R_GROUP_MAQHRIB=4;
    public static final int R_GROUP_ESHA=5;

    // STATE OF PRAYERS
    public static final int FAUT=0;
    public static final int JAAMAT=1;
    public static final int MONFARED=2;
    public static final int QAZA=3;
    public static final int RC_BACKUP_DIRECTORY_CHOOSER = 1;
    public static final int RC_RESTORE_DB =2 ;

    static {

        String pkg = "com.sameer.tashbih";
        IS_ONCE_GUID= pkg +"is-once-guid-";
    }



}
