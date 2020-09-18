package de.itscope.chartservice.model.repository;

import de.itscope.chartservice.model.storage.Segmentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SegmentationRepository extends JpaRepository<Segmentation, String> {
    List<Segmentation> findByDomain(String domain);

    List<Segmentation> findByDomainAndBreakdownType(String domain, String breakDownType);

    List<Segmentation> findByEventAndDomainAndDateIn(String event, String domain, ArrayList<String> dateRange);

    List<Segmentation> findByEventAndDomainAndBreakdownTypeAndDateIn(String event, String domain, String breakdownType, ArrayList<String> dateRange);

    Segmentation findBySerial(String serial);

    Segmentation findFirstByDomain(String domain);

    Segmentation findFirstByEventAndDomainAndBreakdownTypeOrderByDateAsc(String event, String Domain, String breakdownType);

    Segmentation findFirstByOrderByDateAsc();

    Segmentation findByEventAndDomainAndBreakdownTypeAndBreakdownValueAndDateAndValue(String event, String domain, String breakdownType, String breakdownValue, String date, int value);

    Segmentation findByEventAndDomainAndBreakdownTypeAndBreakdownValueAndDate(String event, String domain, String breakdownType, String breakdownValue, String date);

    boolean existsByEventAndDomainAndBreakdownTypeAndBreakdownValueAndDate(String event, String domain, String breakdownType, String breakdownValue, String date);

    boolean existsByEventAndDomainAndBreakdownTypeAndBreakdownValueAndDateAndValue(String event, String domain, String breakdownType, String breakdownValue, String date, int value);

}
