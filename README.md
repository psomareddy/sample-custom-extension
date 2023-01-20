This is a really simple sample java app used to demonstrate the ability to add custom tags to spans delivered through opentelemetry auto instrumentation extension.

## Running simple-java-app locally
simple-java-app is a java application built using gradle. You can build a jar file and run it from the command line (it should work just as well with Java 11 or newer):

```
git clone https://github.com/psomareddy/sample-custom-extension.git
./gradlew clean build
```
The custom extension jar should be available in the **build/libs/sample-custom-extension.jar** location.

## Instrument the app to load the custom auto instrumentation extension

Set the -Dotel.javaagent.extensions property to the path of the custom extension jar- sample-custom-extension.jar . For the simple-java-app, the start application command should now look like this.

```
java  -javaagent:/opt/otel/splunk-otel-javaagent.jar  -Dotel.instrumentation.methods.include=com.simple.app.App[getGreeting] -Dotel.javaagent.extensions=/opt/otel/extensions/sample-custom-extension.jar -jar build/libs/*.jar 
```

## Usage
As before, once started, the application should run for about 3 minutes invoking the App.getGreeting() every two seconds method which prints "Hello World! {iteration}". The instrumentation will pick up on these invocations and create a span for each exectution.

In addition, this custom extension jar will add a custom span tag of custom-tag-name=custom-tag-value. Of course, you should edit the [SampleInstrumentation.java file](https://github.com/psomareddy/sample-custom-extension/blob/main/src/main/java/com/splunk/field/extension/SampleInstrumentation.java#L41) to modify the line to add whatever custom attributes makes sense to your application in the real world.




