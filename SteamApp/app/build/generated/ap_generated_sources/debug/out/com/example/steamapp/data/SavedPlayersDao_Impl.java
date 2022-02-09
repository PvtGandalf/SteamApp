package com.example.steamapp.data;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SavedPlayersDao_Impl implements SavedPlayersDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlayerSummary> __insertionAdapterOfPlayerSummary;

  private final EntityDeletionOrUpdateAdapter<PlayerSummary> __deletionAdapterOfPlayerSummary;

  public SavedPlayersDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlayerSummary = new EntityInsertionAdapter<PlayerSummary>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `savedPlayers` (`steamid`,`personaname`,`avatar`,`personastate`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PlayerSummary value) {
        stmt.bindLong(1, value.steamid);
        if (value.personaname == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.personaname);
        }
        if (value.avatar == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.avatar);
        }
        stmt.bindLong(4, value.personastate);
      }
    };
    this.__deletionAdapterOfPlayerSummary = new EntityDeletionOrUpdateAdapter<PlayerSummary>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `savedPlayers` WHERE `steamid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PlayerSummary value) {
        stmt.bindLong(1, value.steamid);
      }
    };
  }

  @Override
  public void insert(final PlayerSummary playerSummary) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPlayerSummary.insert(playerSummary);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final PlayerSummary playerSummary) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPlayerSummary.handle(playerSummary);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<PlayerSummary>> getAllSavedPlayers() {
    final String _sql = "SELECT * FROM savedPlayers";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"savedPlayers"}, false, new Callable<List<PlayerSummary>>() {
      @Override
      public List<PlayerSummary> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSteamid = CursorUtil.getColumnIndexOrThrow(_cursor, "steamid");
          final int _cursorIndexOfPersonaname = CursorUtil.getColumnIndexOrThrow(_cursor, "personaname");
          final int _cursorIndexOfAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "avatar");
          final int _cursorIndexOfPersonastate = CursorUtil.getColumnIndexOrThrow(_cursor, "personastate");
          final List<PlayerSummary> _result = new ArrayList<PlayerSummary>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final PlayerSummary _item;
            _item = new PlayerSummary();
            _item.steamid = _cursor.getInt(_cursorIndexOfSteamid);
            _item.personaname = _cursor.getString(_cursorIndexOfPersonaname);
            _item.avatar = _cursor.getString(_cursorIndexOfAvatar);
            _item.personastate = _cursor.getInt(_cursorIndexOfPersonastate);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<PlayerSummary> getSavedPlayersByID(final String steamid) {
    final String _sql = "SELECT * FROM savedPlayers WHERE steamid = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (steamid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, steamid);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"savedPlayers"}, false, new Callable<PlayerSummary>() {
      @Override
      public PlayerSummary call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSteamid = CursorUtil.getColumnIndexOrThrow(_cursor, "steamid");
          final int _cursorIndexOfPersonaname = CursorUtil.getColumnIndexOrThrow(_cursor, "personaname");
          final int _cursorIndexOfAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "avatar");
          final int _cursorIndexOfPersonastate = CursorUtil.getColumnIndexOrThrow(_cursor, "personastate");
          final PlayerSummary _result;
          if(_cursor.moveToFirst()) {
            _result = new PlayerSummary();
            _result.steamid = _cursor.getInt(_cursorIndexOfSteamid);
            _result.personaname = _cursor.getString(_cursorIndexOfPersonaname);
            _result.avatar = _cursor.getString(_cursorIndexOfAvatar);
            _result.personastate = _cursor.getInt(_cursorIndexOfPersonastate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
