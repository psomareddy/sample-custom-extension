package com.splunk.field.extension;

import java.util.List;

import static java.util.Collections.singletonList;

import com.google.auto.service.AutoService;

import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;

@AutoService(InstrumentationModule.class)
public final class SampleInstrumentationModule extends InstrumentationModule {

	public SampleInstrumentationModule() {
		super("sampleextension", "sampleextension-1.0");
	}

	@Override
	public List<TypeInstrumentation> typeInstrumentations() {
		return singletonList(new SampleInstrumentation());
	}

}
