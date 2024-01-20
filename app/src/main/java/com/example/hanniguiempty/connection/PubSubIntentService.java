package com.example.hanniguiempty.connection;
/**
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0.
 */


import android.app.IntentService;
import android.content.Intent;


import software.amazon.awssdk.crt.CRT;
import software.amazon.awssdk.crt.CrtResource;
import software.amazon.awssdk.crt.CrtRuntimeException;
import software.amazon.awssdk.crt.http.HttpProxyOptions;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class PubSubIntentService {

    // When run normally, we want to exit nicely even if something goes wrong
    static String ciPropValue = System.getProperty("aws.crt.ci");
    static boolean isCI = ciPropValue != null && Boolean.valueOf(ciPropValue);


    ///
    private short port = 8883;
    private String clientId = "phone";
    private String endpoint = "a3j5gn8c1p7i99-ats.iot.us-east-1.amazonaws.com";
    private String input_cert = "com/example/hanniguiempty/connection/phone.cert.pem";
    private String input_key = "com/example/hanniguiempty/connection/phone.private.key";

    private String topic = "/iot";
    private int count = 5;

    private String message = "27";


    public void mqtt_funtion() {
        MqttClientConnectionEvents callbacks = new MqttClientConnectionEvents() {
            @Override
            public void onConnectionInterrupted(int errorCode) {
                if (errorCode != 0) {
                    System.out.println("Connection interrupted: " + errorCode + ": " + CRT.awsErrorString(errorCode));
                }
            }

            @Override
            public void onConnectionResumed(boolean sessionPresent) {
                System.out.println("Connection resumed: " + (sessionPresent ? "existing session" : "clean session"));
            }
        };

        try {

            /**
             * Create the MQTT connection from the builder
             */
            AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(input_cert, input_key);

            builder.withConnectionEventCallbacks(callbacks)
                    .withClientId(clientId)
                    .withEndpoint(endpoint)
                    .withPort(port)
                    .withCleanSession(true)
                    .withProtocolOperationTimeoutMs(60000);

            MqttClientConnection connection = builder.build();
            builder.close();

            // Connect the MQTT client
            CompletableFuture<Boolean> connected = connection.connect();
            try {
                boolean sessionPresent = connected.get();
                System.out.println("Connected to " + (!sessionPresent ? "new" : "existing") + " session!");
            } catch (Exception ex) {
                throw new RuntimeException("Exception occurred during connect", ex);
            }

            // Subscribe to the topic
            //CountDownLatch countDownLatch = new CountDownLatch(count);
            CompletableFuture<Integer> subscribed = connection.subscribe(topic, QualityOfService.AT_LEAST_ONCE, (message) -> {
                String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                System.out.println("MESSAGE: " + payload);
                //countDownLatch.countDown();
            });
            subscribed.get();

            // Publish to the topic
            int count2 = 0;
            //while (count2++ < count) {
                CompletableFuture<Integer> published = connection.publish(new MqttMessage(topic, message.getBytes(), QualityOfService.AT_LEAST_ONCE, false));
                published.get();
                Thread.sleep(1000);
            //}
            //countDownLatch.await();

            // Disconnect
            CompletableFuture<Void> disconnected = connection.disconnect();
            disconnected.get();

            // Close the connection now that we are completely done with it.
            connection.close();

        } catch (CrtRuntimeException | InterruptedException | ExecutionException ex) {
            onApplicationFailure(ex);
        }

        CrtResource.waitForNoResources();

    }

    private static void onApplicationFailure(Throwable cause) {
        // Handle the failure as needed
    }

    // Additional methods as needed...
}

