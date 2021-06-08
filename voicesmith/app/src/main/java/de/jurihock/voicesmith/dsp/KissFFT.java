/*
 * Voicesmith <http://voicesmith.jurihock.de/>
 *
 * Copyright (c) 2011-2014 Juergen Hock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.jurihock.voicesmith.dsp;

import de.jurihock.voicesmith.Disposable;

public final class KissFFT implements Disposable
{
	private final long	handle;

	public KissFFT(int size)
	{
		handle = alloc(size);
	}

	public void dispose()
	{
		free(handle);
	}

	public void fft(float[] buffer)
	{
		fft(handle, buffer);
	}

	public void ifft(float[] buffer)
	{
		ifft(handle, buffer);
	}

	// JNI calls
	private native long alloc(int size);

	private native void free(long handle);

	private native void fft(long handle, float[] buffer);

	private native void ifft(long handle, float[] buffer);
}
