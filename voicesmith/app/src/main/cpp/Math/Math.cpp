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

#include "Math.h"
#include "MathDefs.h"
#include <stdlib.h>

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_pow
  (JNIEnv *, jclass, jfloat base, jfloat exponent)
{
	return powf(base, exponent);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_log10
  (JNIEnv *, jclass, jfloat value)
{
	return log10f(value);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_min
  (JNIEnv *, jclass, jfloat a, jfloat b)
{
	return fmin(a, b);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_max
  (JNIEnv *, jclass, jfloat a, jfloat b)
{
	return fmax(a, b);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_floor
  (JNIEnv *, jclass, jfloat value)
{
	return floorf(value);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_ceil
  (JNIEnv *, jclass, jfloat value)
{
	return ceilf(value);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_sin
  (JNIEnv *, jclass, jfloat angle)
{
	return sinf(angle);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_cos
  (JNIEnv *, jclass, jfloat angle)
{
	return cosf(angle);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_sqrt
  (JNIEnv *, jclass, jfloat value)
{
	return sqrtf(value);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_atan2
  (JNIEnv *, jclass, jfloat y, jfloat x)
{
	return atan2f(y, x);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_abs
  (JNIEnv *, jclass, jfloat real, jfloat imag)
{
	return sqrtf(real * real + imag * imag);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_arg
  (JNIEnv *, jclass, jfloat real, jfloat imag)
{
	return atan2f(imag, real);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_real
  (JNIEnv *, jclass, jfloat abs, jfloat arg)
{
	return abs * cosf(arg);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_imag
  (JNIEnv *, jclass, jfloat abs, jfloat arg)
{
	return abs * sinf(arg);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_random
  (JNIEnv *, jclass, jfloat min, jfloat max)
{
	// TODO: min < result <= max

	float next = rand() / (float)RAND_MAX;

	// min <= result < max
	return min + next * (max - min);
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_princarg
  (JNIEnv *, jclass, jfloat phase)
{
	return princargf(phase);
}

JNIEXPORT jshort JNICALL Java_de_jurihock_voicesmith_dsp_Math_mean
  (JNIEnv *env, jclass, jshortArray _buffer, jint offset, jint length)
{
	short* buffer = (short*)env->GetPrimitiveArrayCritical(_buffer, 0);

        long mean = 0;

        for (int i = offset; i < offset + length; i++)
        {
            mean += buffer[i];
        }

        mean /= length;

	env->ReleasePrimitiveArrayCritical(_buffer, buffer, 0);
        return (short)mean;
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_rms___3SII
  (JNIEnv *env, jclass, jshortArray _buffer, jint offset, jint length)
{
	short* buffer = (short*)env->GetPrimitiveArrayCritical(_buffer, 0);

        float rms = 0;

        for (int i = offset; i < offset + length; i++)
        {
            float value = buffer[i] / SHRT_MAX_F;
            rms += value * value;
        }

        rms = sqrtf(rms / length);

	env->ReleasePrimitiveArrayCritical(_buffer, buffer, 0);
	return rms;
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_rms___3SIIS
  (JNIEnv *env, jclass, jshortArray _buffer, jint offset, jint length, jshort mean)
{
	short* buffer = (short*)env->GetPrimitiveArrayCritical(_buffer, 0);

        float rms = 0;

        for (int i = offset; i < offset + length; i++)
        {
            float value = (buffer[i] - mean) / SHRT_MAX_F;
            rms += value * value;
        }

        rms = sqrtf(rms / length);

	env->ReleasePrimitiveArrayCritical(_buffer, buffer, 0);
	return rms;
}

JNIEXPORT jfloat JNICALL Java_de_jurihock_voicesmith_dsp_Math_rms2dbfs
 (JNIEnv *, jclass, jfloat value, jfloat min, jfloat max)
{
	value = fmin(fmax(value,min),max);

	return 10.0f*log10f(value);
}

