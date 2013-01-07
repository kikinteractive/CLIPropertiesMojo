package com.kik.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Properties;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Requirement;

import com.kik.plugin.launch.SysCommandExecutor;

/**
 * Goal which will set build properties by capturing the output of 
 * running the specified commands. 
 * 
 * example:
 * <plugins>
 * .. 
 *	 <plugin>
 *	  <groupId>com.kik.plugin</groupId>
 *  	 <artifactId>commandline-params</artifactId>
 * 		 <version>1.0-SNAPSHOT</version>
 * 		 <executions>
 *			<execution>
 *				<id>set-params</id>
 *				<goals>
 *					<goal>set-params</goal>
 *				</goals>
 *				<configuration>
 *				<myProperties>
 *			         <property>
 *				        <name>build.buildtime</name>
 *						<value>date</value>
 *					</property>
 *					<property>
 *						<name>build.commithash</name>
 *						<value>git rev-parse --verify HEAD</value>
 *					</property>
 *					<property>
 *						<name>build.machinename</name>
 *						<value>hostname</value>
 *					</property>
 *				</myProperties>
 *				</configuration>
 *				</execution>
 *				</executions>
 *					</plugin>
 *					...
 *				</plugins>
 *
 * @goal set-params
 * 
 * @phase process-sources
 */
public class CommandLineMojo extends AbstractMojo {

	/**
	 * The project that we would like to set
	 * 
	 * @parameter default-value="${project}"
	 * @required
	 */
	@Requirement
	private MavenProject project;

	/**
	 * @parameter
	 * @required
	 * @readonly
	 */
	@Requirement
	private Properties myProperties;
	
	public void execute() throws MojoExecutionException {
		try {
			final Set<Object> keySet = myProperties.keySet();
			Properties properties = project.getProperties();
			for (Object key : keySet) {
				final String currentKey = String.valueOf(key);
				final String executionString = String.valueOf(myProperties
						.get(key));
				SysCommandExecutor executor = new SysCommandExecutor();

				executor.runCommand(executionString);
				String output = executor.getCommandOutput();

				properties.setProperty(currentKey, output);
				getLog().debug("setting: " + currentKey + " value: " + output);
				String errorOutput = executor.getCommandError();
				if (errorOutput != null && errorOutput.length() > 0) {
					getLog().error("error: " + errorOutput);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
