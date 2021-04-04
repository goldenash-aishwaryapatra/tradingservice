package com.deutsche.tradestore.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.deutsche.tradestore.entity.Trade;

/**
 * Repository class for Trade entity
 * 
 * @author aishwarya patra
 *
 */
public interface TradeRepository extends JpaRepository<Trade, String> {

	/**
	 * Query to find list of trades by tradeId
	 * 
	 * @param tradeId
	 * @return
	 */
	public List<Trade> findByTradeStoreIdTradeId(String tradeId);

	/**
	 * Query to get the highest version for trade id
	 * 
	 * @param tradeId
	 * @return
	 */
	@Query(value = "select max(version) from trade where tradeId=?1", nativeQuery = true)
	public int getMaxVersionForTradeId(String tradeId);

	/**
	 * Query to get list of trades which are not expired and maturity date is before
	 * current date
	 * 
	 * @param todayDate
	 * @return
	 */
	@Query(value = "select * from trade where expired=false and maturitydate< ?1", nativeQuery = true)
	public List<Trade> getTradesbyNotExpiredAndMaturityDateLessThanToday(Date todayDate);
}
