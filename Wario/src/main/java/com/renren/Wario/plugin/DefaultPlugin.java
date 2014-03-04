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
package com.renren.Wario.plugin;

public class DefaultPlugin extends IPlugin {

	private final String number = "13888888888";
	private final String address = "test@test.com";
	private final int maxOutStanding = 5;
	
	@Override
	public void run() {
		if (!client.isAvailable()) {
			msgSender.sendMessage(number,
					"Client " + client.getConnectionString() + " is down!");
			mailSender.sendMail(address,
					"Client " + client.getConnectionString() + " is down!");
		}
		
		if (!client.state.ruok()) {
			msgSender.sendMessage(number,
					"Something wrong with client " + client.getConnectionString() + "!");
			mailSender.sendMail(address,
					"Something wrong with client " + client.getConnectionString() + "!");
		}

		String oldMode = client.state.getMode();
		client.state.update();
		String mode = client.state.getMode();

		if (!mode.equals(oldMode)) {
			msgSender.sendMessage(number,
					"Client " + client.getConnectionString()
							+ " has changed mode to " + mode);
			mailSender.sendMail(address,
					"Client " + client.getConnectionString()
							+ " has changed mode to " + mode);
		}
		
		if(client.state.getOutStanding() > maxOutStanding) {
			msgSender.sendMessage(number,
					"Client " + client.getConnectionString()
							+ " exceed max outstanding.");
			mailSender.sendMail(address,
					"Client " + client.getConnectionString()
							+ " exceed max outstanding.");
		}
		
		if(!client.canBeUsed()) {
			msgSender.sendMessage(number,
					"Client " + client.getConnectionString()
							+ " can not be used.");
			mailSender.sendMail(address,
					"Client " + client.getConnectionString()
							+ " can not be used.");
		}
	}
}
