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

package de.jurihock.voicesmith.dsp.processors;

import static de.jurihock.voicesmith.dsp.Math.PI;
import static de.jurihock.voicesmith.dsp.Math.abs;
import static de.jurihock.voicesmith.dsp.Math.imag;
import static de.jurihock.voicesmith.dsp.Math.random;
import static de.jurihock.voicesmith.dsp.Math.real;

public final class HoarsenessProcessor
{
	public static void processFrame(float[] frame)
	{
		final int fftSize = frame.length / 2;
		float re, im, abs, phase;

		for (int i = 1; i < fftSize; i++)
		{
			// Get source Re and Im parts
			re = frame[2 * i];
			im = frame[2 * i + 1];
			abs = abs(re, im);

			// Compute random phase
			phase = random(-PI, PI);

			// Compute destination Re and Im parts
			frame[2 * i] = real(abs, phase);
			frame[2 * i + 1] = imag(abs, phase);
		}
	}
}
