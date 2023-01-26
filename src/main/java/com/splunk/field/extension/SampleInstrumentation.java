package com.splunk.field.extension;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

public class SampleInstrumentation implements TypeInstrumentation {

	private static String originalClass = "com.simple.app.App";
	private static String originalMethod = "getGreeting";
	private static int originalMethodArgumentCount = 1;

	@Override
	public ElementMatcher<TypeDescription> typeMatcher() {
		return AgentElementMatchers.hasSuperType(named(originalClass));
	}

	@Override
	public void transform(TypeTransformer transformer) {
		transformer.applyAdviceToMethod(named(originalMethod).and(takesArguments(originalMethodArgumentCount)),
				this.getClass().getName() + "$ExtensionAdvice");
	}

	public static class ExtensionAdvice {
		@Advice.OnMethodEnter(suppress = Throwable.class)
		public static void onEnter(@Advice.Argument(0) int i, @Advice.Local("otelContext") Context context,
				@Advice.Local("otelScope") Scope scope) {

			Span currentSpan = Java8BytecodeBridge.currentSpan();
			// notice that the span tag value of i is read from method argument
			currentSpan.setAttribute("iteration.value", i);
			// although hard-coded here, you will probably read the email value from some field,
			// argument or method return value on the original class
			currentSpan.setAttribute("splunk.user.email", "prakash.reddy@splunk.com");

		}

	}

}
