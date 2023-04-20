package com.bct.fuelpay.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.bct.fuelpay.database.data.CartOptions;

import java.util.List;

@Dao
public interface CartDao {


    @Insert
    void insertAll(CartOptions... options);

    @Update
    void updateAll(CartOptions... options);

    @Query("DELETE FROM CartOptions")
    void deleteAll();

    @Query("SELECT * FROM CartOptions")
    List<CartOptions> getAll();

}
