package com.greentag.app.data;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile CupReturnDao _cupReturnDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "e7ed1332ef0bf5d8936c1bf9513e765a", "9a19088f87941aedf35355292223a8f6") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `cup_returns` (`uid` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`uid`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e7ed1332ef0bf5d8936c1bf9513e765a')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `cup_returns`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsCupReturns = new HashMap<String, TableInfo.Column>(2);
        _columnsCupReturns.put("uid", new TableInfo.Column("uid", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCupReturns.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysCupReturns = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesCupReturns = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCupReturns = new TableInfo("cup_returns", _columnsCupReturns, _foreignKeysCupReturns, _indicesCupReturns);
        final TableInfo _existingCupReturns = TableInfo.read(connection, "cup_returns");
        if (!_infoCupReturns.equals(_existingCupReturns)) {
          return new RoomOpenDelegate.ValidationResult(false, "cup_returns(com.greentag.app.data.CupReturn).\n"
                  + " Expected:\n" + _infoCupReturns + "\n"
                  + " Found:\n" + _existingCupReturns);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "cup_returns");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "cup_returns");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CupReturnDao.class, CupReturnDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CupReturnDao cupReturnDao() {
    if (_cupReturnDao != null) {
      return _cupReturnDao;
    } else {
      synchronized(this) {
        if(_cupReturnDao == null) {
          _cupReturnDao = new CupReturnDao_Impl(this);
        }
        return _cupReturnDao;
      }
    }
  }
}
