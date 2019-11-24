package com.zzx.executor.dao;


import com.zzx.executor.entity.ReOrderEntity;
import com.zzx.executor.entity.TradeEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TradeEntityMapper {

    @Insert("insert into t_trade (id, trade_date, trade_time,money, trade_type, remark, \n" +
            "      identity, bank, report, bank_account)\n" +
            "    values (#{id}, #{tradeDate}, #{tradeTime},#{money}, #{tradeType}, #{remark}, \n" +
            "      #{identity}, #{bank}, #{report},#{bankAccount})")
    int insert(TradeEntity record);

    @Select("SELECT t.id,t.trade_date,t.trade_time,t.money,t.trade_type,t.remark,t.identity,t.bank,t.report," +
            "t.bank_account FROM t_trade t WHERE t.trade_date = #{tradeDate} AND t.trade_time = #{tradeTime} " +
            "AND t.money = #{money} AND t.bank_account = #{bankAccount};")
    List<TradeEntity> checkExist(@Param("tradeDate") String tradeDate,
                                 @Param("tradeTime") String tradeTime,
                                 @Param("money") String money,
                                 @Param("bankAccount") String bankAccount);
    @Select("SELECT * FROM t_trade WHERE report=\"00\" AND trade_date=#{tradeDate} ORDER BY trade_time desc;")
    List<ReOrderEntity> rereport(@Param("tradeDate") String tradeDate);

    @Update("UPDATE t_trade SET report=TRUE WHERE remark=#{remark};")
    int upda(@Param("remark") String remark);

    @Select("SELECT count(id) from t_trade where bank_account=#{bankAccount}")
    int getCount(@Param("bankAccount") String bankAccount);

    @Select("SELECT remark from t_trade where bank_account=#{bankAccount}")
    String getRemark(@Param("bankAccount") String bankAccount);

    @Update("update t_trade set remark=#{remark} where  bank_account=#{bankAccount}")
    int updateRemark(@Param("remark") String remark,
                   @Param("bankAccount") String bankAccount);
}