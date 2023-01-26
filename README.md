An extension to OpenTelemetry Java agent that delivers additional instrumentation through byte code transformation. This project artifact (jar file) location is passed to OpenTelemetry Java agent using the -Dotel.javaagent.extensions JVM parameter, when then loads it similar to Java auto instrumentation. This is an alternative to manual instrumentation that requires code changes to instrumented application.

Example use cases include adding spans, additional span tags and events etc.

## Build
sample-custom-extension is a Java library built using Gradle. You can build it by running the following commands.

```
git clone https://github.com/psomareddy/sample-custom-extension.git
cd sample-custom-extension
./gradlew clean build
```
The extension jar should be built in the **build/libs/sample-custom-extension.jar** location. 

## Test application
Install the [simple-java-app](https://github.com/psomareddy/simple-java-app) to test this extension library. This extension will add some span tags to the spans produced by the test application. 

## Instrument the app to load the custom auto instrumentation extension

Set the -Dotel.javaagent.extensions property to the path of the custom extension jar- sample-custom-extension.jar . For the simple-java-app, the start application command should now look like this. Before running this command, copy the sample-custom-extension.jar file to the simple-java-app home folder. Then change current working directory to the simple-java-app folder.

```
java  -javaagent:./splunk-otel-javaagent.jar "-Dotel.instrumentation.methods.include=com.simple.app.App[getGreeting]" -Dotel.javaagent.extensions=./sample-custom-extension.jar -jar ./build/libs/simple-java-app.jar
```

## Usage
As before, once started, the application should run for about 3 minutes invoking the App.getGreeting() every two seconds method which prints "Hello World! {iteration.value}". The instrumentation will pick up on these invocations and create a span for each execution.

In addition, this custom extension jar will add a the span tags of iteration.value and splunk.user.email to each App.getGreeting operation span. Of course, you should edit these lines - [SampleInstrumentation.java file](https://github.com/psomareddy/sample-custom-extension/blob/main/src/main/java/com/splunk/field/extension/SampleInstrumentation.java#L40-L44) to create span tags relevant to your real world application.

To apply this instrumentation to a different application target class.method, change the class, method names and number of method arguments [here](https://github.com/psomareddy/sample-custom-extension/blob/main/src/main/java/com/splunk/field/extension/SampleInstrumentation.java#L19-L21) and then rebuild the jar. 

