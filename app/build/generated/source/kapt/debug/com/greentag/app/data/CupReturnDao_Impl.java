package com.greentag.app.data;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class CupReturnDao_Impl implements CupReturnDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<CupReturn> __insertAdapterOfCupReturn;

  public CupReturnDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfCupReturn = new EntityInsertAdapter<CupReturn>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `cup_returns` (`uid`,`timestamp`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final CupReturn entity) {
        if (entity.getUid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getUid());
        }
        statement.bindLong(2, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insert(final CupReturn cupReturn, final Continuation<? super Unit> $completion) {
    if (cupReturn == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfCupReturn.insert(_connection, cupReturn);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<CupReturn>> $completion) {
    final String _sql = "SELECT * FROM cup_returns";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfUid = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "uid");
        final int _columnIndexOfTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "timestamp");
        final List<CupReturn> _result = new ArrayList<CupReturn>();
        while (_stmt.step()) {
          final CupReturn _item;
          final String _tmpUid;
          if (_stmt.isNull(_columnIndexOfUid)) {
            _tmpUid = null;
          } else {
            _tmpUid = _stmt.getText(_columnIndexOfUid);
          }
          final long _tmpTimestamp;
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp);
          _item = new CupReturn(_tmpUid,_tmpTimestamp);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM cup_returns";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM cup_returns";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
