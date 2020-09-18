package de.itscope.chartservice.model.repository;

import de.itscope.chartservice.model.storage.Funnels;
import de.itscope.chartservice.util.funnelRangeUtils.FunnelRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunnelsRepository extends JpaRepository<Funnels, String> {
    boolean existsByFunnelIDAndDomainAndStepLabelAndFunnelRange(int funnelID, String domain, String steplabel, String range);

    boolean existsByFunnelIDAndDomainAndStepLabelAndFunnelRangeAndCount(int funnelID, String domain, String stepLabel, String funnelRange, int count);

    Funnels findByFunnelIDAndDomainAndStepLabelAndFunnelRange(int funnelID, String domain, String stepLabel, String funnelRange);

    List<Funnels> findByFunnelIDAndDomainAndFunnelRange(int funnelsID, String domain, String dateRange);

}
