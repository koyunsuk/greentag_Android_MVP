package com.greentag.app.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/greentag/app/data/CupReturnDao;", "", "deleteAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "", "Lcom/greentag/app/data/CupReturn;", "getCount", "", "insert", "cupReturn", "(Lcom/greentag/app/data/CupReturn;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface CupReturnDao {
    
    @androidx.room.Insert(onConflict = 3)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.greentag.app.data.CupReturn cupReturn, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM cup_returns")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.greentag.app.data.CupReturn>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM cup_returns")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "DELETE FROM cup_returns")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}