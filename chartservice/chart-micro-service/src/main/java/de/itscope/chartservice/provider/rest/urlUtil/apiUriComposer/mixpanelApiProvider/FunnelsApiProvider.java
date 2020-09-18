package de.itscope.chartservice.provider.rest.urlUtil.apiUriComposer.mixpanelApiProvider;

import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi.MPFunnelsApi;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class FunnelsApiProvider implements MixpanelApiProvider {
    public static final Class<MPFunnelsApi> returnType = MPFunnelsApi.class;

    private int funnel_id;

    private String from_date;

    private String to_date;

    private String on;

    public FunnelsApiProvider(int funnelID, String from_date, String to_date, String on) {
        this.funnel_id = funnelID;
        this.from_date = from_date;
        this.to_date = to_date;

        if (!on.isEmpty()){
            this.on =  "properties[\"" + on + "\"]";
        }

    }

    public URI compose() {
            if (on == null) {
                return compose(funnelsUri, "funnel_id", String.valueOf(funnel_id), "from_date", from_date, "to_date", to_date, "interval", "100");
            } else {
                return compose(funnelsUri, "funnel_id", String.valueOf(funnel_id), "from_date", from_date, "to_date", to_date, "on", on, "interval", "100");
            }
    }

    public Map<String, Integer> prepareDataStructure(MPFunnelsApi data) {

        Map<String, Integer> collect = data.getData().getAdditionalProperties().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> (int)s.getValue()));

        return collect;
    }

}
