import com.ebay.sojourner.common.model.ClientData;
import com.ebay.sojourner.common.model.RawEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author juntzhang
 */
public class TDQRawEventSourceFunction implements SourceFunction<RawEvent> {
    public static Map<String, Double> aggr = new HashMap<>();

    public synchronized static void compute(Map<String, Double> aggr, RawEvent rawEvent) {
    }

    @Override
    public void run(SourceContext<RawEvent> ctx) {
        while (true) {
            String   siteId        = String.valueOf(getSiteId());
            String   item          = getItm();
            String   tDuration     = String.valueOf(getInt() % 100);
            String   pageId        = new String[]{"711", "1702898", "1677718"}[getInt() % 2];
            String   contentLength = String.valueOf(getInt() % 100);
            RawEvent rawEvent      = new RawEvent();
            rawEvent.setClientData(new ClientData());
            rawEvent.getClientData().setContentLength(contentLength);
            rawEvent.setEventTimestamp(System.currentTimeMillis());
            rawEvent.setSojA(new HashMap<>());
            rawEvent.setSojK(new HashMap<>());
            rawEvent.setSojC(new HashMap<>());
            rawEvent.getSojA().put("p", pageId);
            rawEvent.getSojA().put("t", siteId);
            rawEvent.getSojA().put("TDuration", tDuration);
            rawEvent.getSojA().put("itm", item);
            compute(aggr, rawEvent);
//            System.out.println(Thread.currentThread() + ">{" +
//                    "page_id=" + pageId +
//                    ",contentLength=" + contentLength +
//                    ",site_id=" + siteId +
//                    ",itm=" + item +
//                    ",TDuration=" + tDuration +
//                    ",eventTime=" + FastDateFormat.getInstance("yyy-MM-dd HH:mm:ss").format(rawEvent
//                    .getEventTimestamp()) + "}");
            ctx.collect(rawEvent);
            try {
                Thread.sleep(500 * (getInt() % 5 + 1));
            } catch (InterruptedException ignore) {
            }
        }
    }

    public static int getInt() {
        return Math.abs(new Random().nextInt());
    }

    public static String getItm() {
        return new String[]{"123", "1abc", "", null}[getInt() % 4];
    }

    public static int getSiteId() {
        return new int[]{1, 2, 3, 4}[getInt() % 1];
    }

    @Override
    public void cancel() {
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        String   siteId        = String.valueOf(getSiteId());
        String   item          = getItm();
        String   tDuration     = String.valueOf(getInt() % 100);
        String   pageId        = new String[]{"711", "1702898", "1677718"}[getInt() % 3];
        String   contentLength = String.valueOf(getInt() % 100);
        RawEvent rawEvent      = new RawEvent();
        rawEvent.setClientData(new ClientData());
        rawEvent.getClientData().setContentLength(contentLength);
        rawEvent.setEventTimestamp(System.currentTimeMillis());
        rawEvent.setSojA(new HashMap<>());
        rawEvent.setSojK(new HashMap<>());
        rawEvent.setSojC(new HashMap<>());
        rawEvent.getSojA().put("p", pageId);
        rawEvent.getSojA().put("t", siteId);
        rawEvent.getSojA().put("TDuration", tDuration);
        rawEvent.getSojA().put("itm", item);

        Object o = PropertyUtils.getProperty(rawEvent, "clientData.CONTENTLENGTH");
        System.out.println(o);
    }
}
