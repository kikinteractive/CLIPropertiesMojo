CLIPropertiesMojo
==============

A simple mojo to add build properties that are populated by executing CLI commands.

SAMPLE USAGE
==============

To use you will have to host on your own nexus repository, but a sample configuration can include:

  <plugin>
  <groupId>com.kik.plugin</groupId>
  <artifactId>commandline-params</artifactId>
  <version>1.0-SNAPSHOT</version>
  <executions>
	<execution>
		<id>set-params</id>
		<goals>
			<goal>set-params</goal>
		</goals>
		<configuration>
		<myProperties>
		    <property>
			<name>build.buildtime</name>
			<value>date</value>
		    </property>
		    <property>
			<name>build.commithash</name>
			<value>git rev-parse --verify HEAD</value>
		    </property>
		</myProperties>
		</configuration>
	</execution>
	</executions>
  </plugin>
