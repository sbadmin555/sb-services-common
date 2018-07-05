package com.sb.services.common.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Provides a way to convert original HTTP request input stream into a string
 * stream.
 * 
 * @author vkirodia
 *
 */
public class JsonResponseWrapper extends HttpServletResponseWrapper {

	private final ByteArrayOutputStream baos;
	private ServletOutputStream output;
	private PrintWriter writer;

	/**
	 * Provides a way to convert original HTTP request input stream into a
	 * string stream.
	 * 
	 * @param response
	 */
	public JsonResponseWrapper(HttpServletResponse response) {
		super(response);
		baos = new ByteArrayOutputStream(response.getBufferSize());
	}

	@Override
	public ServletOutputStream getOutputStream() {
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called on this response.");
		}

		if (output == null) {
			output = new ServletOutputStream() {
				@Override
				public void write(int b) throws IOException {
					baos.write(b);
				}

				@Override
				public void flush() throws IOException {
					baos.flush();
				}

				@Override
				public void close() throws IOException {
					baos.close();
				}

				@Override
				public boolean isReady() {
					return false;
				}

				@Override
				public void setWriteListener(WriteListener arg0) {
				}
			};
		}

		return output;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (output != null) {
			throw new IllegalStateException("getOutputStream() has already been called on this response.");
		}

		if (writer == null) {
			writer = new PrintWriter(new OutputStreamWriter(baos, getCharacterEncoding()));
		}

		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		super.flushBuffer();

		if (writer != null) {
			writer.flush();
		} else if (output != null) {
			output.flush();
		}
	}

	private byte[] getResponseAsBytes() throws IOException {
		if (writer != null) {
			writer.close();
		} else if (output != null) {
			output.close();
		}

		return baos.toByteArray();
	}

	/**
	 * Returns string representation of the request stream.
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getResponseAsString() throws IOException {
		return new String(getResponseAsBytes(), getCharacterEncoding());
	}
}
