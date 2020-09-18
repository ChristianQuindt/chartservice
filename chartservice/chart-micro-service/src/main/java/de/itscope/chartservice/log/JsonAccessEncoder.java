package de.itscope.chartservice.log;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.encoder.EncoderBase;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonAccessEncoder extends EncoderBase<IAccessEvent> {

	@Override
	public byte[] encode(IAccessEvent event) {
		final StringWriter outputWriter = new StringWriter();
		final JsonWriter jsonWriter = new JsonWriter(outputWriter);

		try {
			jsonWriter.beginObject();
			jsonWriter.name("logType").value("access");
			jsonWriter.name("timestamp").value(event.getTimeStamp());
			jsonWriter.name("method").value(event.getMethod());
			jsonWriter.name("status").value(event.getStatusCode());

			writeRequestObject(event, jsonWriter);
			writeResponseObject(event, jsonWriter);

			jsonWriter.endObject();

			jsonWriter.flush();
			outputWriter.flush();
			outputWriter.write("\n");
			return outputWriter.toString().getBytes(StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	@Override
	public byte[] footerBytes() {
		return new byte[0];
	}

	@Override
	public byte[] headerBytes() {
		return new byte[0];
	}

	private void writeRequestObject(IAccessEvent event, JsonWriter jsonWriter) throws IOException {
		jsonWriter.name("request").beginObject();

		jsonWriter.name("headers").beginObject();
		for (Map.Entry<String, String> requestHeader : event.getRequestHeaderMap().entrySet()) {
			jsonWriter.name(requestHeader.getKey()).value(requestHeader.getValue());
		}
		jsonWriter.endObject();

		jsonWriter.name("method").value(event.getRequest().getMethod());
		jsonWriter.name("remoteAddress").value(event.getRemoteAddr());
		jsonWriter.name("url").value(event.getRequestURL());
		jsonWriter.endObject();
	}

	private void writeResponseObject(IAccessEvent event, JsonWriter jsonWriter) throws IOException {
		jsonWriter.name("response").beginObject();
		jsonWriter.name("contentLength").value(event.getContentLength());
		jsonWriter.name("time").value(event.getElapsedTime());
		jsonWriter.name("status").value(event.getStatusCode());
		jsonWriter.endObject();
	}
}
