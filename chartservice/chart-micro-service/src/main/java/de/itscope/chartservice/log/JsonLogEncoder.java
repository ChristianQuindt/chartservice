package de.itscope.chartservice.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.encoder.EncoderBase;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonLogEncoder extends EncoderBase<ILoggingEvent> {

	@Override
	public byte[] encode(ILoggingEvent event) {
		final StringWriter outputWriter = new StringWriter();
		final JsonWriter jsonWriter = new JsonWriter(outputWriter);

		try {
			jsonWriter.beginObject();
			jsonWriter.name("logType").value("log");
			jsonWriter.name("timestamp").value(event.getTimeStamp());
			jsonWriter.name("level").value(String.valueOf(event.getLevel()));
			jsonWriter.name("thread").value(event.getThreadName());
			jsonWriter.name("logger").value(event.getLoggerName());
			jsonWriter.name("message").value(event.getMessage());

			final IThrowableProxy throwableProxy = event.getThrowableProxy();
			if (throwableProxy != null) {
				jsonWriter.name("exception").value(ThrowableProxyUtil.asString(throwableProxy));
			}

			final Map<String, String> mdc = event.getMDCPropertyMap();
			for (Map.Entry<String, String> entry : mdc.entrySet()) {
				jsonWriter.name("mdc_" + entry.getKey()).value(entry.getValue());
			}

			jsonWriter.endObject();

			jsonWriter.flush();
			outputWriter.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		outputWriter.flush();
		return outputWriter.toString().getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public byte[] footerBytes() {
		return new byte[0];
	}

	@Override
	public byte[] headerBytes() {
		return new byte[0];
	}
}
