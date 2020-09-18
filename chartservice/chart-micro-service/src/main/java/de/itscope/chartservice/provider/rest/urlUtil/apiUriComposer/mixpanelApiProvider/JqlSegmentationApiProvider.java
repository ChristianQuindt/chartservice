package de.itscope.chartservice.provider.rest.urlUtil.apiUriComposer.mixpanelApiProvider;

import java.net.URI;
import java.util.ArrayList;

public class JqlSegmentationApiProvider implements MixpanelApiProvider {
    public static final Class<ArrayList> returnType = ArrayList.class;

    private final String event;
    private final String from_date;
    private final String to_date;
    private final String[] groupByProperties;

    public JqlSegmentationApiProvider(String event, String from_date, String to_date, String[] groupByProperties) {
        this.event = event;
        this.from_date = from_date;
        this.to_date = to_date;
        this.groupByProperties = groupByProperties;
    }

    public URI compose() {
        return compose(jqlUri, "script", buildJqlScript());
    }

    private String buildJqlScript() {
        String script = "";
        script = script + JqlScriptParts.MainMethodStart.getPart();
        script = script + event;
        script = script + JqlScriptParts.EventQueryStart.getPart();
        script = script + JqlScriptParts.EventQueryFromDate.getPart();
        script = script + from_date;
        script = script + JqlScriptParts.EventQueryToDate.getPart();
        script = script + to_date;
        script = script + JqlScriptParts.EventQueryEnd.getPart();
        script = script + JqlScriptParts.GroupByStartWithDailyBuckets.getPart();
        for (String property :
                groupByProperties) {
            if (property != null && !property.isEmpty()){
                script = script + JqlScriptParts.GroupByPropertySuffix.getPart();
                script = script + property;
                script = script + "\"";
            }
        }
        script = script + JqlScriptParts.GroupByEnd.getPart();
        script = script + JqlScriptParts.MainMethodEnd.getPart();
        script = script + JqlScriptParts.HelperFunctions.getPart();


        return script;
    }


    private enum JqlScriptParts {
        MainMethodStart("function main() {\n" +
                "  var event = \""),
        EventQueryStart("\";\n" +
                "  \n" +
                "  return Events({"),
        EventQueryFromDate("\n" +
                "    from_date: \""),
        EventQueryToDate("\",\n" +
                "      to_date: \""),
        EventQueryEnd("\",\n" +
                "    event_selectors: [{event: event}]\n" +
                "  })"),
        GroupByStartWithDailyBuckets(".groupBy([mixpanel.numeric_bucket('time', mixpanel.daily_time_buckets)"),
        GroupByPropertySuffix(", \"properties."),
        GroupByEnd("], mixpanel.reducer.count())"),
        MainMethodEnd("\n" +
                "    .sortDesc('key.0');\n" +
                "}"),
        HelperFunctions("\n" +
                "//Helper functions\n" +
                "function formatDate(date) {\n" +
                "  return date.getFullYear()+\"-\"+(\"0\" + parseInt(date.getMonth()+1)).slice(-2)+\"-\"+date.getDate();\n" +
                "}\n" +
                "function getThreeMonthsPast() {\n" +
                "\tvar fromDate = new Date();\n" +
                "\tfromDate.setMonth(fromDate.getMonth() - 3);\n" +
                "\treturn formatDate(fromDate);\n" +
                "}\n" +
                "function getDay(time) {\n" +
                "  return (new Date(time)).toISOString().split('T')[0];\n" +
                "}");

        private final String part;

        JqlScriptParts(String part) {
            this.part = part;
        }

        public String getPart() {
            return part;
        }
    }
}
