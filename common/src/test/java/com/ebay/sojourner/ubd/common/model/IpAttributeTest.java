package com.ebay.sojourner.ubd.common.model;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class IpAttributeTest {

    private IpAttribute ipAttribute;
    private AgentIpAttribute agentIpAttribute;

    @BeforeEach
    public void setUp() {
        ipAttribute = new IpAttribute();
        agentIpAttribute = new AgentIpAttribute();
    }

    @Test
    public void test_feed() {
        agentIpAttribute.setTotalCnt(11);
        agentIpAttribute.setConsistent(true);
        agentIpAttribute.setValidPageCnt(2);
        agentIpAttribute.setTotalCntForSec1(5);
        agentIpAttribute.setIsAllAgentHoper(false);

        ipAttribute.feed(agentIpAttribute, 7);

        assertThat(ipAttribute.getTotalCnt()).isEqualTo(11);
        assertThat(ipAttribute.getTotalCntForSec1()).isEqualTo(5);
        assertThat(ipAttribute.getIsAllAgentHoper()).isFalse();
    }


    @Test
    public void test_feed_sessionCntGt3_and_sameSessionCnt() {
        agentIpAttribute.setTotalCnt(4);
        agentIpAttribute.setTotalCntForSec1(5);
        agentIpAttribute.setHomePageCnt(4);

        ipAttribute.feed(agentIpAttribute, 7);

        assertThat(ipAttribute.getTotalCnt()).isEqualTo(4);
        assertThat(ipAttribute.getTotalCntForSec1()).isEqualTo(5);
        assertThat(ipAttribute.getIsAllAgentHoper()).isTrue();
    }

    @Test
    public void test_feed_sessionCntGt5() {
        agentIpAttribute.setTotalCnt(6);
        agentIpAttribute.setTotalCntForSec1(5);
        agentIpAttribute.setFamilyViCnt(6);
        agentIpAttribute.setSigninCnt(6);

        ipAttribute.feed(agentIpAttribute, 7);

        assertThat(ipAttribute.getTotalCnt()).isEqualTo(6);
        assertThat(ipAttribute.getTotalCntForSec1()).isEqualTo(5);
        assertThat(ipAttribute.getIsAllAgentHoper()).isTrue();
    }

    @Test
    public void test_feed_sessionCntGt10_and_sameGuidCnt() {
        agentIpAttribute.setTotalCnt(12);
        agentIpAttribute.setTotalCntForSec1(5);
        agentIpAttribute.setNewGuidCnt(12);

        ipAttribute.feed(agentIpAttribute, 7);

        assertThat(ipAttribute.getTotalCnt()).isEqualTo(12);
        assertThat(ipAttribute.getTotalCntForSec1()).isEqualTo(5);
        assertThat(ipAttribute.getIsAllAgentHoper()).isTrue();
    }

    @Test
    public void test_feed_sessionCntGt20_and_sameMktCnt() {
        agentIpAttribute.setTotalCnt(21);
        agentIpAttribute.setTotalCntForSec1(5);
        agentIpAttribute.setMktgCnt(21);
        agentIpAttribute.setGuidSet(Sets.newHashSet("1"));

        ipAttribute.feed(agentIpAttribute, 7);

        assertThat(ipAttribute.getTotalCnt()).isEqualTo(21);
        assertThat(ipAttribute.getTotalCntForSec1()).isEqualTo(5);
        assertThat(ipAttribute.getIsAllAgentHoper()).isTrue();
    }

    @Test
    public void test_feed_sessionCntGt50_and_sameNoUidCnt() {
        agentIpAttribute.setTotalCnt(51);
        agentIpAttribute.setTotalCntForSec1(5);
        agentIpAttribute.setNoUidCnt(51);

        ipAttribute.feed(agentIpAttribute, 7);

        assertThat(ipAttribute.getTotalCnt()).isEqualTo(51);
        assertThat(ipAttribute.getTotalCntForSec1()).isEqualTo(5);
        assertThat(ipAttribute.getIsAllAgentHoper()).isTrue();
    }

    @Test
    public void test_feed_sessionCntGt100_and_sameNoUidCnt() {
        agentIpAttribute.setTotalCnt(101);
        agentIpAttribute.setTotalCntForSec1(5);
        agentIpAttribute.setFamilyViCnt(100);

        ipAttribute.feed(agentIpAttribute, 7);

        assertThat(ipAttribute.getTotalCnt()).isEqualTo(101);
        assertThat(ipAttribute.getTotalCntForSec1()).isEqualTo(5);
        assertThat(ipAttribute.getIsAllAgentHoper()).isTrue();
    }

    @Test
    public void test_feed_sessionCntGt200() {
        agentIpAttribute.setTotalCnt(201);
        agentIpAttribute.setTotalCntForSec1(5);
        agentIpAttribute.setCguidSet(Sets.newHashSet("1", "2", "3", "4"));

        ipAttribute.feed(agentIpAttribute, 7);

        assertThat(ipAttribute.getTotalCnt()).isEqualTo(201);
        assertThat(ipAttribute.getTotalCntForSec1()).isEqualTo(5);
        assertThat(ipAttribute.getIsAllAgentHoper()).isTrue();
    }
}