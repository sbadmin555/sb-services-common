package com.sb.services.common.interceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Provides a way to convert original HTTP request input stream into a buffered
 * stream.
 * 
 * @author vkirodia
 *
 */
public class BufferedRequestWrapper extends HttpServletRequestWrapper {

	ByteArrayInputStream bais;

	ByteArrayOutputStream baos;

	BufferedServletInputStream bsis;

	byte[] buffer;

	/**
	 * Provides a way to convert original HTTP request input stream into a
	 * buffered stream.
	 * 
	 * @param req
	 *            the request whose input stream needs to be converted into
	 *            buffered stream.
	 * @throws IOException
	 */
	public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
		super(req);
		InputStream is = req.getInputStream();
		baos = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int letti;
		while ((letti = is.read(buf)) > 0) {
			baos.write(buf, 0, letti);
		}
		buffer = baos.toByteArray();
	}

	public ServletInputStream getInputStream() {
		try {
			bais = new ByteArrayInputStream(buffer);
			bsis = new BufferedServletInputStream(bais);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return bsis;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	private class BufferedServletInputStream extends ServletInputStream {

		ByteArrayInputStream bais;

		public BufferedServletInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		public int available() {
			return bais.available();
		}

		public int read() {
			return bais.read();
		}

		public int read(byte[] buf, int off, int len) {
			return bais.read(buf, off, len);
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener listener) {
		}

	}

	public static class ByteArrayPrintWriter {

		private ByteArrayOutputStream baos = new ByteArrayOutputStream();

		private PrintWriter pw = new PrintWriter(baos);

		private ServletOutputStream sos = new ByteArrayServletStream(baos);

		public PrintWriter getWriter() {
			return pw;
		}

		public ServletOutputStream getStream() {
			return sos;
		}

		byte[] toByteArray() {
			return baos.toByteArray();
		}
	}

	private static class ByteArrayServletStream extends ServletOutputStream {

		ByteArrayOutputStream baos;

		ByteArrayServletStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}

		public void write(int param) throws IOException {
			baos.write(param);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener listener) {
		}
	}

}