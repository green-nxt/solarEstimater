package com.greennext.solarestimater.repository;

import com.greennext.solarestimater.model.Customer;
import com.greennext.solarestimater.model.DailyPowerRecord;
import com.greennext.solarestimater.model.Inverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyPowerRecordRepository extends JpaRepository<DailyPowerRecord, Long> {
    List<DailyPowerRecord> findByCustomerOrderByDateDesc(Customer customer);
    List<DailyPowerRecord> findByInverterOrderByDateDesc(Inverter inverter);
    List<DailyPowerRecord> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);
    Optional<DailyPowerRecord> findByInverterAndDate(Inverter inverter, LocalDate date);
    boolean existsByInverterAndDate(Inverter inverter, LocalDate date);
}
