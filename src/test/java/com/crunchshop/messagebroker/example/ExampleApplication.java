package com.crunchshop.messagebroker.example;

import com.crunchshop.messagebroker.example.dpn.DPNBroadCastConsumer;
import com.crunchshop.messagebroker.example.webui.WebUISendEmailConsumer;
import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.core.MessageBrokerManagerFactory;
import com.crunchshop.messagebroker.core.exception.EventPublishException;
import com.crunchshop.messagebroker.event.AssetShared;
import com.crunchshop.messagebroker.impl.redis.RedisMessageBrokerManager;
import redis.clients.jedis.JedisPool;

public class ExampleApplication {

    public static void main(String[] args) throws EventPublishException {

//        MessageBroker.init("dpn", "dev", "node1", new MessageBrokerManagerFactory(
//                new AWSMessageBrokerManager("?","?","us-east-1"))
//        );

        MessageBroker.init("dpn", "dev", "node1", new MessageBrokerManagerFactory(
                new RedisMessageBrokerManager(new JedisPool("localhost", 6379)))
        );

        DPNBroadCastConsumer dpnNode1 = new DPNBroadCastConsumer();
        WebUISendEmailConsumer webuiNode1 = new WebUISendEmailConsumer();

//        MessageBroker.getInstance().setUniqueInstanceName("node2");
        DPNBroadCastConsumer dpnNode2 = new DPNBroadCastConsumer();
        WebUISendEmailConsumer webuiNode2 = new WebUISendEmailConsumer();

//        MessageBroker.getInstance().setUniqueInstanceName("node3");
        DPNBroadCastConsumer dpnNode3 = new DPNBroadCastConsumer();
        WebUISendEmailConsumer webuiNode3 = new WebUISendEmailConsumer();

//        MessageBroker.getInstance().setUniqueInstanceName("node4");
        DPNBroadCastConsumer dpnNode4 = new DPNBroadCastConsumer();
        WebUISendEmailConsumer webuiNode4 = new WebUISendEmailConsumer();


        /**
         * All DPN nodes will receive the event since it uses a broadcast queue
         *
         * Only 1 WEBUI node will receive the event since it uses a deliver once queue
         */
        AssetShared event = new AssetShared(
                "assetId",
                "assetName",
                "assetTypeDisplayName",
                "rawAssetType",
                "shareTargetType",
                "shareTargetId",
                "shareRightType",
                "sharerId"
        );
        MessageBroker.getInstance().publishEvent(event);
    }
}
