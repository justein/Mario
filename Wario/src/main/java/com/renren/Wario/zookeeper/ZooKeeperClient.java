/**
 *    Copyright 2014 Renren.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.renren.Wario.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperClient implements Watcher {

	private static Logger logger = LogManager.getLogger(ZooKeeperClient.class
			.getName());

	private ZooKeeper zk = null;
	private final String connectionString;
	private final int sessionTimeout;
	public ZooKeeperState state = null;

	private volatile boolean isAvailable;
	private CountDownLatch countDownLatch = null;

	private static final String ZK_PATH = "/god_damn_zookeeper";
	
	public ZooKeeperClient(String connectionString, int sessionTimeout) {
		this.connectionString = connectionString;
		this.sessionTimeout = sessionTimeout;
		this.state = new ZooKeeperState(connectionString);
	}

	public void createConnection() {
		releaseConnection();
		countDownLatch = new CountDownLatch(1);

		try {
			zk = new ZooKeeper(connectionString, sessionTimeout, this);
			countDownLatch.await();
		} catch (IOException e) {
			logger.error("Create connection failed! IOException occured!\n"
					+ e.toString());
		} catch (InterruptedException e) {
			logger.error("InterruptedException occured!\n" + e.toString());
		}
	}

	public void releaseConnection() {
		isAvailable = false;
		if (zk != null) {
			try {
				zk.close();
			} catch (InterruptedException e) {
				logger.error("InterruptedException occured!\n" + e.toString());
			}
		}
	}

	@Override
	public void process(WatchedEvent event) {
		logger.info("SessionWatcher: " + connectionString + "|"
				+ event.getType() + "|" + event.getState());

		if (event.getType() == EventType.None) {
			if (event.getState().equals(KeeperState.SyncConnected)) {
				isAvailable = true;
				countDownLatch.countDown();
			} else if (event.getState().equals(KeeperState.Expired)
					|| event.getState().equals(KeeperState.Disconnected)) {
				isAvailable = false;
				createConnection();
			}
		}
	}
	
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * @return the connectionString
	 */
	public String getConnectionString() {
		return connectionString;
	}

	/**
	 * @return the sessionTimeout
	 */
	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public boolean canBeUsed() {
		boolean res = true;
		try {
			if(exits(ZK_PATH)) {
				deleteNode(ZK_PATH);
			}
			createPath(ZK_PATH, "I am the initial data");
			readData(ZK_PATH);
			writeData(ZK_PATH, "I am the updated data");
			readData(ZK_PATH);
			deleteNode(ZK_PATH);
		} catch (KeeperException e) {
			logger.error("Client " + connectionString + " can not be used!\n" + e.toString());
			res = false;
		} catch (InterruptedException e) {
			logger.error("Client " + connectionString + " can not be used!\n" + e.toString());
			res = false;
		}
		return res;
	}
	
	private boolean exits(String path) throws KeeperException, InterruptedException {
		return zk.exists(ZK_PATH, false) != null;
	}
	
	private void createPath(String path, String data) throws KeeperException, InterruptedException {
		zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}

	private String readData(String path) throws KeeperException, InterruptedException {
		return new String(zk.getData(path, false, null));
	}

	private void writeData(String path, String data) throws KeeperException, InterruptedException {
		zk.setData(path, data.getBytes(), -1);
	}

	private void deleteNode(String path) throws InterruptedException, KeeperException {
		zk.delete(path, -1);
	}

}
