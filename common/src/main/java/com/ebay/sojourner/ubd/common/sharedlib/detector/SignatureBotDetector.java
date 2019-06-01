package com.ebay.sojourner.ubd.common.sharedlib.detector;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.common.model.Signature;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.rule.IpBotRule;
import com.ebay.sojourner.ubd.common.rule.Rule;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SignatureBotDetector implements BotDetector<UbiSession> {

    private static SignatureBotDetector singnatureBotDetector;
    private Set<Rule> botRules = new LinkedHashSet<Rule>();
    private static QueryableStateClient client = null;
    private static JobID jobID = null;

    private SignatureBotDetector(QueryableStateClient queryableStateClient, JobID jobID) {
        this.client = queryableStateClient;
        this.jobID = jobID;
        initBotRules();
        for (Rule rule : botRules) {
            rule.init();
        }
    }

    public static SignatureBotDetector getInstance(QueryableStateClient queryableStateClient, JobID jobID) {
        if (singnatureBotDetector == null) {
            synchronized (SignatureBotDetector.class) {
                if (singnatureBotDetector == null) {
                    singnatureBotDetector = new SignatureBotDetector(queryableStateClient, jobID);
                }
            }
        }
        return singnatureBotDetector;
    }

    @Override
    public Set<Integer> getBotFlagList(UbiSession ubiSession) {
        Signature signature = null;
        Set<Integer> botflagSet = new HashSet<Integer>();
        if (ubiSession.getClientIp() != null) {
            CompletableFuture<ValueState<IpSignature>> completableFuture = queryState(ubiSession.getClientIp(), this.jobID, this.client);
            if (completableFuture != null) {

                try {
                    ValueState<IpSignature> valueState = completableFuture.get();
                    signature = valueState.value();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }
        if (signature != null) {
            for (Rule rule : botRules) {
                int botFlag = rule.getBotFlag(signature);
                if (botFlag != 0) {
                    botflagSet.add(botFlag);
                }
            }
        }

        return botflagSet;
    }

    @Override
    public void initBotRules() {
        botRules.add(new IpBotRule());
    }

    private CompletableFuture<ValueState<IpSignature>> queryState(String key, JobID jobId, QueryableStateClient client) {
        return client.getKvState(
                jobId,
                "bot7",
                key,
                Types.STRING,
                new ValueStateDescriptor<IpSignature>("", TypeInformation.of(new TypeHint<IpSignature>() {
                })));
    }


}
