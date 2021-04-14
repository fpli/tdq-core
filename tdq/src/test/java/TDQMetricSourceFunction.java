import com.ebay.tdq.config.ExpressionConfig;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.rules.TdqMetric;
import java.util.Random;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author juntzhang
 */
public class TDQMetricSourceFunction implements SourceFunction<TdqMetric> {
    @Override
    public void run(SourceContext<TdqMetric> ctx) {
        while (true) {
            TdqMetric metric = getRateMetric(new String[]{"a", "b"}[getInt() % 2]);
            System.out.println(Thread.currentThread() + ">" + metric);
            ctx.collect(metric);
            try {
                Thread.sleep(500 * (getInt() % 5 + 1));
            } catch (InterruptedException ignore) {
            }
        }
    }

    // global_mandatory_tag_item_rate
    public TdqMetric getRateMetric(String name) {
        TdqMetric m = new TdqMetric(name, System.currentTimeMillis())
                .putTag("page_family", getPageFamily())
                .putTag("site_id", getSiteId())
                .putExpr("itm_missing_cnt", getVal())
                .putExpr("itm_valid_cnt", getVal())
                .genUID();
        m.setProfilerConfig(
                ProfilerConfig.builder()
                        .expression(ExpressionConfig.expr("itm_valid_cnt / itm_missing_cnt"))
                        .build()
        );
        return m;
    }

    public Double getVal() {
//        return 1d;
        return Math.abs(new Random().nextInt()) % 10d;
    }

    public int getInt() {
        return Math.abs(new Random().nextInt() % 10);
    }

    public String getPageFamily() {
        return new String[]{"WTCH", "ASQ"}[getInt() % 2];
    }

    public int getSiteId() {
        return new int[]{1, 2, 3, 4}[getInt() % 2];
    }

    @Override
    public void cancel() {

    }
}
