A sample custom extension library (jar) that is designed to be loaded dynamically at runtime as a extension to the opentelemetry java auto instrumentation (using the -Dotel.javaagent.extensions jvm system parameter). This enabled custom instrumentation of java classes/methods to achieve the same use cases supported by manual instrumentation but without modifying application code. 
Example use cases include adding spans, adding span tags and events etc.

## Build
sample-custom-extension is a java library built using gradle. You can build a jar file with the following commands

```
git clone https://github.com/psomareddy/sample-custom-extension.git
./gradlew clean build
```
The custom extension jar should be available in the **build/libs/sample-custom-extension.jar** location.

## Test application
Install the [simple-java-app](https://github.com/psomareddy/simple-java-app) application. This extension will add a custom span tag to the spans produced by the test application without modifying the test application code.

## Instrument the app to load the custom auto instrumentation extension

Set the -Dotel.javaagent.extensions property to the path of the custom extension jar- sample-custom-extension.jar . For the simple-java-app, the start application command should now look like this.

```
java  -javaagent:/opt/otel/splunk-otel-javaagent.jar  -Dotel.instrumentation.methods.include=com.simple.app.App[getGreeting] -Dotel.javaagent.extensions=/opt/otel/extensions/sample-custom-extension.jar -jar build/libs/*.jar 
```

## Usage
As before, once started, the application should run for about 3 minutes invoking the App.getGreeting() every two seconds method which prints "Hello World! {iteration}". The instrumentation will pick up on these invocations and create a span for each exectution.

In addition, this custom extension jar will add a custom span tag of custom-tag-name=custom-tag-value. Of course, you should edit the [SampleInstrumentation.java file](https://github.com/psomareddy/sample-custom-extension/blob/main/src/main/java/com/splunk/field/extension/SampleInstrumentation.java#L41) to modify the line to add whatever custom attributes makes sense to your application in the real world.

To apply this instrumentation to a different application target class.method, change the class name [here](https://github.com/psomareddy/sample-custom-extension/blob/dcc44eda73b49e0a2c4898b5979e21e7b7c6d4a3/src/main/java/com/splunk/field/extension/SampleInstrumentation.java#L24) and method name [here](https://github.com/psomareddy/sample-custom-extension/blob/dcc44eda73b49e0a2c4898b5979e21e7b7c6d4a3/src/main/java/com/splunk/field/extension/SampleInstrumentation.java#L29), then rebuild the jar. 

