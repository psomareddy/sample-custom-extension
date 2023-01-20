package com.splunk.field.extension;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

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
	
	public SampleInstrumentation() {
		
	}

	@Override
	public ElementMatcher<TypeDescription> typeMatcher() {
		return AgentElementMatchers.hasSuperType(named("com.simple.app.App"));
	}

	@Override
	public void transform(TypeTransformer transformer) {
		transformer.applyAdviceToMethod(named("getGreeting").and(takesArguments(1)), 
				 this.getClass().getName() + "$AddCustomSpanTagsAdvice");
	}
	
	@SuppressWarnings("unused")
	  public static class AddCustomSpanTagsAdvice {
	    @Advice.OnMethodEnter(suppress = Throwable.class)
	    public static void onEnter(
	        @Advice.Argument(0) int i,
	        @Advice.Local("otelContext") Context context,
	        @Advice.Local("otelScope") Scope scope) {
	    	
	    	Java8BytecodeBridge.currentSpan().setAttribute("custom-tag-name", "custom-tag-value" + i);
	    }
	    
	    
	  }


}
