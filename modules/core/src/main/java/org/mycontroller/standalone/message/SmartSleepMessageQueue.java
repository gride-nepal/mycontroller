/*
 * Copyright 2015-2017 Jeeva Kandasamy (jkandasa@gmail.com)
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mycontroller.standalone.message;

import java.util.ArrayList;

import org.mapdb.HTreeMap;
import org.mycontroller.standalone.AppProperties;
import org.mycontroller.standalone.MapDbFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
@Slf4j
public class SmartSleepMessageQueue {
    public static final String AVAILABLE_QUEUE_LIST = "smart_sleep_msg_queue";
    HTreeMap<String, ArrayList<McMessage>> messagesQueue;

    //Do not load until some calls getInstance
    private static class SmartSleepMessageQueueHelper {
        private static final SmartSleepMessageQueue INSTANCE = new SmartSleepMessageQueue();
    }

    public static SmartSleepMessageQueue getInstance() {
        return SmartSleepMessageQueueHelper.INSTANCE;
    }

    private SmartSleepMessageQueue() {
        messagesQueue = MapDbFactory.getDbStore().getHashMap(AVAILABLE_QUEUE_LIST);
        if (AppProperties.getInstance().getClearMessagesQueueOnStart()) {
            messagesQueue.clear();
            _logger.debug("Cleared all smart sleep messages...");
        }
    }

    private String getQueueName(Integer gatewayId, String nodeEui) {
        return gatewayId + "_" + nodeEui;
    }

    private ArrayList<McMessage> getQueueInternal(String queueName) {
        if (!messagesQueue.containsKey(queueName)) {
            messagesQueue.put(queueName, new ArrayList<McMessage>());
        }
        return messagesQueue.get(queueName);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<McMessage> getQueue(String queueName) {
        if (!messagesQueue.containsKey(queueName)) {
            return new ArrayList<McMessage>();
        }
        return (ArrayList<McMessage>) messagesQueue.get(queueName).clone();
    }

    public ArrayList<McMessage> getQueue(Integer gatewayId, String nodeEui) {
        return getQueue(getQueueName(gatewayId, nodeEui));
    }

    public ArrayList<String> getQueueNames() {
        ArrayList<String> names = new ArrayList<String>();
        names.addAll(messagesQueue.keySet());
        return names;
    }

    public synchronized void removeQueue(String queueName) {
        if (messagesQueue.containsKey(queueName)) {
            messagesQueue.remove(queueName);
            _logger.debug("Queue removed:[{}]", queueName);
        }
    }

    public synchronized void removeQueue(Integer gatewayId, String nodeEui) {
        removeQueue(getQueueName(gatewayId, nodeEui));
    }

    public synchronized void removeMessages(String queueName, String sensorId) {
        ArrayList<McMessage> queue = getQueueInternal(queueName);
        for (int index = 0; index < queue.size(); index++) {
            if (queue.get(index).getSensorId().equals(sensorId)) {
                queue.remove(index);
            }
        }
    }

    public synchronized void removeMessages(Integer gatewayId, String nodeEui, int index) {
        removeMessages(getQueueName(gatewayId, nodeEui), index);
    }

    public synchronized void removeMessages(String queueName, int index) {
        ArrayList<McMessage> queue = getQueueInternal(queueName);
        if (queue.size() > index) {
            queue.remove(index);
        }
    }

    public synchronized void removeMessages(Integer gatewayId, String nodeEui, String sensorId) {
        removeMessages(getQueueName(gatewayId, nodeEui), sensorId);
    }

    public void clearAll() {
        messagesQueue.clear();
        _logger.debug("Cleared all queues..");
    }

    public synchronized void putMessage(McMessage mcMessage) {
        String queueName = getQueueName(mcMessage.getGatewayId(), mcMessage.getNodeEui());
        ArrayList<McMessage> queue = getQueueInternal(queueName);
        queue.add(mcMessage);
        _logger.info("Added new {}, on queue [{}], size:{}", mcMessage, queueName, queue.size());
    }

    public synchronized McMessage getMessage(Integer gatewayId, String nodeEui) {
        String queueName = getQueueName(gatewayId, nodeEui);
        McMessage mcMessage = null;
        ArrayList<McMessage> queue = getQueueInternal(queueName);
        if (!queue.isEmpty()) {
            mcMessage = queue.remove(0);
        }
        _logger.debug("Retriving {}, on queue [{}], size:{}", mcMessage, queueName, queue.size());
        return mcMessage;
    }
}
