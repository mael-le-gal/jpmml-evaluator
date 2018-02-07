/*
 * Copyright (c) 2018 Villu Ruusmann
 *
 * This file is part of JPMML-Evaluator
 *
 * JPMML-Evaluator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JPMML-Evaluator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with JPMML-Evaluator.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jpmml.evaluator;

import java.util.StringJoiner;

public class ToStringHelper {

	private StringJoiner joiner = null;


	public ToStringHelper(Object object){
		setJoiner(new StringJoiner(", ", (object.getClass()).getSimpleName() + "{", "}"));
	}

	public ToStringHelper add(String key, Object value){
		StringJoiner joiner = getJoiner();

		joiner.add(key + "=" + value);

		return this;
	}

	@Override
	public String toString(){
		StringJoiner joiner = getJoiner();

		return joiner.toString();
	}

	public StringJoiner getJoiner(){
		return this.joiner;
	}

	private void setJoiner(StringJoiner joiner){
		this.joiner = joiner;
	}
}