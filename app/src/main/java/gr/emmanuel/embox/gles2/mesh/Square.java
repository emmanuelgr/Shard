/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gr.emmanuel.embox.gles2.mesh;

public class Square extends Mesh {

	public Square() {
		super();
	}

	@Override
	protected float[] vertices() {
		return new float[]{
						// top left
						-0.5f, 0.5f, 0.0f, 1f,
						// bottom left
						-0.5f, -0.5f, 0.0f, 1f,
						// bottom right
						0.5f, -0.5f, 0.0f, 1f,
						// top right
						0.5f, 0.5f, 0.0f, 1f
		};
	}

	@Override
	protected short[] indices() {
		return  new short[]{
						0, 1, 2,
						0, 2, 3
		};
	}

	@Override
	protected float[] uvs() {
		return new float[]{
						0.0f, 0.0f,
						0.0f, 1.0f,
						1.0f, 1.0f,
						1.0f, 0.0f
		};
	}

	@Override
	protected float[] colors() {
		return null;
	}
}
