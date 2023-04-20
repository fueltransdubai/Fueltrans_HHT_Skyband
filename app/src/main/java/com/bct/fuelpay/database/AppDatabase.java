package com.bct.fuelpay.database;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.bct.fuelpay.database.dao.CartDao;
import com.bct.fuelpay.database.data.CartOptions;




@Database(entities = {CartOptions.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "fueltrans_db";

    public abstract CartDao getOptionsDao();
//
//    public abstract CategoryDao getCategoryDao();

}
