package de.itscope.chartservice.model.repository;

import de.itscope.chartservice.model.storage.Retention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface RetentionRepository extends JpaRepository<Retention, String> {
    boolean existsByBornEventAndEventAndDomainAndDate(String bornEvent, String event, String domain, String date);
    boolean existsByBornEventAndEventAndDomainAndDateAndFirstAndCountsIn(String bornEvent, String event, String domain, String date, int first, List<Integer> counts);

    Retention findByBornEventAndEventAndDomainAndDate(String bornEvent, String event, String domain, String date);
    Retention findFirstByBornEventAndEventAndDomainOrderByDateAsc(String bornEvent, String event, String domain);
    List<Retention> findByBornEventAndEventAndDomainAndDateIn(String bornEvent, String event, String domain, ArrayList<String> dateRange);
}
