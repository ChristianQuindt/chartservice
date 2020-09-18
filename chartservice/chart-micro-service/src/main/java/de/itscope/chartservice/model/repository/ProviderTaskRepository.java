package de.itscope.chartservice.model.repository;

import de.itscope.chartservice.model.storage.ProviderTask;
import de.itscope.chartservice.util.funnelRangeUtils.FunnelRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderTaskRepository extends JpaRepository<ProviderTask, String> {
    ProviderTask findByTypeAndEventAndSpecifierAndBreakdown(ProviderTask.ProviderType type, String event, String specifier, String breakdown);

    ProviderTask findByTypeAndFunnelIDAndBreakdownAndFunnelRange(ProviderTask.ProviderType type, int funnelID, String breakdown, String funnelRange);

    ProviderTask findByTypeAndBornEventAndEventAndSpecifierAndIntervalCount(ProviderTask.ProviderType type, String bornEvent, String event, String specifier, int intervalCount);
}
